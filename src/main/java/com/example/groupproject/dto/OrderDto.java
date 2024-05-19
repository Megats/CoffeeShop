package com.example.groupproject.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Data
@Builder
public class OrderDto {
    private int order_id;
    private int user_id;
    private int cart_id;
    private double order_total;
    private int order_quantity;
    private String order_status;
    private List<ProductDto> products;

    public OrderDto(int user_id, int order_id, int cart_id, double order_total, int order_quantity, String order_status, List<ProductDto> products) {
        this.user_id = user_id;
        this.order_id = order_id;
        this.cart_id = cart_id;
        this.order_total = order_total;
        this.order_quantity = order_quantity;
        this.order_status = order_status;
        this.products = products;
    }

    public OrderDto() {

    }

    public int getCart_id() {
        return cart_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public double getOrder_total() {
        return order_total;
    }

    public int getOrder_quantity() {
        return order_quantity;
    }

    public String getOrder_status() {
        return order_status;
    }

    public List<ProductDto> getProducts() {
        return products;
    }
}
