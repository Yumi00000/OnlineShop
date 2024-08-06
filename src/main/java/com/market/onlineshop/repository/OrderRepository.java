package com.market.onlineshop.repository;

import com.market.onlineshop.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserId(Long userId);
    List<Order> findByOrderStatusNot(String status);
    Optional<Order> findByUserIdAndId(Long user_id, Long id);
    List<Order> findAllByUserIdAndOrderStatus(Long userId, String status);
}