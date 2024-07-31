package com.market.onlineshop.controller;

import com.market.onlineshop.models.Order;
import com.market.onlineshop.models.Product;
import com.market.onlineshop.models.User;
import com.market.onlineshop.repository.OrderRepository;
import com.market.onlineshop.repository.ProductRepository;
import com.market.onlineshop.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public AdminController(AdminService adminService, OrderRepository orderRepository, ProductRepository productRepository) {
        this.adminService = adminService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    @GetMapping
    public String home(Model model) {
        List<Order> orders = orderRepository.findByOrderStatusNot("PLACED");
        model.addAttribute("orders", orders);
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
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            return "redirect:/admin";
        }
        Product savedProduct = productRepository.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully with ID: " + savedProduct.getId());
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
