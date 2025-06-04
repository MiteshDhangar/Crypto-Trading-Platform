package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Model.Coin;
import com.mitesh.TradingPlatform.Model.User;
import com.mitesh.TradingPlatform.Model.Watchlist;

public interface WatchListService {

    Watchlist findUserWatchList(Long userId) throws Exception;
    Watchlist createWatchList(User user);
    Watchlist findById(Long id) throws Exception;
    Coin addItemToWatchList(Coin coin,User user) throws Exception;

}
