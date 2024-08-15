package com.codej.service;

import com.codej.domain.PaymentMethod;
import com.codej.model.PaymentOrder;
import com.codej.model.UserEntity;
import com.codej.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface IPaymentService {

    PaymentOrder createOrder(UserEntity user, Long amount, PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean procedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLing(UserEntity user, Long amount) throws RazorpayException;

    PaymentResponse createStripePaymentLing(UserEntity user, Long amount, Long orderId) throws StripeException;


}
