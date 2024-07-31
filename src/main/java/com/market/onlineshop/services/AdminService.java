package com.market.onlineshop.services;


import com.market.onlineshop.models.Order;
import com.market.onlineshop.models.User;
import com.market.onlineshop.repository.OrderRepository;
import com.market.onlineshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public AdminService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;

        this.orderRepository = orderRepository;
    }


    public Order changeOrderStatus(Order order, String status) {
        Order updatedOrder = orderRepository.findById(order.getId()).orElse(null);
        if (updatedOrder != null) {

            updatedOrder.setOrderStatus(status);
            if (status.equals("CANCELLED") || status.equals("DELIVERED")) {
                orderRepository.delete(updatedOrder);
                return null;
            }
            return orderRepository.save(updatedOrder);
        }


        return null;
    }


    public User changeRole(String username, String role) throws Exception {
        // Fetch user by username
        User user = userRepository.findByUsername(username);

        // Check if user exists
        if (user == null) {
            throw new Exception("User not found");
        }

        // Validate and set new role
        try {
            user.setRole(User.UserRole.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid role: " + role);
        }

        // Save the updated user back to the database
        return userRepository.save(user);
    }


}
