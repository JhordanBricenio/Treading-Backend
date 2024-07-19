package com.codej.repository;

import com.codej.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, String> {
    ForgotPasswordToken findByUserId(Long userId);
}
