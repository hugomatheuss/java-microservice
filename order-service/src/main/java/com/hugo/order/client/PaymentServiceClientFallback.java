package com.hugo.order.client;

import org.springframework.stereotype.Component;

@Component
public class PaymentServiceClientFallback implements PaymentServiceClient {
    
    @Override
    public Object registrarPagamento(Object request) {
        System.err.println("Fallback: Payment service is unavailable");
        return null;
    }
}