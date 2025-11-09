package com.hugo.order.service;

import com.hugo.common.dto.OrderPaymentEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderEventPublisher {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private static final String EXCHANGE = "payment.exchange";
    
    public void publishOrderPayment(Long orderId, BigDecimal totalAmount) {
        OrderPaymentEvent event = new OrderPaymentEvent(
            orderId,
            totalAmount,
            "ORDER_PAYMENT_REQUEST"
        );
        
        rabbitTemplate.convertAndSend(EXCHANGE, "order.payment", event);
        System.out.println("Published order payment event for Order: " + orderId + " - Amount: R$" + totalAmount);
    }
}