package com.codej.service.impl;

import com.codej.config.JwtProvider;
import com.codej.domain.VerificationType;
import com.codej.exception.UserException;
import com.codej.model.TwoFactorAuth;
import com.codej.model.UserEntity;
import com.codej.repository.IUserRepository;
import com.codej.response.AuthResponse;
import com.codej.service.IUserService;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class UserServiceImpl  implements IUserService {

    private IUserRepository userRepository;

    private JwtProvider jwtProvider;

    private PasswordEncoder passwordEncoder;


    @Override
    public AuthResponse save(UserEntity userEntity) {
        UserEntity isExistEmail= findByEmail(userEntity.getEmail());
        if(isExistEmail!=null){
            throw new UserException("Email is already exist with email: "+userEntity.getEmail());

        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

         userRepository.save(userEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token= jwtProvider.generateToken(authentication);
        AuthResponse authResponse= new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setStatus(true);
        authResponse.setMessage("User created successfully");

        return authResponse;
    }

    @Override
    public UserEntity findByEmail(String email) {
        UserEntity user=userRepository.findByEmail(email);
        if(user==null){
            throw  new UserException("User not found");
        }
        return user;
    }

    @Override
    public UserEntity findUserProfileByJwt(String jwt) {
        String email = jwtProvider.getEmailFromToken(jwt);
        UserEntity user=userRepository.findByEmail(email);
        if(user==null){
            throw  new UserException("User not found");
        }
        return user;
    }

    @Override
    public UserEntity finUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    @Override
    public UserEntity enableTwoFactorAuthentication(VerificationType verificationType, String sendTo,UserEntity user) {
        TwoFactorAuth twoFactorAuth= new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSetSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }

    @Override
    public UserEntity updatePassword(UserEntity user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }


}
