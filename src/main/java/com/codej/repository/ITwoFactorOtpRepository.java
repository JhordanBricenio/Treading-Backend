package com.codej.repository;

import com.codej.model.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ITwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String>{

    @Query("SELECT t FROM TwoFactorOTP t WHERE t.user.id = ?1")
    TwoFactorOTP finByUserId(Long userId);
}
