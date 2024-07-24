package com.market.onlineshop.controller;

import com.market.onlineshop.User;
import com.market.onlineshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "login"; // Return to login page if there are validation errors
        }

        User authenticatedUser = userService.processLogin(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {
            return "redirect:/"; // Redirect to home page or dashboard after successful login
        } else {
            model.addAttribute("error", "Invalid username or password"); // Add error message if login fails
            return "login"; // Return to login page
        }
    }


}
