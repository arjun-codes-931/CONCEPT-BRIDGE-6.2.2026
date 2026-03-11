package com.concept.conceptbridge.controller;

import com.concept.conceptbridge.config.JwtUtil;
import com.concept.conceptbridge.entity.User;
import com.concept.conceptbridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ injected (NoOp)

    // ✅ REGISTER (PLAIN TEXT)
    @PostMapping("/register")
    public String register(@RequestBody User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists";
        }

        // ✅ No encoding logic here (NoOp handles it)
        user.setPassword(user.getPassword());
        userRepository.save(user);

        return "User registered successfully";
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User req) {

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Plain text comparison via NoOpPasswordEncoder
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());

        return Map.of(
                "token", token,
                "role", user.getRole()
        );
    }
}