package com.codej.service;

import com.codej.domain.OrderType;
import com.codej.model.Coin;
import com.codej.model.Order;
import com.codej.model.OrderItem;
import com.codej.model.UserEntity;

import java.util.List;

public interface IOrderService {
    Order createOrder(UserEntity user, OrderItem orderItem, OrderType orderType);
    Order getOrderById(Long orderId) throws Exception;
    List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol);
    Order processOrder(Coin coin, double quantity, OrderType orderType, UserEntity user) throws Exception;

}
