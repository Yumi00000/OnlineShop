package com.market.onlineshop.services;


import com.market.onlineshop.Product;
import com.market.onlineshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HomePageService {

    private final ProductRepository productRepository;

    @Autowired
    public HomePageService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }


    public Optional<Product> getProductById(Long productId) {

        return productRepository.findById(productId);
    }

}
