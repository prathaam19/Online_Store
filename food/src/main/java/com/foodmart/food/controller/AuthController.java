package com.foodmart.food.controller;

import com.foodmart.food.dto.AuthRequest;
import com.foodmart.food.dto.AuthResponse;
import com.foodmart.food.dto.GoogleAuthRequest;
import com.foodmart.food.dto.RegisterRequest;
import com.foodmart.food.entity.User;
import com.foodmart.food.repository.UserRepository;
import com.foodmart.food.service.UserService;
import com.foodmart.food.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse resp = userService.register(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleAuth(@RequestBody GoogleAuthRequest request) {
        // TODO: Implement Google OAuth verification
        // This endpoint should:
        // 1. Verify the Google token with Google's API
        // 2. Extract user information (email, name, etc.)
        // 3. Create or find the user in the database
        // 4. Return a JWT token
        
        // For now, return a placeholder response
        // You need to:
        // 1. Add Google OAuth Client ID and Secret to application.properties
        // 2. Add Google OAuth dependencies to pom.xml
        // 3. Implement token verification with Google's API
        // 4. Create or update user based on Google profile
        
        throw new RuntimeException("Google authentication not yet implemented. Please configure Google OAuth credentials.");
    }
}
