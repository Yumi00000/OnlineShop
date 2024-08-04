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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserController( OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/my-profile")
    public String user_profile(Model model) {
        logger.info("Fetching user profile");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user != null) {
            logger.debug("User found: {}", user);
            List<Order> orders = orderRepository.findAllByUserId(user.getId());

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
}
