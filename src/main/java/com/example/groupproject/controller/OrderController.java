package com.example.groupproject.controller;

import com.example.groupproject.dbconfig.DatabaseConfig;
import com.example.groupproject.model.Cart;
import com.example.groupproject.model.Order;
import com.example.groupproject.model.Product;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class OrderController {


    @GetMapping("/order")
    public String getCartPage(Model model) {
        model.addAttribute("orders", getOrdersFromDatabase());
        return "order";
    }
    @GetMapping("/adminOrder")
    public String getAdminCartPage(Model model) {
        return "admin/adminOrder";
    }
    @PostMapping("/add_status")
    public String updateOrderDatabase(@RequestParam("order_id") int order_id)
    {
        updateOrder(order_id);
        return "redirect:/adminOrder";
    }

    @PostMapping("/add_order")
    public String addOrder(@RequestParam("cart_id") int cart_id, @RequestParam("order_total") double order_total, @RequestParam("order_quantity") int order_quantity) {
        Order order = new Order(cart_id, order_quantity, order_total);
        addOrderToDatabase(order);
        return "redirect:/checkout";
    }

    public List<Order> getOrdersFromDatabase() {
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        //Database table and column names
        String tableName = "orders";
        String orderId = "order_id";
        String userId = "user_id";
        String cartId = "cart_id";
        String orderTotal = "order_total";
        String orderStatus = "order_status";
        String orderQuantity = "order_quantity";

        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            //Create a statement object
            statement = connection.createStatement();
            //Execute the SELECT query
            resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            //Process the results
            while (resultSet.next()) {
                int order_id = resultSet.getInt(orderId);
                int user_id = resultSet.getInt(userId);
                int cart_id = resultSet.getInt(cartId);
                int order_quantity = resultSet.getInt(orderQuantity);
                double order_total = resultSet.getDouble(orderTotal);
                String order_status = resultSet.getString(orderStatus);

                Order order = new Order(user_id, order_id, cart_id, order_quantity, order_total, order_status);
                orders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void addOrderToDatabase(Order order) {
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        //Database table and column names
        String tableName = "orders";
        String userId = "user_id";
        String cartId = "cart_id";
        String orderTotal = "order_total";
        String orderStatus = "order_status";
        String orderQuantity = "order_quantity";

        Connection connection = null;
        PreparedStatement statement = null;
        int user_id = 1;
        double order_total = order.getOrder_total();
        String order_status = "pending";
        int cart_id = order.getCart_id();
        int quantity = 1;

        try {
            //Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            //Create a statement object

            String sql = "INSERT INTO " + tableName + " (" + userId + ", " + cartId + ", " + orderQuantity + ", " +orderTotal + ", " + orderStatus + ") VALUES (?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, user_id);
            statement.setInt(2, cart_id);
            statement.setInt(3, quantity);
            statement.setDouble(4, order_total);
            statement.setString(5, order_status);

            //Execute the INSERT statement
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

    public void updateOrder(int orderId) {
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        //Database table and column names
        String tableName = "orders";
        String updateStatus = "order_status";


        String status = "completed";


        Connection connection = null;
        PreparedStatement statement = null;

        try {
            //Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            //Create a statement object


            String sql = "UPDATE " + tableName + " SET " + updateStatus + " = ? WHERE order_id = ?";


            statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, orderId);


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

}




