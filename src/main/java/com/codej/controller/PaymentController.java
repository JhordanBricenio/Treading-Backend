package com.codej.controller;

import com.codej.domain.PaymentMethod;
import com.codej.model.PaymentOrder;
import com.codej.model.UserEntity;
import com.codej.response.PaymentResponse;
import com.codej.service.IPaymentService;
import com.codej.service.IUserService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PaymentController {

    private final IUserService userService;

    private IPaymentService paymentService;

    @PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
    ResponseEntity<PaymentResponse> paymentHandler(@PathVariable PaymentMethod paymentMethod,
                                                   @PathVariable Long amount, @RequestHeader("Authorization") String jwt)
            throws RazorpayException, StripeException {

        UserEntity user = userService.findUserProfileByJwt(jwt);
        PaymentResponse paymentResponse;
        PaymentOrder order= paymentService.createOrder(user, amount, paymentMethod);

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)){
            paymentResponse= paymentService.createRazorpayPaymentLing(user, amount);
        }else {
            paymentResponse= paymentService.createStripePaymentLing(user, amount, order.getId());
        }

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

}
