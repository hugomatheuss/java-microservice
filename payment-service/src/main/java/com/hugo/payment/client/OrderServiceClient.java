package com.hugo.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "order-service", fallback = OrderServiceClientFallback.class)
public interface OrderServiceClient {
    
    @PutMapping("/api/orders/{orderId}/confirm-payment/{paymentCode}")
    void confirmPayment(@PathVariable Long orderId, @PathVariable String paymentCode);
}