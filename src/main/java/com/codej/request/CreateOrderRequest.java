package com.codej.request;

import com.codej.domain.OrderType;

public record CreateOrderRequest(String coinId, double quantity, OrderType orderType) {
}
