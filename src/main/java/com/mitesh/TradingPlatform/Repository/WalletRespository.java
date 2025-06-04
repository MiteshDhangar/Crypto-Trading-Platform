package com.mitesh.TradingPlatform.Repository;

import com.mitesh.TradingPlatform.Model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRespository extends JpaRepository<Wallet,Long> {
    Wallet findByUserId(Long userId);
}
