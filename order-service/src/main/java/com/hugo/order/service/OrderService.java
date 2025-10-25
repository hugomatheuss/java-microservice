package com.hugo.order.service;

import com.hugo.order.client.ProductServiceClient;
import com.hugo.order.dto.OrderCreateDTO;
import com.hugo.order.dto.OrderResponseDTO;
import com.hugo.order.model.Order;
import com.hugo.order.repository.OrderRepository;
import com.hugo.common.dto.ProductDTO;
import com.hugo.common.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<OrderResponseDTO> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToResponseDTO);
    }
    
    public OrderResponseDTO createOrder(OrderCreateDTO createDTO) {
        for (Long productId : createDTO.getProductIds()) {
            try {
                ProductDTO product = productServiceClient.getProductById(productId);
                if (product == null) {
                    throw new RuntimeException("Product with ID " + productId + " not found");
                }
            } catch (Exception e) {
                log.error("Error validating product with ID: {}", productId, e);
                throw new RuntimeException("Error validating product with ID: " + productId);
            }
        }
        
        Order order = convertToEntity(createDTO);
        Order savedOrder = orderRepository.save(order);
        return convertToResponseDTO(savedOrder);
    }
    
    public Optional<OrderResponseDTO> updateOrderStatus(Long id, OrderStatus status) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(status);
                    Order savedOrder = orderRepository.save(existingOrder);
                    return convertToResponseDTO(savedOrder);
                });
    }
    
    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<OrderResponseDTO> getOrdersByCustomerEmail(String customerEmail) {
        return orderRepository.findByCustomerEmail(customerEmail)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    private OrderResponseDTO convertToResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getProductIds(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
    
    private Order convertToEntity(OrderCreateDTO createDTO) {
        Order order = new Order();
        order.setCustomerName(createDTO.getCustomerName());
        order.setCustomerEmail(createDTO.getCustomerEmail());
        order.setProductIds(createDTO.getProductIds());
        order.setStatus(OrderStatus.CREATED);
        return order;
    }
}