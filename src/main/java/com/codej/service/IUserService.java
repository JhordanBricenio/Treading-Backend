package com.codej.service;

import com.codej.model.UserEntity;
import com.codej.response.AuthResponse;

import java.util.List;

public interface IUserService {

    public AuthResponse save (UserEntity UserEntity);
    public UserEntity findByEmail(String email);
    public UserEntity findUserProfileByJwt(String jwt);
    public UserEntity getUser(Long id);
    public List<UserEntity> getUsers();
    public UserEntity findUserByJwt(String jwt) ;
    public void deleteUser(Long id);
}
