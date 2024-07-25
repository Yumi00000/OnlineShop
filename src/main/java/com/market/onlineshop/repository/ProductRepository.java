package com.market.onlineshop.repository;

import com.market.onlineshop.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


}
