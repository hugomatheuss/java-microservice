package com.hugo.product.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange("product.exchange");
    }
    
    @Bean
    public Queue productCreatedQueue() {
        return QueueBuilder.durable("product.created.queue").build();
    }
    
    @Bean
    public Queue productUpdatedQueue() {
        return QueueBuilder.durable("product.updated.queue").build();
    }
    
    @Bean
    public Queue productDeletedQueue() {
        return QueueBuilder.durable("product.deleted.queue").build();
    }
    
    @Bean
    public Queue productStockLowQueue() {
        return QueueBuilder.durable("product.stock.low.queue").build();
    }
    
    @Bean
    public Binding productCreatedBinding() {
        return BindingBuilder
                .bind(productCreatedQueue())
                .to(productExchange())
                .with("product.created");
    }
    
    @Bean
    public Binding productUpdatedBinding() {
        return BindingBuilder
                .bind(productUpdatedQueue())
                .to(productExchange())
                .with("product.updated");
    }
    
    @Bean
    public Binding productDeletedBinding() {
        return BindingBuilder
                .bind(productDeletedQueue())
                .to(productExchange())
                .with("product.deleted");
    }
    
    @Bean
    public Binding productStockLowBinding() {
        return BindingBuilder
                .bind(productStockLowQueue())
                .to(productExchange())
                .with("product.stock.low");
    }
}