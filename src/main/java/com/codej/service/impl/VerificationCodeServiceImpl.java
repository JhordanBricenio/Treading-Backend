package com.codej.service.impl;

import com.codej.domain.VerificationType;
import com.codej.model.UserEntity;
import com.codej.model.VerificationCode;
import com.codej.repository.IVerificationCodeRepository;
import com.codej.service.IVerificationCodeService;
import com.codej.utils.OtpUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VerificationCodeServiceImpl  implements IVerificationCodeService {

    private IVerificationCodeRepository verificationCodeRepository;

    @Override
    public VerificationCode sendVerificationCode(UserEntity user, VerificationType verificationType) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(OtpUtils.generateOtp());
        verificationCode.setVerificationType(verificationType);
        verificationCode.setUser(user);
        return verificationCodeRepository.save(verificationCode);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id)  {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id);
        if(verificationCode.isPresent()){
            return verificationCode.get();
        }
        return null;
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
 verificationCodeRepository.delete(verificationCode);
    }
}
