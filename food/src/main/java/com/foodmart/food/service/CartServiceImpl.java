package com.foodmart.food.service;

import com.foodmart.food.dto.CartResponse;
import com.foodmart.food.dto.ProductResponse;
import com.foodmart.food.entity.Cart;
import com.foodmart.food.entity.Product;
import com.foodmart.food.entity.User;
import com.foodmart.food.exception.ResourceNotFoundException;
import com.foodmart.food.repository.CartRepository;
import com.foodmart.food.repository.ProductRepository;
import com.foodmart.food.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(String username) {
        logger.info("Fetching cart for user: {}", username);
        Cart cart = findOrCreateCart(username);
        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addProductToCart(String username, Long productId) {
        logger.info("Adding product {} to cart for user: {}", productId, username);
        Cart cart = findOrCreateCart(username);
        Long safeProductId = Objects.requireNonNull(productId, "Product id is required");
        Product product = productRepository.findById(safeProductId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + safeProductId));
        cart.getProducts().add(product);
        cartRepository.save(cart);
        logger.info("Product added to cart successfully");
        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeProductFromCart(String username, Long productId) {
        logger.info("Removing product {} from cart for user: {}", productId, username);
        Cart cart = findOrCreateCart(username);
        cart.getProducts().removeIf(product -> product.getId().equals(productId));
        cartRepository.save(cart);
        logger.info("Product removed from cart successfully");
        return toResponse(cart);
    }

    private Cart findOrCreateCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return cartRepository.findByUserUsername(username)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    user.setCart(cart);
                    return cartRepository.save(cart);
                });
    }

    private CartResponse toResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUsername(cart.getUser().getUsername());
        List<ProductResponse> products = cart.getProducts().stream().map(product -> {
            ProductResponse resp = new ProductResponse();
            resp.setId(product.getId());
            resp.setName(product.getName());
            resp.setPrice(product.getPrice());
            resp.setQuantity(product.getQuantity());
            resp.setImageFilename(product.getImageFilename());
            if (product.getCategory() != null) {
                resp.setCategory(new com.foodmart.food.dto.CategoryDTO(product.getCategory().getName()));
                resp.getCategory().setId(product.getCategory().getId());
            }
            return resp;
        }).collect(Collectors.toList());
        response.setProducts(products);
        return response;
    }
}
