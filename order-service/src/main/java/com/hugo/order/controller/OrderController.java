package com.hugo.order.controller;

import com.hugo.order.dto.OrderCreateDTO;
import com.hugo.order.dto.OrderResponseDTO;
import com.hugo.order.service.OrderService;
import com.hugo.common.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;
    
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        Optional<OrderResponseDTO> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderCreateDTO createDTO) {
        try {
            OrderResponseDTO createdOrder = orderService.createOrder(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable Long id, 
            @RequestBody OrderCreateDTO updateDTO) {
        try {
            Optional<OrderResponseDTO> updatedOrder = orderService.updateOrder(id, updateDTO);
            return updatedOrder.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Error updating order: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam OrderStatus status) {
        Optional<OrderResponseDTO> updatedOrder = orderService.updateOrderStatus(id, status);
        return updatedOrder.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/customer")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomerEmail(@RequestParam String email) {
        List<OrderResponseDTO> orders = orderService.getOrdersByCustomerEmail(email);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/status")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(@RequestParam OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByProduct(@PathVariable Long productId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByProduct(productId);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<OrderResponseDTO>> searchOrdersByCustomerName(@RequestParam String customerName) {
        List<OrderResponseDTO> orders = orderService.searchOrdersByCustomerName(customerName);
        return ResponseEntity.ok(orders);
    }
}