package com.market.onlineshop.controller;

import com.market.onlineshop.models.User;
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

        if (authenticatedUser.getRole() != null) {
            return switch (authenticatedUser.getRole()) {
                case ADMIN -> "redirect:/admin";
                case USER -> "redirect:/home";
            };
        }
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }


}
