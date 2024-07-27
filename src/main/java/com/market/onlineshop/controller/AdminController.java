package com.market.onlineshop.controller;

import com.market.onlineshop.Order;
import com.market.onlineshop.Product;
import com.market.onlineshop.User;
import com.market.onlineshop.repository.OrderRepository;
import com.market.onlineshop.repository.ProductRepository;
import com.market.onlineshop.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("product", new Product());
        return "admin";
    }

    @GetMapping("/order")
    public String getOrder(@RequestParam(name = "id", required = false) Long id, Model model) {
        if (id != null) {
            model.addAttribute("order", orderRepository.findById(id).orElse(null));
        }
        return "redirect:/admin/home";
    }


    @PostMapping("/change-order-status")
    public String changeOrderStatus(@RequestParam Long orderId, @RequestParam String status, Model model) {
        Order order = new Order();
        order.setId(orderId);
        Order updatedOrder = adminService.changeOrderStatus(order, status);
        model.addAttribute("order", updatedOrder);
        return "redirect:/admin/home";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute("product") @Valid Product product, Model model, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/admin/home";
        }
        try {
            productRepository.save(product);
        } catch (Exception e) {
            model.addAttribute("addProductError", e.getMessage());
            return "redirect:/admin/home";
        }
        return "redirect:/admin/home";
    }

    @PostMapping("/change-role")
    public String changeRole(@RequestParam String username, @RequestParam String role, Model model) {
        User updatedUser = adminService.changeRole(username, role);
        model.addAttribute("user", updatedUser);
        return "redirect:/admin/home";
    }
}
