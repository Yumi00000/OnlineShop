package com.market.onlineshop.controller;

import com.market.onlineshop.Order;
import com.market.onlineshop.Product;
import com.market.onlineshop.User;
import com.market.onlineshop.repository.OrderRepository;
import com.market.onlineshop.repository.ProductRepository;
import com.market.onlineshop.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public AdminController(AdminService adminService, ProductRepository productRepository, OrderRepository orderRepository) {
        this.adminService = adminService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }


    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "admin";
    }


    @PostMapping("/orders")
    public String getOrders(Model model) {
        // Ensure that the getOrders method in AdminService is adapted to accept a username
        model.addAttribute("orders", orderRepository.findAll());
        return "admin"; // Thymeleaf template to display orders
    }

    @GetMapping("/order")
    public String getOrder(@RequestParam(name = "id", required = false) Long id, Model model) {
        if (id != null) {
            model.addAttribute("order", orderRepository.findById(id).orElse(null));
        }
        return "admin";
    }

    @PostMapping("/products")
    public String getProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin"; // Thymeleaf template to display products
    }

    @PostMapping("/change-order-status")
    public String changeOrderStatus(@RequestParam Long orderId, @RequestParam String status, Model model) {
        Order order = new Order();
        order.setId(orderId);
        Order updatedOrder = adminService.changeOrderStatus(order, status);
        model.addAttribute("order", updatedOrder);
        return "admin"; // Thymeleaf template to show updated order status
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestParam String productName, @RequestParam Double productPrice, @RequestParam String productDescription, Model model) {
        Product product = new Product();
        product.setName(productName);
        product.setPrice(productPrice);
        product.setDescription(productDescription);
        Product addedProduct = adminService.addProduct(product);
        model.addAttribute("product", addedProduct);
        return "admin"; // Thymeleaf template to show added product
    }

    @PostMapping("/change-role")
    public String changeRole(@RequestParam String username, @RequestParam String role, Model model) {
        User updatedUser = adminService.changeRole(username, role);
        model.addAttribute("user", updatedUser);
        return "admin"; // Thymeleaf template to show changed user role
    }
}
