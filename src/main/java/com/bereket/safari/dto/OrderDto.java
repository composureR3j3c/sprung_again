package com.bereket.safari.dto;

import java.time.LocalDateTime;

public class OrderDto {

    private Long id;
    private String productName;
    private int quantity;
    private double price;
    private LocalDateTime createdAt;

    // Optional: Include customer info
    private Long customerId;
    private String customerName;

    public OrderDto() {}

    public OrderDto(Long id, String productName, int quantity, double price, LocalDateTime createdAt,
                    Long customerId, String customerName) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.customerName = customerName;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}
