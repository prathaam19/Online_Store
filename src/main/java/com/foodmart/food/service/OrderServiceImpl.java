package com.foodmart.food.service;

import com.foodmart.food.dto.AddressDTO;
import com.foodmart.food.dto.OrderItemDTO;
import com.foodmart.food.dto.OrderRequest;
import com.foodmart.food.dto.OrderResponse;
import com.foodmart.food.dto.ProductResponse;
import com.foodmart.food.service.EmailService;
import com.foodmart.food.entity.Address;
import com.foodmart.food.entity.Order;
import com.foodmart.food.entity.OrderItem;
import com.foodmart.food.entity.OrderStatus;
import com.foodmart.food.entity.Product;
import com.foodmart.food.entity.User;
import com.foodmart.food.exception.ResourceNotFoundException;
import com.foodmart.food.repository.CartRepository;
import com.foodmart.food.repository.OrderRepository;
import com.foodmart.food.repository.ProductRepository;
import com.foodmart.food.repository.UserRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final EmailService emailService;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository,
                          CartRepository cartRepository, ObjectProvider<EmailService> emailServiceProvider) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.emailService = emailServiceProvider.getIfAvailable();
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(String username, OrderRequest request) {
        logger.info("Placing order for user: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        Address address = new Address();
        AddressDTO dto = request.getShippingAddress();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        order.setShippingAddress(address);

        for (OrderItemDTO itemDTO : request.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemDTO.getProductId()));
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(BigDecimal.valueOf(product.getPrice()));
            order.addOrderItem(item);
        }

        Order saved = orderRepository.save(order);
        
        // Clear user's cart after successful order placement
        user.getCart().getProducts().clear();
        cartRepository.save(user.getCart());

        if (emailService != null) {
            emailService.sendOrderConfirmationEmail(user.getEmail(), user.getUsername(), saved.getId(), saved.getTotal().toString());
        }
        
        logger.info("Order placed successfully with ID: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(String username) {
        logger.info("Fetching orders for user: {}", username);
        return orderRepository.findByUserUsername(username).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setTotal(order.getTotal());
        response.setStatus(order.getStatus().name());

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(order.getShippingAddress().getStreet());
        addressDTO.setCity(order.getShippingAddress().getCity());
        addressDTO.setState(order.getShippingAddress().getState());
        addressDTO.setPostalCode(order.getShippingAddress().getPostalCode());
        addressDTO.setCountry(order.getShippingAddress().getCountry());
        response.setShippingAddress(addressDTO);

        response.setItems(order.getOrderItems().stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setQuantity(item.getQuantity());
            return itemDTO;
        }).collect(Collectors.toList()));

        return response;
    }
}
