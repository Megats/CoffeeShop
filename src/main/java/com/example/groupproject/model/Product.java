package com.example.groupproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Data
    @NoArgsConstructor
//    @Builder
    @Entity
    @Table(name = "product")
    public class Product {
    //declare variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int product_id;
    private String product_name;
    private String product_description;
    private double product_price;
    private int product_quantity;
    private String product_category;
    private String product_image;


    //constructor
    public Product(int product_id, String product_name, String product_description, double product_price, int product_quantity, String product_category, String product_image) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.product_category = product_category;
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getProduct_description() {
        return product_description;
    }

    public double getProduct_price() {
        return product_price;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public String getProduct_category() {
        return product_category;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}