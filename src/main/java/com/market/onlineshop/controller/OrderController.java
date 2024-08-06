package com.market.onlineshop.controller;


import com.market.onlineshop.models.Order;
import com.market.onlineshop.models.Product;
import com.market.onlineshop.models.User;
import com.market.onlineshop.repository.OrderRepository;
import com.market.onlineshop.repository.ProductRepository;
import com.market.onlineshop.repository.UserRepository;
import com.market.onlineshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;



    @Autowired
    public OrderController(OrderRepository orderRepository, UserService userService, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;

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

    @PostMapping("/order")
    public String completeOrder(@RequestParam("order-id") Long orderId,
                                @RequestParam(value = "postalAddress", required = false) String postalAddress,
                                Model model) {
        Long userId = userService.getCurrentUserId();
        if (userId != null) {
            Optional<Order> orderOpt = orderRepository.findByUserIdAndId(userId, orderId);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                order.setOrderStatus("PENDING");
                orderRepository.save(order);

                if (postalAddress != null && !postalAddress.isEmpty()) {
                    User user = userRepository.findById(userId).orElse(null);
                    if (user != null) {
                        user.setPostalAddress(postalAddress);
                        userRepository.save(user);
                    }
                }

                model.addAttribute("order", order);
                return "redirect:/my-profile";
            }
        }
        return "error_page";
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
        order.getProducts().remove(Objects.requireNonNull(productRepository.findFirstById(productId)));
        orderRepository.save(order);

        if(order.getProducts().toArray().length == 0) {
            orderRepository.delete(order);
            return "redirect:/home";
        }

        return "redirect:/order?order-id=" + orderId;
    }
}

