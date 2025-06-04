package com.mitesh.TradingPlatform.Repository;

import com.mitesh.TradingPlatform.Model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, String> {
}
