package com.codej.service;

import com.codej.model.UserEntity;
import com.codej.model.Withdrawal;

import java.util.List;

public interface IWithdrawalService {
    Withdrawal requestWithdrawal(Long amount, UserEntity user);
    Withdrawal procedWithwithdrawal(Long withdrawalId, boolean accept) throws Exception;
    List<Withdrawal> getUsersWithdrawalHistory(UserEntity user);
    List<Withdrawal> getAllWithdrawalRequest();
}
