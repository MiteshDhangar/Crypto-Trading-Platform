package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Model.Order;
import com.mitesh.TradingPlatform.Model.User;
import com.mitesh.TradingPlatform.Model.Wallet;


public interface WalletService {
    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet wallet,Long amount);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception;
    Wallet payOrderPayment(Order order, User user) throws Exception;
}
