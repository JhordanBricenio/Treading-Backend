package com.codej.controller;


import com.codej.model.Asset;
import com.codej.model.UserEntity;
import com.codej.service.IAssetService;
import com.codej.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/asset")
public class AssetController {

    private final IAssetService assetService;

    private IUserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) throws Exception {
        Asset asset= assetService.getAssetById(assetId);
        return  ResponseEntity.ok().body(asset);

    }

    @GetMapping("/coin/{coinId}/user")
    public  ResponseEntity<Asset> getAssetByUserIdAndCoinId(@PathVariable String coinId,
                                                            @RequestHeader("Authorization") String jwt){
        UserEntity user = userService.findUserProfileByJwt(jwt);
        Asset asset= assetService.findAssetByUserIdAndCoinId(user.getId(),coinId );
        return ResponseEntity.ok().body(asset);

    }

    @GetMapping
    public  ResponseEntity<List<Asset>> getAssetsForUser(@RequestHeader("Authorization") String jwt){
        UserEntity user = userService.findUserProfileByJwt(jwt);
        List<Asset> assets= assetService.getUsersAssets(user.getId());
        return  ResponseEntity.ok().body(assets);
    }







}
