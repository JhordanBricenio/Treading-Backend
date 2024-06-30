package com.codej.service;

import com.codej.model.TwoFactorOTP;
import com.codej.model.UserEntity;

public interface ITwoFactorOtpService {
    TwoFactorOTP createTwoFactorOtp(UserEntity user, String otp, String jwt);
    TwoFactorOTP findByUser(Long userId);
    TwoFactorOTP findById(String id);
    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);
    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);
}
