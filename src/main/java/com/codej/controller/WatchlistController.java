package com.codej.controller;

import com.codej.model.Coin;
import com.codej.model.UserEntity;
import com.codej.model.Watchlist;
import com.codej.service.ICoinService;
import com.codej.service.IUserService;
import com.codej.service.IWatchlistService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
@AllArgsConstructor
public class WatchlistController {

    private final IWatchlistService watchlistService;
    private final IUserService userService;
    private ICoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<Watchlist> getUserWatchlist(@RequestHeader("Authorization") String jwt) throws Exception {
        UserEntity user= findUserProfileByJwt( jwt);
        Watchlist watchlist= watchlistService.findUserWatchlist(user.getId());
        return  ResponseEntity.ok(watchlist);
    }

    @PostMapping("/create")
    public ResponseEntity<Watchlist> createWatchlist(@RequestHeader("Authorization") String jwt){
        UserEntity user= findUserProfileByJwt( jwt);
        Watchlist watchlist= watchlistService.createWatchlist(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(watchlist);
    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getWatchlistById(@PathVariable Long watchlistId) throws Exception {
        Watchlist watchlist= watchlistService.findById(watchlistId);
        return  ResponseEntity.ok(watchlist);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchlist(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable String coinId) throws Exception {
        UserEntity user=  findUserProfileByJwt( jwt);
        Coin coin= coinService.findById(coinId);
        Coin addedCoin= watchlistService.addItemToWatchlist(coin,user);
        return ResponseEntity.ok(addedCoin);
    }

    private UserEntity findUserProfileByJwt(String jwt){
        return userService.findUserProfileByJwt(jwt);
    }
}
