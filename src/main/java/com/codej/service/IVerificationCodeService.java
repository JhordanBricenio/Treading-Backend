package com.codej.service;

import com.codej.domain.VerificationType;
import com.codej.model.UserEntity;
import com.codej.model.VerificationCode;

public interface IVerificationCodeService {
    VerificationCode sendVerificationCode(UserEntity user, VerificationType verificationType);
    VerificationCode getVerificationCodeById(Long id);
    VerificationCode getVerificationCodeByUser(Long userId);
    void deleteVerificationCodeById(VerificationCode verificationCode);
}
