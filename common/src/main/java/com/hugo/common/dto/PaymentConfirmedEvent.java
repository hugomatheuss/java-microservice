package com.hugo.common.dto;

import java.time.LocalDateTime;

public class PaymentConfirmedEvent {
    private Long orderId;
    private String paymentCode;
    private String eventType;
    private LocalDateTime timestamp;

    public PaymentConfirmedEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public PaymentConfirmedEvent(Long orderId, String paymentCode, String eventType) {
        this();
        this.orderId = orderId;
        this.paymentCode = paymentCode;
        this.eventType = eventType;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getPaymentCode() { return paymentCode; }
    public void setPaymentCode(String paymentCode) { this.paymentCode = paymentCode; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}