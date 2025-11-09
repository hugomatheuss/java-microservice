package com.hugo.payment.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentRabbitConfig {
    
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange("payment.exchange");
    }
    
    @Bean
    public Queue orderPaymentQueue() {
        return QueueBuilder.durable("order.payment.queue").build();
    }
    
    @Bean
    public Queue paymentConfirmedQueue() {
        return QueueBuilder.durable("payment.confirmed.queue").build();
    }
    
    @Bean
    public Binding orderPaymentBinding() {
        return BindingBuilder
                .bind(orderPaymentQueue())
                .to(paymentExchange())
                .with("order.payment");
    }
    
    @Bean
    public Binding paymentConfirmedBinding() {
        return BindingBuilder
                .bind(paymentConfirmedQueue())
                .to(paymentExchange())
                .with("payment.confirmed");
    }
}