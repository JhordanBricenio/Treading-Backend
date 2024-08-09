package com.codej.service;

import com.codej.domain.VerificationType;
import com.codej.model.UserEntity;
import com.codej.response.AuthResponse;



public interface IUserService {

    public AuthResponse save (UserEntity user);
    public UserEntity findByEmail(String email);
    public UserEntity findUserProfileByJwt(String jwt);
    public UserEntity finUserById(Long userId);
    public UserEntity enableTwoFactorAuthentication(VerificationType verificationType,String sendTo,UserEntity user);
    public UserEntity updatePassword( UserEntity user, String newPassword) ;

}
