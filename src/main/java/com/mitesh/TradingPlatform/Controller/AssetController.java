package com.mitesh.TradingPlatform.Controller;

import com.mitesh.TradingPlatform.Model.Asset;
import com.mitesh.TradingPlatform.Model.User;
import com.mitesh.TradingPlatform.Service.AssetService;
import com.mitesh.TradingPlatform.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {
    @Autowired
    private AssetService assetService;
    @Autowired
    private UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId)throws Exception{
        Asset asset=assetService.getAssetById(assetId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(
            @PathVariable String coinId,
            @RequestHeader("Authorization") String jwt
    )throws Exception{
        User user=userService.findUserProfileByJwt(jwt);

        Asset asset=assetService.findAssetByUserIdAndCoinId(user.getId(),coinId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping("/user/getasset")
    public ResponseEntity<List<Asset>> getAssetsForUser(@RequestHeader("Authorization") String jwt)throws Exception{
        User user=userService.findUserProfileByJwt(jwt);

        List<Asset> asset=assetService.getUsersAssets(user.getId());
        return ResponseEntity.ok().body(asset);
    }

}
