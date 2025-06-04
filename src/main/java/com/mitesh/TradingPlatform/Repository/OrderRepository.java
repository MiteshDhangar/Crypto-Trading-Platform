package com.mitesh.TradingPlatform.Repository;

import com.mitesh.TradingPlatform.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
}
