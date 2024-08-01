package com.codej.controller;

import com.codej.model.Order;
import com.codej.model.UserEntity;
import com.codej.model.Wallet;
import com.codej.model.WalletTransaction;
import com.codej.service.IOrderService;
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

    private IWalletService walletService;

    private IUserService userService;

    private IOrderService orderService;

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




}
