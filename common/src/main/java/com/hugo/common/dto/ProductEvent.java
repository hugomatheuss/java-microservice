package com.hugo.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductEvent {
    private String eventType;
    private Long productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private LocalDateTime timestamp;
    
    public ProductEvent() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ProductEvent(String eventType, Long productId, String productName, 
                       String description, BigDecimal price, Integer stockQuantity) {
        this();
        this.eventType = eventType;
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
    
    public String getEventType() { 
        return eventType; 
    }
    
    public void setEventType(String eventType) { 
        this.eventType = eventType; 
    }
    
    public Long getProductId() { 
        return productId; 
    }
    
    public void setProductId(Long productId) { 
        this.productId = productId; 
    }
    
    public String getProductName() { 
        return productName; 
    }
    
    public void setProductName(String productName) { 
        this.productName = productName; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public BigDecimal getPrice() { 
        return price; 
    }
    
    public void setPrice(BigDecimal price) { 
        this.price = price; 
    }
    
    public Integer getStockQuantity() { 
        return stockQuantity; 
    }
    
    public void setStockQuantity(Integer stockQuantity) { 
        this.stockQuantity = stockQuantity; 
    }
    
    public LocalDateTime getTimestamp() { 
        return timestamp; 
    }
    
    public void setTimestamp(LocalDateTime timestamp) { 
        this.timestamp = timestamp; 
    }
}