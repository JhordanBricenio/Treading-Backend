package com.codej.service;

import com.codej.domain.VerificationType;
import com.codej.model.ForgotPasswordToken;
import com.codej.model.UserEntity;

public interface IForgotPasswordService {
    public ForgotPasswordToken createToken(UserEntity user, String id, String otp, VerificationType
                                           verificationType, String sendTo);

    public ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteForgotPasswordToken(ForgotPasswordToken id);
}
