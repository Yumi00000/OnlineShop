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
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;

    @Autowired
    public ProductController(ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @GetMapping("/product")
    public String productPage(Model model, @RequestParam("product-id") Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product-page";
        }
        return "error";
    }

    @PostMapping("/place-order")
    public String orderProduct(Model model, @RequestParam("product-id") Long productId) {
        Long userId = userService.getCurrentUserId();
        Optional<Product> productOpt = productRepository.findById(productId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (productOpt.isPresent() && userOpt.isPresent()) {
            Product product = productOpt.get();
            User user = userOpt.get();

            Optional<Order> orderOpt = orderRepository.findByUserId(userId);

            Order order;
            if (orderOpt.isPresent()) {
                order = orderOpt.get();
                order.setTotalPrice(order.getTotalPrice() + product.getPrice());
                order.getProducts().add(product);
            } else {
                order = new Order();
                order.setTimestamp(new Date());
                order.setOrderStatus("Order Placed");
                order.setTotalPrice(product.getPrice());
                order.setUser(user);
                order.setProducts(new ArrayList<>(List.of(product)));
            }

            // Save the order to the repository
            orderRepository.save(order);

            // Return a success view name or redirect
            return "orderSuccess";
        }

        // Handle the case where either the user or product is not found
        model.addAttribute("error", "User or Product not found");
        return "orderError";
    }


}
