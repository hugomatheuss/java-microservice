package com.hugo.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderPaymentEvent {
    private Long orderId;
    private BigDecimal totalAmount;
    private String eventType;
    private LocalDateTime timestamp;

    public OrderPaymentEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public OrderPaymentEvent(Long orderId, BigDecimal totalAmount, String eventType) {
        this();
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.eventType = eventType;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}