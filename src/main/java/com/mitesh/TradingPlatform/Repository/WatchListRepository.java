package com.mitesh.TradingPlatform.Repository;

import com.mitesh.TradingPlatform.Model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepository extends JpaRepository<Watchlist,Long> {
    Watchlist findByUserId(Long userId);
}
