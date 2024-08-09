package com.codej.controller;


import com.codej.model.PaymentDetails;
import com.codej.model.UserEntity;
import com.codej.service.IPaymentDetailsService;
import com.codej.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PaymentDetailsController {
    private final IUserService userService;
    private final IPaymentDetailsService paymentDetailsService;

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestHeader("Authorization") String jwt,
                                                            @RequestBody PaymentDetails paymentDetails){
        UserEntity user= userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails1= paymentDetailsService.addPaymentDetails(
                paymentDetails.getAccountNumber(),
                paymentDetails.getAccountHolderName(),
                paymentDetails.getIfsc(),
                paymentDetails.getBankName(),
                user
        );
        return  new ResponseEntity<>(paymentDetails1, HttpStatus.CREATED);
    }


    @GetMapping("/payment-details")
    ResponseEntity<PaymentDetails> getUsersPaymentDetails(@RequestHeader("Authorization") String jwt){
        UserEntity user= userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails= paymentDetailsService.getUsersPaymentDetails(user);

        return new ResponseEntity<>(paymentDetails, HttpStatus.OK);

    }

}
