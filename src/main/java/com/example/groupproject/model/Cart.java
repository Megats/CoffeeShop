package com.example.groupproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Data
@NoArgsConstructor
//@Builder
@Entity
@Table(name = "cart")
public class Cart {
    //declare variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cart_id;
    private int user_id;
    private int product_id;
    private int cart_quantity;
    private double cart_total;


    //constructor
    public Cart(int cart_id, int user_id, int product_id, int cart_quantity, double cart_total) {
        this.cart_id = cart_id;
        this.user_id = user_id;
        this.product_id = product_id;
        this.cart_quantity = cart_quantity;
        this.cart_total = cart_total;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getCart_id() {
        return cart_id;
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

}


