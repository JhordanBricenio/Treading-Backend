package com.codej.service.impl;

import com.codej.config.JwtProvider;
import com.codej.model.TwoFactorOTP;
import com.codej.model.UserEntity;
import com.codej.repository.IUserRepository;
import com.codej.request.AuthLogin;
import com.codej.response.AuthResponse;
import com.codej.service.EmailService;
import com.codej.service.ITwoFactorOtpService;
import com.codej.utils.OtpUtils;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private IUserRepository userRepository;

    private JwtProvider jwtUtils;
    private ITwoFactorOtpService twoFactorOtpService;
    private EmailService emailService;

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found with email: "+username);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new User(user.getEmail(), user.getPassword(), authorities);
    }

    public AuthResponse loginUser(AuthLogin authLoginRequest) throws MessagingException {

        String username = authLoginRequest.email();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateToken(authentication);
        UserEntity user = userRepository.findByEmail(username);
        if(user.getTwoFactorAuth().isEnabled()){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor authentication is enabled");
            res.setIsTwoFactorAuthEnabled(true);
            String otp= OtpUtils.generateOtp();
            TwoFactorOTP oldTwoFactorOtp= twoFactorOtpService.findByUser(user.getId());
            if(oldTwoFactorOtp!=null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }
            TwoFactorOTP twoFactorOTP = twoFactorOtpService.createTwoFactorOtp(user,otp,accessToken);
            emailService.sendVerificationOtpEmail(user.getEmail(),otp);
            res.setSession(twoFactorOTP.getId());
            return  res;
        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(accessToken);
        authResponse.setStatus(true);
        authResponse.setMessage("User loged succesfully");
        return authResponse;
    }

    public Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException(String.format("Invalid username or password"));
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public ResponseEntity<AuthResponse> verifySigninOtp(@PathVariable String otp, @RequestParam String id)
            throws  Exception{

        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);
        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP,otp)){
            AuthResponse authResponse = new AuthResponse(twoFactorOTP.getJwt(),true,
                    "User loged succesfully", true, "Active");
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(new  AuthResponse("Invalid OTP", false, "Invalid OTP", false, "Inactive"), HttpStatus.BAD_REQUEST);

    }




}
