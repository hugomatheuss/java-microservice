package com.hugo.order.repository;

import com.hugo.order.model.Order;
import com.hugo.common.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByCustomerEmail(String customerEmail);
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByProductId(Long productId);
    
    List<Order> findByCustomerNameContainingIgnoreCase(String customerName);
    
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
    
    List<Order> findByCustomerEmailAndStatus(String customerEmail, OrderStatus status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.productId = :productId AND o.status = 'CONFIRMED'")
    Long countConfirmedOrdersByProductId(@Param("productId") Long productId);
}