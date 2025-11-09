package com.hugo.common.dto;

import java.math.BigDecimal;

public class PaymentRequestDTO {
    private Long orderId;
    private BigDecimal amount;

    public PaymentRequestDTO() {}

    public PaymentRequestDTO(Long orderId, BigDecimal amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}