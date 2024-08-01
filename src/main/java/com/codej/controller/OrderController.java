package com.codej.controller;

import com.codej.domain.OrderType;
import com.codej.model.Coin;
import com.codej.model.Order;
import com.codej.model.UserEntity;
import com.codej.request.CreateOrderRequest;
import com.codej.service.ICoinService;
import com.codej.service.IOrderService;
import com.codej.service.IUserService;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    private final IUserService userService;
    private final ICoinService coinService;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwt
            , @RequestBody CreateOrderRequest req) throws Exception {
        UserEntity user = userService.findUserProfileByJwt(jwt);
        Coin coin= coinService.findById(req.coinId());

        Order order= orderService.processOrder(coin, req.quantity(), req.orderType(), user);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt
            , @PathVariable Long orderId) throws Exception {

        UserEntity user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);
        if (order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        }else {
            throw  new Exception("you don't have access");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUser(@RequestHeader("Authorization") String jwt,
                                                           @RequestParam(required = false) OrderType orderType,
                                                           @RequestParam(required = false) String assetSymbol){
        Long userId= userService.findUserProfileByJwt(jwt).getId();
        List<Order> userOrders= orderService.getAllOrdersOfUser(userId, orderType, assetSymbol);
        return  ResponseEntity.ok(userOrders);
    }


}
