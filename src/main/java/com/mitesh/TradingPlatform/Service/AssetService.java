package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Model.Asset;
import com.mitesh.TradingPlatform.Model.Coin;
import com.mitesh.TradingPlatform.Model.User;

import java.util.List;

public interface AssetService {
    Asset createAsset(User user, Coin coin , double quantity);

    Asset getAssetById(Long assetId) throws Exception;

    Asset getAssetByUserIdAndCoinId(Long userId,Long assetId);

    List<Asset> getUsersAssets(Long userId);

    Asset updateAsset(Long assetId, double quantity) throws Exception;

    Asset findAssetByUserIdAndCoinId(Long userId,String coinId);

    void deleteAsset(Long assetId);

}
