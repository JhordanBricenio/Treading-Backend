package com.codej.service.impl;

import com.codej.model.TwoFactorOTP;
import com.codej.model.UserEntity;
import com.codej.repository.ITwoFactorOtpRepository;
import com.codej.service.ITwoFactorOtpService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TwoFactorOtpServiceImpl implements ITwoFactorOtpService {

    private ITwoFactorOtpRepository twoFactorOtpRepository;
    @Override
    public TwoFactorOTP createTwoFactorOtp(UserEntity user, String otp, String jwt) {
        UUID uuid = UUID.randomUUID();
        String id= uuid.toString();
        TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
        twoFactorOTP.setId(id);
        twoFactorOTP.setOtp(otp);
        twoFactorOTP.setJwt(jwt);
        twoFactorOTP.setUser(user);

        return twoFactorOtpRepository.save(twoFactorOTP);
    }

    @Override
    public TwoFactorOTP findByUser(Long userId) {
        return twoFactorOtpRepository.finByUserId(userId);
    }

    @Override
    public TwoFactorOTP findById(String id) {
        return twoFactorOtpRepository.findById(id).orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp) {
        return twoFactorOTP.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP) {
        twoFactorOtpRepository.delete(twoFactorOTP);
    }
}
