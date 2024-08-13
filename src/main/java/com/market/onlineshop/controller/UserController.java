package com.market.onlineshop.controller;

import com.market.onlineshop.dto.UserDTO;
import com.market.onlineshop.dto.OrderDTO;
import com.market.onlineshop.models.Order;
import com.market.onlineshop.models.User;
import com.market.onlineshop.repository.OrderRepository;
import com.market.onlineshop.repository.UserRepository;
import com.market.onlineshop.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(OrderRepository orderRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/my-profile")
    public String user_profile(Model model) {
        logger.info("Fetching user profile");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user != null) {
            logger.debug("User found: {}", user);
            List<Order> orders = orderRepository.findAllByUserIdAndOrderStatus(user.getId(), "PLACED");

            // Convert User to UserDTO
            UserDTO userDTO = DtoConverter.convertToDto(user);

            // Convert List of Orders to List of OrderDTOs
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(DtoConverter::convertToDto)
                    .collect(Collectors.toList());

            model.addAttribute("orders", orderDTOs);
            model.addAttribute("user_data", userDTO);
            return "user_page";
        }

        logger.warn("User not found: {}", username);
        return "error_page";
    }

    @RequestMapping(value = "/edit-userdata", method = RequestMethod.GET)
    public String editUserData(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user != null) {

            model.addAttribute("user_data", user);
            return "edit_user";
        }
        return "error_page";
    }

    @RequestMapping(value = "/edit-userdata", method = RequestMethod.POST)
    public String saveUserData(@ModelAttribute("user_data") User formUser, Model model) {
        try {
            // Retrieve the authenticated user's username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Retrieve the existing user from the database
            User existingUser = userRepository.findByUsername(username);

            if (existingUser == null) {
                logger.warn("User not found: {}", username);
                return "error_page";
            }

            // Update the existing user's fields with the data from the form
            existingUser.setFirstName(formUser.getFirstName());
            existingUser.setLastName(formUser.getLastName());
            existingUser.setEmail(formUser.getEmail());
            existingUser.setPostalAddress(formUser.getPostalAddress());
            existingUser.setPhone(formUser.getPhone());
            if(formUser.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(formUser.getPassword()));
            }

            // Save the updated user entity
            userRepository.saveAndFlush(existingUser);

            return "redirect:/my-profile";
        } catch (Exception e) {
            logger.warn("Error saving user data: {}", formUser.getUsername(), e);
            return "error_page";
        }
    }


}
