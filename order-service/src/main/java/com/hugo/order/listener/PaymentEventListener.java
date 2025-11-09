package com.hugo.order.listener;

import com.hugo.common.dto.PaymentConfirmedEvent;
import com.hugo.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = "payment.confirmed.queue")
    public void handlePaymentConfirmed(PaymentConfirmedEvent event) {
        System.out.println("Received payment confirmation for Order: " + event.getOrderId() + 
                          " - Payment Code: " + event.getPaymentCode());

        try {            
            orderService.confirmPayment(event.getOrderId(), event.getPaymentCode());
            
            orderService.updateProductStock(event.getOrderId());
            
            System.out.println("Order updated and stock synchronized for Order: " + event.getOrderId());

        } catch (Exception e) {
            System.err.println("Error processing payment confirmation for Order: " + event.getOrderId() + " - " + e.getMessage());
        }
    }
}