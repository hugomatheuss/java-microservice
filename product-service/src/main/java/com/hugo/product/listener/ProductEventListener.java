package com.hugo.product.listener;

import com.hugo.common.dto.ProductEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {
    
    @RabbitListener(queues = "product.created.queue")
    public void handleProductCreated(ProductEvent event) {
        System.out.println("=== PRODUCT CREATED EVENT ===");
        System.out.println("Product: " + event.getProductName());
        System.out.println("ID: " + event.getProductId());
        System.out.println("Description: " + event.getDescription());
        System.out.println("Price: $" + event.getPrice());
        System.out.println("Stock: " + event.getStockQuantity());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=============================");
    }
    
    @RabbitListener(queues = "product.updated.queue")
    public void handleProductUpdated(ProductEvent event) {
        System.out.println("=== PRODUCT UPDATED EVENT ===");
        System.out.println("Product: " + event.getProductName());
        System.out.println("ID: " + event.getProductId());
        System.out.println("New Description: " + event.getDescription());
        System.out.println("New Price: $" + event.getPrice());
        System.out.println("New Stock: " + event.getStockQuantity());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=============================");
    }
    
    @RabbitListener(queues = "product.deleted.queue")
    public void handleProductDeleted(ProductEvent event) {
        System.out.println("=== PRODUCT DELETED EVENT ===");
        System.out.println("Product: " + event.getProductName());
        System.out.println("ID: " + event.getProductId());
        System.out.println("Status: REMOVED FROM CATALOG");
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=============================");
    }
    
    @RabbitListener(queues = "product.stock.low.queue")
    public void handleStockLow(ProductEvent event) {
        System.out.println("🚨🚨🚨 STOCK LOW ALERT 🚨🚨🚨");
        System.out.println("Product: " + event.getProductName());
        System.out.println("ID: " + event.getProductId());
        System.out.println("Current Stock: " + event.getStockQuantity());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("ACTION REQUIRED: Reorder stock!");
        System.out.println("================================");
    }
}