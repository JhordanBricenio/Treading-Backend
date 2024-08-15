package com.codej.controller;

import com.codej.model.*;
import com.codej.service.IOrderService;
import com.codej.service.IPaymentService;
import com.codej.service.IUserService;
import com.codej.service.IWalletService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final IWalletService walletService;

    private final IUserService userService;

    private final IOrderService orderService;

    private final IPaymentService paymentService;

    @GetMapping
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt){
        UserEntity user = userService.findUserProfileByJwt(jwt);
        Wallet wallet= walletService.getUserWallet(user);
        return  new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);

    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader("Authorization") String jwt,
                                                         @PathVariable Long walletId, @RequestBody WalletTransaction req) throws Exception {
        UserEntity senderUser= userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet= walletService.findWalletById(walletId);
        Wallet wallet = walletService.walletToWalletTransfer(senderUser, receiverWallet, req.getAmount());
        return  new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/order/{oderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(@RequestHeader("Authorization") String jwt,
                                                         @PathVariable Long orderId) throws Exception {

        UserEntity user= userService.findUserProfileByJwt(jwt);
        Order order= orderService.getOrderById(orderId);
        Wallet wallet= walletService.payOrderPayment(order, user);

        return  new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/deposit")
    public ResponseEntity<Wallet> addBalanceToWallet(@RequestHeader("Authorization") String jwt,
                                                  @RequestParam(name = "order_id") Long orderId,
                                                     @RequestParam(name = "payment_id")String paymentId)
            throws Exception {

        UserEntity user= userService.findUserProfileByJwt(jwt);
        Wallet wallet= walletService.getUserWallet(user);
        PaymentOrder order= paymentService.getPaymentOrderById(orderId);
        Boolean status= paymentService.procedPaymentOrder(order, paymentId);

        if (status){
            wallet= walletService.addBalance(wallet, order.getAmount());
        }

        return  new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }





}
