package com.foodmart.food.service;

import com.foodmart.food.dto.AuthResponse;
import com.foodmart.food.dto.RegisterRequest;
import com.foodmart.food.entity.Role;
import com.foodmart.food.entity.User;
import com.foodmart.food.repository.RoleRepository;
import com.foodmart.food.repository.UserRepository;
import com.foodmart.food.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        logger.info("Registering new user: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            logger.warn("Registration failed: Username already taken - {}", request.getUsername());
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Registration failed: Email already taken - {}", request.getEmail());
            throw new RuntimeException("Email already taken");
        }
        
        User u = new User();
        u.setUsername(request.getUsername());
        u.setEmail(request.getEmail());
        u.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName("ROLE_CUSTOMER").orElseGet(() -> {
            Role r = new Role("ROLE_CUSTOMER");
            return roleRepository.save(r);
        });

        u.setRoles(new HashSet<>());
        u.getRoles().add(role);

        userRepository.save(u);
        
        String token = jwtUtil.generateToken(u.getUsername());
        logger.info("User registered successfully: {}", request.getUsername());
        return new AuthResponse(token);
    }
}
