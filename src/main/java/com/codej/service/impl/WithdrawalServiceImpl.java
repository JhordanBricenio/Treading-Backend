package com.codej.service.impl;

import com.codej.domain.WithdrawalStatus;
import com.codej.model.UserEntity;
import com.codej.model.Withdrawal;
import com.codej.repository.IWithdrawalRepository;
import com.codej.service.IWithdrawalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WithdrawalServiceImpl implements IWithdrawalService {

    private final IWithdrawalRepository withdrawalRepository;


    @Override
    public Withdrawal requestWithdrawal(Long amount, UserEntity user) {
        Withdrawal withdrawal= new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setStatus(WithdrawalStatus.PENDING);

        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal procedWithwithdrawal(Long withdrawalId, boolean accept) throws Exception {
        Optional<Withdrawal> withdrawal= withdrawalRepository.findById(withdrawalId);
        if(withdrawal.isEmpty()){
            throw  new Exception("withdrawal not found");
        }
        Withdrawal withdrawal1= withdrawal.get();
        withdrawal1.setDate(LocalDateTime.now());
        if (accept){
            withdrawal1.setStatus(WithdrawalStatus.SUCCESS);
        }else {
            withdrawal1.setStatus(WithdrawalStatus.PENDING);
        }
        return withdrawalRepository.save(withdrawal1);
    }

    @Override
    public List<Withdrawal> getUsersWithdrawalHistory(UserEntity user) {

        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }
}
