package com.codej.service.impl;

import com.codej.model.Coin;
import com.codej.model.UserEntity;
import com.codej.model.Watchlist;
import com.codej.repository.IWatchlistRepository;
import com.codej.service.IWatchlistService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WatchlistServiceImpl implements IWatchlistService {

    private final IWatchlistRepository watchlistRepository;

    @Override
    public Watchlist findUserWatchlist(Long userId) throws Exception {
        Watchlist watchlist= watchlistRepository.findByUserId(userId);
        if(watchlist==null){
            throw new Exception("Watchlist not found");
        }
        return watchlist;
    }

    @Override
    public Watchlist createWatchlist(UserEntity user) {
        Watchlist watchlist= new Watchlist();
        watchlist.setUser(user);
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist findById(Long id) throws Exception {
        Optional<Watchlist> watchlistlistOptional= watchlistRepository.findById(id);
        if (watchlistlistOptional.isEmpty()){
            throw new Exception("watchlist not found");
        }
        return watchlistlistOptional.get();
    }

    @Override
    public Coin addItemToWatchlist(Coin coin, UserEntity user) throws Exception {
        Watchlist watchlist= findUserWatchlist(user.getId());
        if(watchlist.getCoins().contains(coin)){
            watchlist.getCoins().remove(coin);
        }
        else watchlist.getCoins().add(coin);
        watchlistRepository.save(watchlist);
        return coin;
    }
}
