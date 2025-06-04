package com.mitesh.TradingPlatform.Model;

import com.mitesh.TradingPlatform.Domain.PaymentMethod;
import com.mitesh.TradingPlatform.Domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long amount;
    private PaymentOrderStatus status;
    private PaymentMethod paymentMethod;
    @ManyToOne
    private User user;

}
