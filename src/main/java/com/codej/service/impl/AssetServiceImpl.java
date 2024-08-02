package com.codej.service.impl;

import com.codej.model.Asset;
import com.codej.model.Coin;
import com.codej.model.UserEntity;
import com.codej.repository.IAssetRepository;
import com.codej.service.IAssetService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AssetServiceImpl implements IAssetService {

    private final IAssetRepository assetRepository;

    @Override
    public Asset createAsset(UserEntity user, Coin coin, double quantity) {
        Asset asset= new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setBuyPrice(coin.getCurrentPrice());

        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) throws Exception {
        return assetRepository.findById(assetId).orElseThrow(()->new Exception("asset not found"));
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {

        return null;
    }

    @Override
    public List<Asset> getUsersAssets(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updatedAsset(Long assetsId, double quantity) throws Exception {
        Asset oldAsset= getAssetById(assetsId);
        oldAsset.setQuantity(quantity+oldAsset.getQuantity());
        return assetRepository.save(oldAsset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return assetRepository.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
assetRepository.deleteById(assetId);
    }
}
