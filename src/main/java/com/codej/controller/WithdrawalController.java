package com.codej.controller;

import com.codej.model.UserEntity;
import com.codej.model.Wallet;
import com.codej.model.Withdrawal;
import com.codej.service.IUserService;
import com.codej.service.IWalletService;
import com.codej.service.IWithdrawalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/withdrawal")
public class WithdrawalController {

    private IWithdrawalService withdrawalService;
    private IWalletService walletService;
    private IUserService userService;


    @GetMapping("/{amount}")
    public ResponseEntity<?> withdrawalRequest(@PathVariable Long amount,
                                               @RequestHeader("Authorization") String jwt){
        UserEntity user=findUserProfileByJwt(jwt);
        Wallet userWallet= walletService.getUserWallet(user);
        Withdrawal withdrawal= withdrawalService.requestWithdrawal(amount, user);
        walletService.addBalance(userWallet, -withdrawal.getAmount());

        //WalletTransaction walletTransaction= walletSer
      return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @PatchMapping("/admin/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(@PathVariable Long id,
                                               @PathVariable boolean accept, @RequestHeader("Authorization") String jwt) throws Exception {
        UserEntity user=findUserProfileByJwt(jwt);
        Withdrawal withdrawal= withdrawalService.procedWithwithdrawal(id, accept);
        Wallet userWallet= walletService.getUserWallet(user);
        if (!accept){
            walletService.addBalance(userWallet, withdrawal.getAmount());
        }
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @GetMapping
    public  ResponseEntity<List<Withdrawal>> getWithdrawalHistory(@RequestHeader("Authorization") String jwt) {
        UserEntity user=findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawals= withdrawalService.getUsersWithdrawalHistory(user);
        return new ResponseEntity<>(withdrawals, HttpStatus.OK);

    }

    @GetMapping("/admin")
    public  ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest() {

        List<Withdrawal> withdrawals= withdrawalService.getAllWithdrawalRequest();
        return new ResponseEntity<>(withdrawals, HttpStatus.OK);

    }

    private UserEntity findUserProfileByJwt(String jwt){
        return userService.findUserProfileByJwt(jwt);
    }

}
