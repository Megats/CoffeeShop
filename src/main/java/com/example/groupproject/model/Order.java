package com.example.groupproject.model;

public class Order {
    private int id;
    private int orderId;
    private int userId;
    private int productId;
    private int quantity;
    private String description;

    public Order(int id, int orderId, int userId, int productId, int quantity, String description) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.description = description;
    }

    //getter
    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }
}
