package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Domain.OrderType;
import com.mitesh.TradingPlatform.Model.Order;
import com.mitesh.TradingPlatform.Model.User;
import com.mitesh.TradingPlatform.Model.Wallet;
import com.mitesh.TradingPlatform.Repository.WalletRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRespository walletRespository;


    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet=walletRespository.findByUserId(user.getId());
        if (wallet==null){
            wallet=new Wallet();
            wallet.setUser(user);
            walletRespository.save(wallet);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long amount) {
        BigDecimal balance=wallet.getBalance();
        BigDecimal newBalance=balance.add(BigDecimal.valueOf(amount));
        wallet.setBalance(newBalance);
        return walletRespository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> wallet=walletRespository.findById(id);
        if (wallet.isPresent()){
            return wallet.get();
        }
        throw new Exception("Wallet not found ");
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception {
        Wallet senderWallet=getUserWallet(sender);
        if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount))<0){
            throw new Exception("Insufficient balance");
        }
        BigDecimal senderBalance=senderWallet
                .getBalance()
                .subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(senderBalance);
        walletRespository.save(senderWallet);
        BigDecimal receiverBalance=receiverWallet
                .getBalance()
                .add(BigDecimal.valueOf(amount));
        receiverWallet.setBalance(receiverBalance);
        walletRespository.save(receiverWallet);

        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        Wallet wallet=getUserWallet(user);
        if (order.getOrderType().equals(OrderType.BUY)){
            BigDecimal newBalance=wallet.getBalance().subtract(order.getPrice());
            if (newBalance.compareTo(order.getPrice())<0){
                throw new Exception("Insufficient fund for this transaction");
            }
            wallet.setBalance(newBalance);
        }
        else{
            BigDecimal newBalance=wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }
        walletRespository.save(wallet);
        return wallet;
    }
}
