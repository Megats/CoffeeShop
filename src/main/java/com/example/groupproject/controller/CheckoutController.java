package com.example.groupproject.controller;

import com.example.groupproject.dbconfig.DatabaseConfig;
import com.example.groupproject.model.Cart;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckoutController {
    @GetMapping("/checkout")
    public String checkout(Model model) throws SQLException {
        // Add data to the model
        model.addAttribute("carts", retrieveCarts());
        return "checkout"; // This will return the name of your HTML template (e.g., example.html)
    }

    private List<Cart> retrieveCarts() throws SQLException {
        // Replace with your connection details
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column names (replace as needed)
        String tableName = "cart";
        String cartColumn = "cart_id";
        String productColumn = "product_id";
        String cartTotalColumn = "cart_total";
        String cartQuantityColumn = "cart_quantity"; // Add the age column name
        String cartProductName = "product_name";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        //Declare an array to store data
        List<Cart> carts = new ArrayList<>();

        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a statement object
            statement = connection.createStatement();

            // Write the SQL query to retrieve recently inserted data
            // Assuming auto-increment on ID, retrieve the maximum ID
            String sql = "SELECT " + cartColumn + "," + productColumn + ", " + cartQuantityColumn + ", " + cartTotalColumn + ",  "  + cartProductName +" FROM " + tableName;

            // Execute the query and get the results
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int cart_id = resultSet.getInt(cartColumn);
                int product_id = resultSet.getInt(productColumn);
                int quantity = resultSet.getInt(cartQuantityColumn);
                double total = resultSet.getDouble(cartTotalColumn);
                String product_name = resultSet.getString(cartProductName);
                //store the value in the model
                Cart cart = new Cart(cart_id,product_id,quantity,total,product_name);
                carts.add(cart);
                System.out.println("display cart is " + cart.getCart_id());
            }
            return carts;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (result set, statement, connection)
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return carts;
    }


}
