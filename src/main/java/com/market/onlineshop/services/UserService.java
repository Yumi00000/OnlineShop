package com.market.onlineshop.services;

import com.market.onlineshop.User;
import com.market.onlineshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists!");
        }
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user); // Save and return the user
        System.out.println("User registered: " + savedUser); // Add logging for verification
        return savedUser;
    }

    public User processLogin(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);

        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            boolean passwordMatch = passwordEncoder.matches(rawPassword, user.getPassword());
            System.out.println("Password match: " + passwordMatch); // Log password matching status
            if (passwordMatch) {
                return user; // Successful login
            } // Successful login
        }
        return null; // Failed login
    }

}
