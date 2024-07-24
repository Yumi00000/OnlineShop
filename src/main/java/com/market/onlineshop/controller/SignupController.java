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
public class SignupController {

    private final UserService userService;

    @Autowired
    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup"; // Refers to signup.html template
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "signup"; // Return to signup.html if there are validation errors
        }

        try {
            userService.signUp(user); // Call the signUp method
        } catch (Exception e) {
            model.addAttribute("registrationError", e.getMessage());
            return "signup"; // Return to signup.html if there is an exception
        }

        return "redirect:/login"; // Redirect to login page after successful registration
    }
}
