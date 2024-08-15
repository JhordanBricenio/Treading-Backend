package com.codej.repository;

import com.codej.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

}
