package com.example.groupproject.controller;

import com.example.groupproject.Services.CartService;
import com.example.groupproject.Services.OrderServices;
import com.example.groupproject.Services.ProductService;
import com.example.groupproject.dbconfig.DatabaseConfig;
import com.example.groupproject.dto.CartDto;
import com.example.groupproject.dto.ProductDto;

import com.example.groupproject.model.Cart;
import com.example.groupproject.model.Order;
import com.example.groupproject.model.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


@Controller
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final OrderServices orderServices;

    public CartController(CartService cartService, ProductService productService, OrderServices orderServices) {
        this.cartService = cartService;
        this.productService = productService;
        this.orderServices = orderServices;
    }

    @GetMapping("/cart")
    public String getCartPage(Model model) {
        List<CartDto> carts = cartService.getAllCarts();
        List<ProductDto> products = new ArrayList<>();

        for (CartDto cart : carts) {
            ProductDto productDto = productService.getProductById(cart.getProduct_id());
            products.add(productDto);
        }

        model.addAttribute("carts", carts);
        model.addAttribute("products", products);
        return "cart";
    }
    @PostMapping("/cart")
    public String updateCartDatabase(@RequestParam("product_id") int product_id,
                             @RequestParam("cart_id") int cart_id,
                             @RequestParam("cart_quantity") int cart_quantity,
                             @RequestParam("product_price") double cart_total)
    {
        System.out.println("Quantity" + cart_quantity);
        updateCart(product_id, cart_id, cart_quantity, cart_total);
        return "redirect:/cart";
    }

    public void updateCart(int cartId, int product_id, int cart_quantity, double cart_total) {
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        //Database table and column names
        String tableName = "cart";
        String cartQuantity = "cart_quantity";
        String cartTotal = "cart_total";
        System.out.println("Cart id is" + cartId);

        cart_total = cart_quantity * cart_total;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            //Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            //Create a statement object


            String sql = "UPDATE " + tableName +
                    " SET " + cartQuantity + " = ?, " + cartTotal + " = ?" +
                    " WHERE cart_id = ?";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, cart_quantity);
            statement.setDouble(2, cart_total);
            statement.setInt(3, cartId);

            //Execute the UPDATE statement
            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @PostMapping("/add_order")
    public String addOrder(@RequestParam("cart_id") int cart_id, @RequestParam("order_quantity") int order_quantity,
                           @RequestParam("order_total") double order_total) {
        System.out.println("Quantity" + order_quantity);
        insertOrder(cart_id,order_quantity, order_total);
        return "checkout";
    }

    // Method to update table cart
    private void insertOrder(int cart_id,int order_quantity, double order_total) {
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column
        String tableName = "orders";
        String userIdColumn = "user_id";
        String cartIdColumn = "cart_id";
        String orderTotalColumn = "order_total";
        String orderQuantityColumn = "order_quantity";
        String orderStatusColumn = "order_status";


        // Data to insert
        int userId = 1;
        int cartId =cart_id;
        int orderQuantity = order_quantity;
        double  orderTotal = order_total;
        String orderStatus = "pending";
        System.out.println("Order quantity sql is "+ orderQuantity);

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            // Connect to db
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare SQL statement with placeholders for data
            String sql = "INSERT INTO " + tableName + " (" + userIdColumn + ", " + cartIdColumn + ", " + orderQuantityColumn + ", " + orderTotalColumn +"," +orderStatusColumn+ ") VALUES (?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set the value for the placeholder
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, cartId);
            preparedStatement.setInt(3, orderQuantity);
            preparedStatement.setDouble(4,  orderTotal);
            preparedStatement.setString(5, orderStatus);


            //Execute the insert statement
            int rowsAffected = preparedStatement.executeUpdate();


            if (rowsAffected > 0) {
                System.out.println("Record updated successfully!");
                System.out.println("Rows affected: " + rowsAffected);
            } else {
                System.out.println("Record not insert successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(preparedStatement != null){
                    preparedStatement.close();
                }
                if(connection != null){
                    connection.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }


    }

}

