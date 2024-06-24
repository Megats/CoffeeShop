package com.example.groupproject.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Data

public class ProductDto {
    private int product_id;
    private String product_name;
    private String product_description;
    private double product_price;
    private int product_quantity;
    private String product_category;
    private String product_image;

    public ProductDto(String product_name, int product_id, String product_description, double product_price, int product_quantity, String product_category, String product_image) {
        this.product_name = product_name;
        this.product_id = product_id;
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

}
