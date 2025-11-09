package com.hugo.order.dto;

import java.util.List;

public class OrderCreateDTO {
    private String customerName;
    private String customerEmail;
    private List<OrderItemDTO> items;
    
    public OrderCreateDTO() {}
    
    public OrderCreateDTO(String customerName, String customerEmail, List<OrderItemDTO> items) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.items = items;
    }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}