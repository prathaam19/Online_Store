package com.foodmart.food.service;

import com.foodmart.food.dto.AuthResponse;
import com.foodmart.food.dto.RegisterRequest;
import com.foodmart.food.entity.Role;
import com.foodmart.food.entity.User;
import com.foodmart.food.repository.RoleRepository;
import com.foodmart.food.repository.UserRepository;
import com.foodmart.food.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
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
    private final String sellerInviteCode;
    private final String adminInviteCode;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                           @Value("${app.seller.invite-code:SELLER_SECRET}") String sellerInviteCode,
                           @Value("${app.admin.invite-code:ADMIN_SECRET}") String adminInviteCode) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.sellerInviteCode = sellerInviteCode;
        this.adminInviteCode = adminInviteCode;
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

        String roleRequested = request.getRole() == null ? "CUSTOMER" : request.getRole().trim().toUpperCase();
        if (roleRequested.startsWith("ROLE_")) {
            roleRequested = roleRequested.substring(5);
        }

        String roleName = "ROLE_CUSTOMER";
        if ("SELLER".equals(roleRequested)) {
            if (request.getInviteCode() == null || !request.getInviteCode().equals(sellerInviteCode)) {
                logger.warn("Invalid seller invite code for username: {}", request.getUsername());
                throw new RuntimeException("Invalid invite code for seller registration");
            }
            roleName = "ROLE_SELLER";
        } else if ("ADMIN".equals(roleRequested)) {
            if (request.getInviteCode() == null || !request.getInviteCode().equals(adminInviteCode)) {
                logger.warn("Invalid admin invite code for username: {}", request.getUsername());
                throw new RuntimeException("Invalid invite code for admin registration");
            }
            roleName = "ROLE_ADMIN";
        }

        final String selectedRoleName = roleName;
        Role role = roleRepository.findByName(selectedRoleName).orElseGet(() -> roleRepository.save(new Role(selectedRoleName)));

        u.setRoles(new HashSet<>());
        u.getRoles().add(role);

        userRepository.save(u);
        
        String token = jwtUtil.generateToken(u.getUsername());
        logger.info("User registered successfully: {} with role {}", request.getUsername(), roleName);
        return new AuthResponse(token);
    }
}
