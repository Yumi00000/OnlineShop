package com.market.onlineshop.services;


import com.market.onlineshop.Order;
import com.market.onlineshop.Product;
import com.market.onlineshop.User;
import com.market.onlineshop.repository.OrderRepository;
import com.market.onlineshop.repository.ProductRepository;
import com.market.onlineshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public AdminService(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }


    public Order changeOrderStatus(Order order, String status) {
        Order updatedOrder = orderRepository.findById(order.getId()).orElse(null);
        if (updatedOrder != null) {
            updatedOrder.setOrderStatus(status);
            return orderRepository.save(updatedOrder);
        }

        return null;
    }

    public Product addProduct(Product product) {
        productRepository.save(product);
        return product;
    }

    public User changeRole(String username, String role) {
        User user = userRepository.findByUsername(username);
        user.setRole(User.UserRole.valueOf(role.toUpperCase()));
        return user;
    }


}
