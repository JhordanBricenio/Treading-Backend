package com.codej.service;

import com.codej.domain.VerificationType;
import com.codej.model.UserEntity;
import com.codej.response.AuthResponse;

import java.util.List;

public interface IUserService {

    public AuthResponse save (UserEntity UserEntity);
    public UserEntity findByEmail(String email);
    public UserEntity findUserProfileByJwt(String jwt);
    public UserEntity finUserById(Long userId);
    public UserEntity enableTwoFactorAuthentication(VerificationType verificationType,String sendTo,UserEntity user);
    public UserEntity updatePassword( UserEntity user, String newPassword) ;

    public void deleteUser(Long id);
}
