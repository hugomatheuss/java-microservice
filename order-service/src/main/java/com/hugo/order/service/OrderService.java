package com.hugo.order.service;

import com.hugo.order.client.PaymentServiceClient;
import com.hugo.order.client.ProductServiceClient;
import com.hugo.order.dto.OrderCreateDTO;
import com.hugo.order.dto.OrderItemDTO;
import com.hugo.order.dto.OrderResponseDTO;
import com.hugo.order.model.Order;
import com.hugo.order.repository.OrderRepository;
import com.hugo.common.dto.ProductDTO;
import com.hugo.common.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductServiceClient productServiceClient;

    @Autowired
    private PaymentServiceClient paymentServiceClient;

    @Autowired
    private OrderEventPublisher eventPublisher;
    
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToResponseDTO(order);
    }
    
    public OrderResponseDTO createOrder(OrderCreateDTO createDTO) {
        System.out.println("🛒 Creating new order...");

        if (createDTO.getItems() == null || createDTO.getItems().isEmpty()) {
            throw new RuntimeException("Order must have at least one item");
        }
        
        OrderItemDTO firstItem = createDTO.getItems().get(0);
        
        ProductDTO product = productServiceClient.getProductById(firstItem.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found: " + firstItem.getProductId());
        }

        if (product.getStockQuantity() < firstItem.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName() + 
                                     " (Available: " + product.getStockQuantity() + 
                                     ", Requested: " + firstItem.getQuantity() + ")");
        }

        Order order = new Order(
            firstItem.getProductId(),
            product.getName(),
            firstItem.getQuantity(),
            product.getPrice(),
            createDTO.getCustomerName(),
            createDTO.getCustomerEmail()
        );
        
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);
        System.out.println("✅ Order created: " + savedOrder.getId() + 
                          " - Product: " + savedOrder.getProductName() + 
                          " - Total: R$" + savedOrder.getTotalPrice());

        eventPublisher.publishOrderPayment(savedOrder.getId(), savedOrder.getTotalPrice());

        return convertToResponseDTO(savedOrder);
    }
    
    public Optional<OrderResponseDTO> updateOrderStatus(Long id, OrderStatus status) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(status);
                    Order savedOrder = orderRepository.save(existingOrder);
                    System.out.println("Order " + id + " status updated to: " + status);
                    return convertToResponseDTO(savedOrder);
                });
    }
    
    public OrderResponseDTO updateOrder(Long id, OrderCreateDTO updateDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Cannot update order with status: " + order.getStatus());
        }

        order.setCustomerName(updateDTO.getCustomerName());
        
        Order updatedOrder = orderRepository.save(order);
        return convertToResponseDTO(updatedOrder);
    }
    
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.SHIPPED) {
            throw new RuntimeException("Cannot delete order with status: " + order.getStatus());
        }

        orderRepository.delete(order);
    }

    public void confirmPayment(Long orderId, String paymentCode) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.PAID);
        
        orderRepository.save(order);
        System.out.println("💳 Payment confirmed for Order: " + orderId + " - Payment Code: " + paymentCode);
    }

    public void updateProductStock(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        System.out.println("Updating product stock for Order: " + orderId);

        try {
            System.out.println("Should update stock for Product: " + order.getProductId() + 
                             " - Decrease by: " + order.getQuantity());            
        } catch (Exception e) {
            System.err.println("Failed to update stock for Product: " + order.getProductId() + " - " + e.getMessage());
        }
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
    
    public List<OrderResponseDTO> getOrdersByProduct(Long productId) {
        return orderRepository.findByProductId(productId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderResponseDTO> searchOrdersByCustomerName(String customerName) {
        return orderRepository.findByCustomerNameContainingIgnoreCase(customerName)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    private OrderResponseDTO convertToResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setProductId(order.getProductId());
        dto.setProductName(order.getProductName());
        dto.setQuantity(order.getQuantity());
        dto.setUnitPrice(order.getUnitPrice());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        dto.setLastModified(order.getLastModified());
        
        return dto;
    }
}