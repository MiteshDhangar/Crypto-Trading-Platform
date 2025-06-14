package com.mitesh.TradingPlatform.Model;

import com.mitesh.TradingPlatform.Domain.OrderStatus;
import com.mitesh.TradingPlatform.Domain.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @Column(nullable = false)
    private OrderType orderType;
    @Column(nullable = false)
    private BigDecimal price;

    private LocalDateTime timeStamp=LocalDateTime.now();

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "order" ,cascade = CascadeType.ALL)
    private OrderItem ordeItem;


}
