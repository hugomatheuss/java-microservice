package com.hugo.order.service;

import com.hugo.order.client.ProductServiceClient;
import com.hugo.order.dto.OrderCreateDTO;
import com.hugo.order.dto.OrderResponseDTO;
import com.hugo.order.model.Order;
import com.hugo.order.repository.OrderRepository;
import com.hugo.common.dto.ProductDTO;
import com.hugo.common.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    
    @Autowired
    public OrderService(OrderRepository orderRepository, ProductServiceClient productServiceClient) {
        this.orderRepository = orderRepository;
        this.productServiceClient = productServiceClient;
    }
    
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
        // Validar produto através do Product Service
        try {
            ProductDTO product = productServiceClient.getProductById(createDTO.getProductId());
            if (product == null) {
                throw new RuntimeException("Product with ID " + createDTO.getProductId() + " not found");
            }
            
            // Verificar se há estoque suficiente
            if (product.getStockQuantity() < createDTO.getQuantity()) {
                throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity() + 
                                         ", Requested: " + createDTO.getQuantity());
            }
            
            // Criar o pedido
            Order order = new Order(
                product.getId(),
                product.getName(),
                createDTO.getQuantity(),
                product.getPrice(),
                createDTO.getCustomerName(),
                createDTO.getCustomerEmail()
            );
            
            Order savedOrder = orderRepository.save(order);
            System.out.println("Order created successfully: " + savedOrder.getId());
            
            return convertToResponseDTO(savedOrder);
            
        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
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
    
    public Optional<OrderResponseDTO> updateOrder(Long id, OrderCreateDTO updateDTO) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    // Validar novo produto se alterado
                    if (!existingOrder.getProductId().equals(updateDTO.getProductId())) {
                        ProductDTO product = productServiceClient.getProductById(updateDTO.getProductId());
                        if (product == null) {
                            throw new RuntimeException("Product with ID " + updateDTO.getProductId() + " not found");
                        }
                        existingOrder.setProductId(product.getId());
                        existingOrder.setProductName(product.getName());
                        existingOrder.setUnitPrice(product.getPrice());
                    }
                    
                    existingOrder.setQuantity(updateDTO.getQuantity());
                    existingOrder.setCustomerName(updateDTO.getCustomerName());
                    existingOrder.setCustomerEmail(updateDTO.getCustomerEmail());
                    
                    Order savedOrder = orderRepository.save(existingOrder);
                    return convertToResponseDTO(savedOrder);
                });
    }
    
    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            System.out.println("Order deleted: " + id);
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
        return new OrderResponseDTO(
                order.getId(),
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getUnitPrice(),
                order.getTotalPrice(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getStatus(),
                order.getOrderDate(),
                order.getLastModified()
        );
    }
}