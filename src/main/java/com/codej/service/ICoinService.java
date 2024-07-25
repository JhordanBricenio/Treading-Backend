package com.codej.service;

import com.codej.model.Coin;

import java.util.List;

public interface ICoinService {
    List<Coin> getCoinList(int page) throws Exception;
    String getMarketChart(String coinId, int days) throws Exception;
    String getCoinDetails(String coinId) throws Exception;
    Coin findById(String coinId);
    String searchCoin(String keyword) throws Exception;
    String getTop50CoinsByMarketsCapRank() throws Exception;
    String getTreadingCoins() throws Exception;
}
