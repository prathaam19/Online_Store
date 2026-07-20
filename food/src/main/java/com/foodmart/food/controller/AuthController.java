package com.foodmart.food.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodmart.food.dto.AuthRequest;
import com.foodmart.food.dto.AuthResponse;
import com.foodmart.food.dto.GoogleAuthRequest;
import com.foodmart.food.dto.RegisterRequest;
import com.foodmart.food.entity.Role;
import com.foodmart.food.entity.User;
import com.foodmart.food.repository.RoleRepository;
import com.foodmart.food.repository.UserRepository;
import com.foodmart.food.service.UserService;
import com.foodmart.food.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final String googleClientId;

    public AuthController(UserService userService, UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                          @Value("${google.client-id:}") String googleClientId) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.googleClientId = googleClientId;
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
        Map<String, Object> tokenInfo = verifyGoogleToken(request.getToken());
        String email = Objects.toString(tokenInfo.get("email"), null);
        String audience = Objects.toString(tokenInfo.get("aud"), null);
        String emailVerified = Objects.toString(tokenInfo.get("email_verified"), "false");

        if (email == null || email.isBlank()) {
            throw new RuntimeException("Google token did not provide an email address.");
        }
        if (!Boolean.parseBoolean(emailVerified)) {
            throw new RuntimeException("Google email is not verified.");
        }
        if (googleClientId == null || googleClientId.isBlank()) {
            throw new RuntimeException("Google client ID is not configured for token verification.");
        }
        if (!googleClientId.equals(audience)) {
            throw new RuntimeException("Invalid Google token audience.");
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> createUserFromGoogleEmail(email));
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    private Map<String, Object> verifyGoogleToken(String idToken) {
        try {
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + URLEncoder.encode(idToken, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Google token verification failed with status " + response.statusCode());
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<>() {});
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Unable to verify Google token.", e);
        }
    }

    private User createUserFromGoogleEmail(String email) {
        String baseUsername = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        String username = baseUsername;
        int suffix = 0;
        while (userRepository.existsByUsername(username)) {
            suffix++;
            username = baseUsername + suffix;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        Role role = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_CUSTOMER")));
        user.setRoles(new HashSet<>());
        user.getRoles().add(role);
        return userRepository.save(user);
    }
}
