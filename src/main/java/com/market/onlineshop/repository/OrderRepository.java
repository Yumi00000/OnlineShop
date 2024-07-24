package com.market.onlineshop.repository;

import com.market.onlineshop.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}