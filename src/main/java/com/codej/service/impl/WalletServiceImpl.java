package com.codej.service.impl;

import com.codej.domain.OrderType;
import com.codej.model.Order;
import com.codej.model.UserEntity;
import com.codej.model.Wallet;
import com.codej.repository.IWalletRepository;
import com.codej.service.IWalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements IWalletService {

    private IWalletRepository walletRepository;


    @Override
    public Wallet getUserWallet(UserEntity user) {
        Wallet wallet= walletRepository.findByUserId(user.getId());
        if(wallet==null){
            wallet= new Wallet();
            wallet.setUser(user);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long money) {
        BigDecimal balance= wallet.getBalance();
        BigDecimal newBalance= balance.add(BigDecimal.valueOf(money));
        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isPresent()){
            return wallet.get();
        }
        throw  new Exception("Wallet not found");
    }

    @Override
    public Wallet walletToWalletTransfer(UserEntity sender, Wallet receiverWallet, Long amount) throws Exception {
        Wallet senderWallet= getUserWallet(sender);
        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount))<0){
            throw new Exception("Insufficient balance");
        }
        BigDecimal senderBalance= senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);

        BigDecimal receiverBalance= receiverWallet.getBalance().subtract(BigDecimal.valueOf(amount));
        walletRepository.save(receiverWallet);
        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, UserEntity user) throws Exception {
        Wallet wallet= getUserWallet(user);
        if(order.getOrderType().equals(OrderType.BUY)){
            BigDecimal newBalance= wallet.getBalance().subtract(order.getPrice());
            if(newBalance.compareTo(order.getPrice())<0){
                throw new Exception("Insufficient funds for this transaction");
            }
            wallet.setBalance(newBalance);
        }
        else  {
            BigDecimal newBalance= wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }
        walletRepository.save(wallet);
        return wallet;
    }

}
