package com.mitesh.TradingPlatform.Controller;

import com.mitesh.TradingPlatform.Model.Coin;
import com.mitesh.TradingPlatform.Model.User;
import com.mitesh.TradingPlatform.Model.Watchlist;
import com.mitesh.TradingPlatform.Service.CoinService;
import com.mitesh.TradingPlatform.Service.UserService;
import com.mitesh.TradingPlatform.Service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {

    @Autowired
    private WatchListService watchListService;
    @Autowired
    private UserService userService;
    @Autowired
    private CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<Watchlist> getUserWatchList(
            @RequestHeader("Authorization") String jwt
    )throws Exception{
        User user=userService.findUserProfileByJwt(jwt);
        Watchlist watchlist=watchListService.findUserWatchList(user.getId());
        return ResponseEntity.ok(watchlist);
    }

    @GetMapping("/{watchListId}")
    public ResponseEntity<Watchlist> getWatchListById(
            @PathVariable Long watchListId
    )throws Exception{

        Watchlist watchlist=watchListService.findById(watchListId);
        return ResponseEntity.ok(watchlist);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchList(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId
    )throws Exception{

        User user=userService.findUserProfileByJwt(jwt);
        Coin coin=coinService.findById(coinId);
        Coin addedcoin=watchListService.addItemToWatchList(coin,user);
        return ResponseEntity.ok(addedcoin);
    }
}
