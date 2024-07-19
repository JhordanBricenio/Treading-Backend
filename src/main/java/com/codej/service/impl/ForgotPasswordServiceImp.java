package com.codej.service.impl;

import com.codej.domain.VerificationType;
import com.codej.model.ForgotPasswordToken;
import com.codej.model.UserEntity;
import com.codej.repository.IForgotPasswordRepository;
import com.codej.service.IForgotPasswordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ForgotPasswordServiceImp implements IForgotPasswordService
{
    private final IForgotPasswordRepository forgotPasswordRepository;
    @Override
    public ForgotPasswordToken createToken(UserEntity user, String id, String otp,
                                           VerificationType verificationType, String sendTo) {

        ForgotPasswordToken token= new ForgotPasswordToken();
        token.setUser(user);
        token.setSendTo(sendTo);
        token.setVerificationType(verificationType);
        token.setOtp(otp);
        token.setId(id);
        return forgotPasswordRepository.save(token);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        return forgotPasswordRepository.findById(id).orElseThrow();
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {

        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteForgotPasswordToken(ForgotPasswordToken forgotPasswordToken) {
forgotPasswordRepository.delete(forgotPasswordToken);
    }
}
