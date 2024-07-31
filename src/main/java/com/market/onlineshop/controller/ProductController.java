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

        // Check if both product and user exist
        if (productOpt.isEmpty() || userOpt.isEmpty()) {
            model.addAttribute("error", "User or Product not found");
            return "orderError";
        }

        Product product = productOpt.get();
        User user = userOpt.get();

        // Retrieve or create the order
        Order order = orderRepository.findByUserId(userId)
                .map(existingOrder -> updateOrder(existingOrder, product))
                .orElseGet(() -> createNewOrder(user, product));

        // Save the order
        orderRepository.save(order);

        return "orderSuccess";
    }

    private Order updateOrder(Order order, Product product) {
        if ("PLACED".equals(order.getOrderStatus())) {
            order.getProducts().add(product);
            order.setTotalPrice(order.getTotalPrice() + product.getPrice());
        } else {
            // Return a new order if the status is not "PlACED"
            return createNewOrder(order.getUser(), product);
        }
        return order; // Return the updated order
    }

    private Order createNewOrder(User user, Product product) {
        Order order = new Order();
        order.setTimestamp(new Date());
        order.setOrderStatus("PLACED");
        order.setTotalPrice(product.getPrice());
        order.setUser(user);
        order.setProducts(new ArrayList<>(List.of(product)));
        return order;
    }


}
