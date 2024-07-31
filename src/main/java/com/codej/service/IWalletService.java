package com.codej.service;

import com.codej.model.Order;
import com.codej.model.UserEntity;
import com.codej.model.Wallet;

public interface IWalletService {
    Wallet getUserWallet(UserEntity user);
    Wallet addBalance(Wallet wallet, Long money);
    Wallet findWalletById(Long Id) throws Exception;
    Wallet walletToWalletTransfer(UserEntity user, Wallet receiverWallet, Long amount) throws Exception;
    Wallet payOrderPayment(Order order, UserEntity user) throws Exception;
}
