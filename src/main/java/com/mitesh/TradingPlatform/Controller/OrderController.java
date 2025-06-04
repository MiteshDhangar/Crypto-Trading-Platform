package com.mitesh.TradingPlatform.Controller;

import com.mitesh.TradingPlatform.Domain.OrderType;
import com.mitesh.TradingPlatform.Model.Coin;
import com.mitesh.TradingPlatform.Model.Order;
import com.mitesh.TradingPlatform.Model.User;
import com.mitesh.TradingPlatform.Request.CreateOrderRequest;
import com.mitesh.TradingPlatform.Service.CoinService;
import com.mitesh.TradingPlatform.Service.OrderService;
import com.mitesh.TradingPlatform.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CoinService coinService;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CreateOrderRequest req
    )throws Exception{
        User user=userService.findUserProfileByJwt(jwt);
        Coin coin=coinService.findById(req.getCoinId());

        Order order=orderService.processOrder(coin,req.getQuantity(),req.getOrderType(),user);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId
    )throws Exception{
        User user=userService.findUserProfileByJwt(jwt);

        Order order=orderService.getOrderById(orderId);
        if (order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);

        }else {
            throw new Exception("You dont have access");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUser(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false) String asset_symbol
    )throws Exception{
        if (jwt==null){
            throw new Exception("Token is missing...");
        }
        Long userId=userService.findUserProfileByJwt(jwt).getId();

        List<Order> userOrders=orderService.getAllOrdersOfuser(userId,order_type,asset_symbol);
        return ResponseEntity.ok(userOrders);
    }

}
