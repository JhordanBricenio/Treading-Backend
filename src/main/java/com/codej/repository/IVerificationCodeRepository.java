package com.codej.repository;

import com.codej.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    public VerificationCode findByUserId(Long id);
}
