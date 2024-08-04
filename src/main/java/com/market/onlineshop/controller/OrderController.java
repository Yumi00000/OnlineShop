package com.market.onlineshop.controller;


import com.market.onlineshop.models.Order;
import com.market.onlineshop.repository.OrderRepository;
import com.market.onlineshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserService userService;


    @Autowired
    public OrderController(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @GetMapping("/order")
    public String order(@RequestParam("order-id") Long orderId, Model model) {
        Long user = userService.getCurrentUserId();
        if (user != null) {
            Optional<Order> order = orderRepository.findByUserIdAndId(user, orderId);
            order.ifPresent(orderP -> model.addAttribute("order", orderP));
            return "orderSuccess";
        }
        return null;
    }

}
