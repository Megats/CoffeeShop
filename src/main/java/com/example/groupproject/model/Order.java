package com.example.groupproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //declare variables
    private int order_id;
    private int user_id;
    private int cart_id;
    private int order_quantity;
    private double order_total;
    private String order_status;


    //constructor
    public Order(int user_id, int order_id, int cart_id, int order_quantity, double order_total, String order_status) {
        this.user_id = user_id;
        this.order_id = order_id;
        this.cart_id = cart_id;
        this.order_quantity = order_quantity;
        this.order_total = order_total;
        this.order_status = order_status;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public int getOrder_quantity() {
        return order_quantity;
    }

    public double getOrder_total() {
        return order_total;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public void setOrder_quantity(int order_quantity) {
        this.order_quantity = order_quantity;
    }

    public void setOrder_total(double order_total) {
        this.order_total = order_total;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}




