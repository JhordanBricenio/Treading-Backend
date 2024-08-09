package com.codej.service;

import com.codej.model.PaymentDetails;
import com.codej.model.UserEntity;
import com.codej.repository.IPaymentDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentDetailsServiceImpl implements IPaymentDetailsService{

    private final IPaymentDetailsRepository paymentDetailsRepository;


    @Override
    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolderName,
                                            String ifsc,
                                            String bankName,
                                            UserEntity user) {

         PaymentDetails paymentDetails= new PaymentDetails();
         paymentDetails.setAccountNumber(accountNumber);
         paymentDetails.setAccountHolderName(accountHolderName);
         paymentDetails.setIfsc(ifsc);
         paymentDetails.setBankName(bankName);
         paymentDetails.setUser(user);
        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUsersPaymentDetails(UserEntity user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }
}
