package com.foodmart.food;

import com.foodmart.food.dto.CartResponse;
import com.foodmart.food.entity.Product;
import com.foodmart.food.entity.Role;
import com.foodmart.food.entity.User;
import com.foodmart.food.repository.ProductRepository;
import com.foodmart.food.repository.RoleRepository;
import com.foodmart.food.repository.UserRepository;
import com.foodmart.food.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        Role role = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_CUSTOMER")));
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);

        product = new Product();
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setQuantity(5);
        Objects.requireNonNull(product, "Product entity must not be null");
        Product savedProduct = Objects.requireNonNull(productRepository.save(product), "Saved product must not be null");
        product = savedProduct;
    }

    @Test
    void shouldAddProductToCart() {
        CartResponse cart = cartService.addProductToCart(user.getUsername(), product.getId());
        assertEquals(user.getUsername(), cart.getUsername());
        assertEquals(1, cart.getProducts().size());
        assertEquals(product.getName(), cart.getProducts().get(0).getName());
    }
}
