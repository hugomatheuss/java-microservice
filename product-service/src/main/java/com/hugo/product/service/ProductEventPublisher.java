package com.hugo.product.service;

import com.hugo.common.dto.ProductEvent;
import com.hugo.product.model.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductEventPublisher {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private static final String EXCHANGE = "product.exchange";
    
    public void publishProductCreated(Product product) {
        ProductEvent event = new ProductEvent(
            "CREATED",
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity()
        );
        
        rabbitTemplate.convertAndSend(EXCHANGE, "product.created", event);
        System.out.println("Published: Product Created - " + product.getName());
    }
    
    public void publishProductUpdated(Product product) {
        ProductEvent event = new ProductEvent(
            "UPDATED",
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity()
        );
        
        rabbitTemplate.convertAndSend(EXCHANGE, "product.updated", event);
        System.out.println("Published: Product Updated - " + product.getName());
    }
    
    public void publishProductDeleted(Long productId, String productName) {
        ProductEvent event = new ProductEvent(
            "DELETED",
            productId,
            productName,
            null,
            null,
            null
        );
        
        rabbitTemplate.convertAndSend(EXCHANGE, "product.deleted", event);
        System.out.println("Published: Product Deleted - " + productName);
    }
    
    public void publishStockLow(Product product) {
        ProductEvent event = new ProductEvent(
            "STOCK_LOW",
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity()
        );
        
        rabbitTemplate.convertAndSend(EXCHANGE, "product.stock.low", event);
        System.out.println("Published: Stock Low Alert - " + product.getName() + " (Stock: " + product.getStockQuantity() + ")");
    }
}