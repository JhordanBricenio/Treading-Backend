package com.codej.service;

import com.codej.model.PaymentDetails;
import com.codej.model.UserEntity;

public interface IPaymentDetailsService {
    PaymentDetails addPaymentDetails(String accountNumber,
                                                             String accountHolderName,
                                                             String ifsc,
                                                             String bankName,
                                                             UserEntity user);

    PaymentDetails getUsersPaymentDetails(UserEntity user);
}
