package com.codej.repository;

import com.codej.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {
    PaymentDetails findByUserId(Long userId);
}
