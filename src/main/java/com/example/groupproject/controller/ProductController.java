package com.example.groupproject.controller;

import com.example.groupproject.dbconfig.DatabaseConfig;
import com.example.groupproject.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ProductController{

        @GetMapping("/menu")
        public String menu(Model model) {

            model.addAttribute("products", getProducts());
            return "menu";
        }
        @GetMapping("/menu_drink")
        public String drink(Model model) {
            System.out.println(getProducts());
            model.addAttribute("products", getProducts());
            return "menu_drink";
        }
        @GetMapping("/menu_dessert")
        public String desserts(Model model) {
        System.out.println(getProducts());
        model.addAttribute("products", getProducts());
        return "menu_dessert";
    }

        //view
        public List<Product> getProducts() {
            String jdbcUrl = DatabaseConfig.JDBC_URL;
            String username = DatabaseConfig.USERNAME;
            String password = DatabaseConfig.PASSWORD;

            //Database table and column names
            String tableName = "product";
            String productImage = "product_image";
            String productId = "product_id";
            String productName= "product_name";
            String productDesc = "product_description";
            String productPrice = "product_price";
            String productCat = "product_category";
            String productQty = "product_quantity";
            List<Product> products = new ArrayList<>();


            try {
                //Connect to MariaDB
                Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                //Create a statement object
                Statement statement = connection.createStatement();

                String sql = "SELECT " +productImage+ ", " + productId + ", " + productName + ", " + productDesc + ", " + productPrice + ", " + productCat + ", " + productQty + " FROM " + tableName;


                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        String image = resultSet.getString(productImage);
                        int id = resultSet.getInt(productId);
                        String name = resultSet.getString(productName);
                        String desc = resultSet.getString(productDesc);
                        double price = resultSet.getDouble(productPrice);
                        String category = resultSet.getString(productCat);
                        int qty = resultSet.getInt(productQty);
                        //create Product object and add to list
                        Product product = new Product(image, id, name, desc, price,category, qty);
                        products.add(product);
                    }
                    return products;

                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle database errors
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection errors
            }

            // Return the name of your HTML template (e.g., menu.html)
            return products;
        }

}
