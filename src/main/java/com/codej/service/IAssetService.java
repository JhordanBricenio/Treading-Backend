package com.codej.service;

import com.codej.model.Asset;
import com.codej.model.Coin;
import com.codej.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public interface IAssetService {
     Asset createAsset(UserEntity user, Coin coin, double quantity);
     Asset getAssetById(Long assetId) throws Exception;
     Asset getAssetByUserIdAndId(Long userId, Long assetId);
     List<Asset> getUsersAssets(Long userId);
     Asset updatedAsset (Long assetsId, double quantity) throws Exception;
     Asset findAssetByUserIdAndCoinId(Long userId, String coinId);
     void deleteAsset(Long assetId);
}
