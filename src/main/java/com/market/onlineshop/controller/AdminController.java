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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    @GetMapping
    public String home(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("user", new User());
        model.addAttribute("product", new Product());
        return "admin";
    }

    @GetMapping("/order")
    public String getOrder(@RequestParam(name = "id", required = false) Long id, Model model) {
        if (id != null) {
            model.addAttribute("order", orderRepository.findById(id).orElse(null));
        }
        return "redirect:/order";
    }


    @PostMapping("/change-order-status")
    public String changeOrderStatus(@RequestParam Long orderId, @RequestParam String status, Model model) {
        Order order = new Order();
        order.setId(orderId);
        Order updatedOrder = adminService.changeOrderStatus(order, status);
        model.addAttribute("order", updatedOrder);
        return "redirect:/admin";
    }

    @PostMapping("/add-product")
    public String addProduct(
            @ModelAttribute("product") @Valid Product product,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            // Add binding result to flash attributes to maintain errors after redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.product", result);
            redirectAttributes.addFlashAttribute("product", product);
            return "redirect:/admin";
        }
        try {
            productRepository.save(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
        } catch (Exception e) {
            // Handle any database exceptions here
            redirectAttributes.addFlashAttribute("addProductError", "Error adding product: " + e.getMessage());
        }
        return "redirect:/admin";
    }


    @PostMapping("/change-role")
    public String changeRole(
            @RequestParam String username,
            @RequestParam String role,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User updatedUser = adminService.changeRole(username, role);
            redirectAttributes.addFlashAttribute("successMessage", "User role updated successfully: " + updatedUser.getUsername() + " - " + updatedUser.getRole());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin";
    }


}
