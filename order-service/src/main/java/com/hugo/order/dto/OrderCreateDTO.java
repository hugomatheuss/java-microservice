package com.hugo.order.dto;

public class OrderCreateDTO {
    
    private Long productId;
    private Integer quantity;
    private String customerName;
    private String customerEmail;
    
    public OrderCreateDTO() {}
    
    public OrderCreateDTO(Long productId, Integer quantity, String customerName, String customerEmail) {
        this.productId = productId;
        this.quantity = quantity;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}