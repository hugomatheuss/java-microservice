package com.hugo.payment.listener;

import com.hugo.common.dto.OrderPaymentEvent;
import com.hugo.common.dto.PaymentConfirmedEvent;
import com.hugo.payment.dto.PaymentRequestDTO;
import com.hugo.payment.dto.PaymentResponseDTO;
import com.hugo.payment.service.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "order.payment.queue")
    public void handleOrderPayment(OrderPaymentEvent event) {
        System.out.println("🔔 Received payment request for Order: " + event.getOrderId() + 
                          " - Amount: R$" + event.getTotalAmount());

        try {
            PaymentRequestDTO request = new PaymentRequestDTO(
                event.getOrderId(),
                event.getTotalAmount()
            );

            PaymentResponseDTO payment = paymentService.registrarPagamento(request);

            PaymentConfirmedEvent confirmedEvent = new PaymentConfirmedEvent(
                event.getOrderId(),
                payment.getPaymentCode(),
                "PAYMENT_CONFIRMED"
            );

            rabbitTemplate.convertAndSend("payment.exchange", "payment.confirmed", confirmedEvent);
            
            System.out.println("✅ Payment confirmed and event sent for Order: " + event.getOrderId());

        } catch (Exception e) {
            System.err.println("❌ Error processing payment for Order: " + event.getOrderId() + " - " + e.getMessage());
        }
    }
}