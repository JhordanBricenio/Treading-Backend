package com.codej.repository;

import com.codej.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository  extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}