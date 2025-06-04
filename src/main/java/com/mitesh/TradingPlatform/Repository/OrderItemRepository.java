package com.mitesh.TradingPlatform.Repository;

import com.mitesh.TradingPlatform.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
