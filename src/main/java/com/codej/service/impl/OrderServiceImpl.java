package com.codej.service.impl;

import com.codej.domain.OrderStatus;
import com.codej.domain.OrderType;
import com.codej.model.*;
import com.codej.repository.IOrderItemRepository;
import com.codej.repository.IOrderRepository;
import com.codej.service.IAssetService;
import com.codej.service.IOrderService;
import com.codej.service.IWalletService;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService
{
    private final IOrderRepository orderRepository;
    private final IWalletService walletService;
    private  final IOrderItemRepository orderItemRepository;
    private final IAssetService assetService;

    @Override
    public Order createOrder(UserEntity user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();
        Order order= new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(()->new Exception("order not found"));
    }

    @Override
    public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return orderRepository.findByUserId(userId);
    }

    private OrderItem createOrderItem(Coin coin, double quantity, double buyPrice, double sellPrice){
        OrderItem orderItem=  new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return  orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, UserEntity user) throws Exception {
        if(quantity<=0){
            throw  new Exception("quantity should be > 0");
        }
        double buyPrice= coin.getCurrentPrice();
        OrderItem orderItem= createOrderItem(coin, quantity, buyPrice, 0);
        Order order = createOrder(user, orderItem, OrderType.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(order, user);

        order.setStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder=orderRepository.save(order);

        //Create asset
        Asset assetOld= assetService.findAssetByUserIdAndCoinId(
                order.getUser().getId(),
                order.getOrderItem().getCoin().getId());

        if (assetOld==null){
            assetService.createAsset(user, orderItem.getCoin(), orderItem.getQuantity());
        }else {
            assetService.updatedAsset(assetOld.getId(), quantity);
        }
        return  savedOrder;
    }

    @Transactional
    public Order sellAsset(Coin coin, double quantity, UserEntity user) throws Exception {
        if(quantity<=0){
            throw  new Exception("quantity should be > 0");
        }
        double sellPrice= coin.getCurrentPrice();
        Asset assetToSell= assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());
        if (assetToSell!=null) {

            double buyPrice = assetToSell.getBuyPrice();

            OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);
            Order order = createOrder(user, orderItem, OrderType.SELL);
            orderItem.setOrder(order);

            if (assetToSell.getQuantity() > quantity) {
                order.setStatus(OrderStatus.SUCCESS);
                order.setOrderType(OrderType.SELL);
                Order savedOrder = orderRepository.save(order);

                walletService.payOrderPayment(order, user);
                Asset updatedAsset = assetService.updatedAsset(assetToSell.getId(), -quantity);
                if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
                    assetService.deleteAsset(updatedAsset.getId());
                }
                return savedOrder;
            }

            throw new Exception("Insufficient quantity to sell");
        }
        throw new Exception("asset not found");
    }

    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, UserEntity user) throws Exception {
        if(orderType.equals(OrderType.BUY)){
            return buyAsset(coin, quantity, user);
        }else if (orderType.equals(OrderType.SELL)){
            return sellAsset(coin, quantity, user);
        }
        throw new Exception("Invalid order type");
    }
}
