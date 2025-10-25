package com.hugo.common.dto;

import com.hugo.common.enums.OrderStatus;
import java.util.List;

public class OrderDTO {
    private Long id;
    private List<Long> productIds;
    private OrderStatus status;
    
    public OrderDTO() {}
    
    public OrderDTO(Long id, List<Long> productIds, OrderStatus status) {
        this.id = id;
        this.productIds = productIds;
        this.status = status;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public List<Long> getProductIds() {
        return productIds;
    }
    
    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
