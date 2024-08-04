package com.market.onlineshop.services;

import com.market.onlineshop.models.User;
import com.market.onlineshop.models.User.UserRole;
import com.market.onlineshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER); // Default role for new users
        User savedUser = userRepository.save(user);
        System.out.println("User registered: " + savedUser);
        return savedUser;
    }

    public User processLogin(String username, String rawPassword) {
        User user = userRepository.findByUsername(username.toLowerCase());
        System.out.println("Searching for user with username: " + username);
        if (user == null) {
            System.out.println("User not found in the database.");
        } else {
            System.out.println("User found: " + user.getUsername());
        }
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {


            return user; // Successful login

        }
        return null;
    }
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        return user.getId();
    }

}
