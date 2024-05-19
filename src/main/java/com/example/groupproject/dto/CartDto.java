package com.example.groupproject.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Data
@Builder
public class CartDto {
    private int cart_id;
    private int user_id;
    private int product_id;
    private int cart_quantity;
    private double cart_total;
    private ProductDto product;

    public CartDto(int user_id, int cart_id, int product_id, int cart_quantity, double cart_total, ProductDto product) {
        this.user_id = user_id;
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.cart_quantity = cart_quantity;
        this.cart_total = cart_total;
        this.product = product;
    }

    public int getCart_id() {
        return cart_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getCart_quantity() {
        return cart_quantity;
    }

    public double getCart_total() {
        return cart_total;
    }

    public ProductDto getProduct() {
        return product;
    }
    public void updateCartQuantity(int newQuantity) {
        this.cart_quantity = newQuantity;
    }

}

