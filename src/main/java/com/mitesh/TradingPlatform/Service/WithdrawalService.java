package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Model.User;
import com.mitesh.TradingPlatform.Model.Withdrawal;

import java.util.List;

public interface WithdrawalService {

    Withdrawal requestWithdrawal(Long amount, User user);
    Withdrawal proceedWithdrawal(Long withdrawalId, boolean accept) throws Exception;
    List<Withdrawal> getUsersWithdrawalHistory(User user);
    List<Withdrawal> getAllWithdrawalRequest();

}
