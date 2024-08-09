package com.codej.service;

import com.codej.model.Coin;
import com.codej.model.UserEntity;
import com.codej.model.Watchlist;

public interface IWatchlistService {
    Watchlist findUserWatchlist(Long userId) throws Exception;
    Watchlist createWatchlist(UserEntity user);
    Watchlist findById(Long id) throws Exception;

    Coin addItemToWatchlist(Coin coin, UserEntity user) throws Exception;
}
