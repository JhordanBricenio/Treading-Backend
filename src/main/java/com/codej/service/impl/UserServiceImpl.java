package com.codej.service.impl;

import com.codej.config.JwtProvider;
import com.codej.exception.UserException;
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

import java.util.List;

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
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findUserProfileByJwt(String jwt) {
        String email = jwtProvider.getEmailFromToken(jwt);
        UserEntity user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public UserEntity getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserEntity> getUsers() {
        return List.of();
    }

    @Override
    public UserEntity findUserByJwt(String jwt) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
