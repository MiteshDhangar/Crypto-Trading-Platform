package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Domain.OrderType;
import com.mitesh.TradingPlatform.Model.Coin;
import com.mitesh.TradingPlatform.Model.Order;
import com.mitesh.TradingPlatform.Model.OrderItem;
import com.mitesh.TradingPlatform.Model.User;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, OrderItem orderItem, OrderType orderType);
    Order getOrderById(Long orderId) throws Exception;

    List<Order> getAllOrdersOfuser(Long userId ,OrderType orderType, String assetSymbol);

    Order processOrder(Coin coin,double quantity, OrderType orderType, User user) throws Exception;


}
