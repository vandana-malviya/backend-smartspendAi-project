package com.expense.tracker.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expense.tracker.dto.AuthResponse;
import com.expense.tracker.model.User;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.util.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ REGISTER USER (FINAL)
    public User register(User user) {

        // duplicate check
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // 🔐 encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }

    // ✅ LOGIN USER (FINAL)
    public AuthResponse login(String username, String password) {

        Optional<User> userOpt = repository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return new AuthResponse("User not found", null);
        }

        User user = userOpt.get();

        // 🔐 match encrypted password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new AuthResponse("Invalid credentials", null);
        }

        // generate JWT token
        String token = JwtUtil.generateToken(username);

        return new AuthResponse("Login successful", token);
    }
}