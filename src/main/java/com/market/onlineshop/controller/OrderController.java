package com.market.onlineshop.controller;


import com.market.onlineshop.models.Order;
import com.market.onlineshop.models.Product;
import com.market.onlineshop.repository.OrderRepository;
import com.market.onlineshop.repository.ProductRepository;
import com.market.onlineshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, UserService userService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productRepository = productRepository;
    }

    @GetMapping("/order")
    public String order(@RequestParam("order-id") Long orderId, Model model, String postalAddress) {
        Long user = userService.getCurrentUserId();
        if (user != null) {
            Optional<Order> order = orderRepository.findByUserIdAndId(user, orderId);
            order.ifPresent(orderP -> model.addAttribute("order", orderP));
            return "orderSuccess";
        }
        return null;
    }

    @PostMapping("/remove-product")
    public String removeProduct(@RequestParam("product-id") Long productId,
                                @RequestParam("order-id") Long orderId,
                                Model model) {
        Long userId = userService.getCurrentUserId();

        if (userId == null) {
            model.addAttribute("error", "User not authenticated");
            return "error_page"; // Replace with the actual error page name
        }

        Optional<Order> orderOptional = orderRepository.findByUserIdAndId(userId, orderId);
        if (orderOptional.isEmpty()) {
            model.addAttribute("error", "Order not found");
            return "error_page"; // Replace with the actual error page name
        }

        Order order = orderOptional.get();
        Product productPrice = productRepository.findById(productId).orElse(null);
        assert productPrice != null;
        order.setTotalPrice(order.getTotalPrice() -  productPrice.getPrice());
        boolean productRemoved = order.getProducts().removeIf(product -> product.getId().equals(productId));

        if (!productRemoved) {
            model.addAttribute("error", "Product not found in the order");
            return "error_page"; // Replace with the actual error page name
        }

        // Save the updated order
        orderRepository.save(order);

        // Redirect to the updated order details page
        return "redirect:/order?order-id=" + orderId;
    }
}

