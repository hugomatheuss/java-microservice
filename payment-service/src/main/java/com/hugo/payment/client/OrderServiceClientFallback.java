package com.hugo.payment.client;

import org.springframework.stereotype.Component;

@Component
public class OrderServiceClientFallback implements OrderServiceClient {
    
    @Override
    public void confirmPayment(Long orderId, String paymentCode) {
        System.err.println("Fallback: Failed to confirm payment for Order: " + orderId);
    }
}