package com.mitesh.TradingPlatform.Request;

import com.mitesh.TradingPlatform.Domain.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}
