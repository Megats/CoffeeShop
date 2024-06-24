package com.example.groupproject.controller;

import com.example.groupproject.dbconfig.DatabaseConfig;

import com.example.groupproject.model.Cart;
import com.example.groupproject.model.Order;
import com.example.groupproject.model.Payment;
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

    String jdbcUrl = DatabaseConfig.JDBC_URL;
    String username = DatabaseConfig.USERNAME;
    String password = DatabaseConfig.PASSWORD;


    @GetMapping("/cart")
    public String getCartPage(Model model) throws SQLException {
        model.addAttribute("carts", retrieveCarts());

        return "cart";
    }

    @PostMapping("/cart")
    public String updateCartDatabase(@RequestParam("product_id") int product_id,
                             @RequestParam("cart_id") int cart_id,
                             @RequestParam("cart_quantity") int cart_quantity,
                             @RequestParam("product_price") double cart_total)
    {
        System.out.println("Quantity" + cart_quantity);
//        updateCart(product_id, cart_id, cart_quantity, cart_total);
        return "redirect:/cart";
    }

    @PostMapping("/cartDelete")
    public String deleteCart(@RequestParam("id") int id) throws SQLException {
        deleteCarts(id);
        System.out.println("Id to delete is " + id);
        return "redirect:/cart";
    }

    //Get list of carts
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

    //delete cart
    public void deleteCarts(int id) throws SQLException {
        // Database table and column names (replace as needed)
        String tableName = "cart";
        String idColumn = "cart_id";  // Assuming deletion based on ID

        // ID of the record to delete (modify as needed)
        int idToDelete = id;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare SQL statement with placeholder
            String sql = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
            statement = connection.prepareStatement(sql);

            // Set value for placeholder
            statement.setInt(1, idToDelete);

            // Execute the DELETE statement
            int rowsAffected = statement.executeUpdate();

            // Check if any rows were deleted
            if (rowsAffected > 0) {
                System.out.println("Record deleted successfully!");
            } else {
                System.out.println("No records deleted!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (statement, connection)
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }




//    public void updateCart(int cartId, int product_id, int cart_quantity, double cart_total) {
//        String jdbcUrl = DatabaseConfig.JDBC_URL;
//        String username = DatabaseConfig.USERNAME;
//        String password = DatabaseConfig.PASSWORD;
//
//        //Database table and column names
//        String tableName = "cart";
//        String cartQuantity = "cart_quantity";
//        String cartTotal = "cart_total";
//        System.out.println("Cart id is" + cartId);
//
//        cart_total = cart_quantity * cart_total;
//
//        Connection connection = null;
//        PreparedStatement statement = null;
//
//        try {
//            //Connect to MariaDB
//            connection = DriverManager.getConnection(jdbcUrl, username, password);
//            //Create a statement object
//
//
//            String sql = "UPDATE " + tableName +
//                    " SET " + cartQuantity + " = ?, " + cartTotal + " = ?" +
//                    " WHERE cart_id = ?";
//
//            statement = connection.prepareStatement(sql);
//            statement.setInt(1, cart_quantity);
//            statement.setDouble(2, cart_total);
//            statement.setInt(3, cartId);
//
//            //Execute the UPDATE statement
//            int rowsAffected = statement.executeUpdate();
//            System.out.println("Rows affected: " + rowsAffected);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (statement != null) {
//                    statement.close();
//                }
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    @PostMapping("/add_order")
//    public String addOrder(@RequestParam("cart_id") int cart_id, @RequestParam("order_quantity") int order_quantity,
//                           @RequestParam("order_total") double order_total) {
//        System.out.println("Quantity" + order_quantity);
//        insertOrder(cart_id,order_quantity, order_total);
//        return "checkout";
//    }

    // Method to update table cart
//    private void insertOrder(int cart_id,int order_quantity, double order_total) {
//        String jdbcUrl = DatabaseConfig.JDBC_URL;
//        String username = DatabaseConfig.USERNAME;
//        String password = DatabaseConfig.PASSWORD;
//
//        // Database table and column
//        String tableName = "orders";
//        String userIdColumn = "user_id";
//        String cartIdColumn = "cart_id";
//        String orderTotalColumn = "order_total";
//        String orderQuantityColumn = "order_quantity";
//        String orderStatusColumn = "order_status";
//
//
//        // Data to insert
//        int userId = 1;
//        int cartId =cart_id;
//        int orderQuantity = order_quantity;
//        double  orderTotal = order_total;
//        String orderStatus = "pending";
//        System.out.println("Order quantity sql is "+ orderQuantity);
//
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//
//        try {
//
//            // Connect to db
//            connection = DriverManager.getConnection(jdbcUrl, username, password);
//
//            // Prepare SQL statement with placeholders for data
//            String sql = "INSERT INTO " + tableName + " (" + userIdColumn + ", " + cartIdColumn + ", " + orderQuantityColumn + ", " + orderTotalColumn +"," +orderStatusColumn+ ") VALUES (?,?,?,?,?)";
//            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//
//            // Set the value for the placeholder
//            preparedStatement.setInt(1, userId);
//            preparedStatement.setInt(2, cartId);
//            preparedStatement.setInt(3, orderQuantity);
//            preparedStatement.setDouble(4,  orderTotal);
//            preparedStatement.setString(5, orderStatus);
//
//
//            //Execute the insert statement
//            int rowsAffected = preparedStatement.executeUpdate();
//
//
//            if (rowsAffected > 0) {
//                System.out.println("Record updated successfully!");
//                System.out.println("Rows affected: " + rowsAffected);
//            } else {
//                System.out.println("Record not insert successfully!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(preparedStatement != null){
//                    preparedStatement.close();
//                }
//                if(connection != null){
//                    connection.close();
//                }
//            } catch (SQLException e){
//                e.printStackTrace();
//            }
//        }
//
//
//    }
//    @PostMapping("/reviewDelete")
//    public String deleteCart(@RequestParam("id") int id) throws SQLException {
//        deleteCarts(id);
//        System.out.println("Id to delete is " + id);
//        return "redirect:/cart";
//    }
//    public void deleteCarts(int id) throws SQLException {
//        // Replace with your connection details
//        String jdbcUrl = DatabaseConfig.JDBC_URL;
//        String username = DatabaseConfig.USERNAME;
//        String password = DatabaseConfig.PASSWORD;
//
//        // Database table and column names (replace as needed)
//        String tableName = "cart";
//        String idCart = "cart_id";  // Assuming deletion based on ID
//
//
//        Connection connection = null;
//        PreparedStatement statement = null;
//
//        try {
//            // Connect to MariaDB
//            connection = DriverManager.getConnection(jdbcUrl, username, password);
//
//            // Prepare SQL statement with placeholder
//            String sql = "DELETE FROM " + tableName + " WHERE " + idCart + " = ?";
//            statement = connection.prepareStatement(sql);
//
//            // Set value for placeholder
//            statement.setInt(1, id);
//
//            // Execute the DELETE statement
//            int rowsAffected = statement.executeUpdate();
//
//            // Check if any rows were deleted
//            if (rowsAffected > 0) {
//                System.out.println("Record deleted successfully!");
//            } else {
//                System.out.println("No records deleted!");
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // Close resources (statement, connection)
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }
//
//    }
//    @PostMapping("/add_status")
//    public String addStatus(@RequestParam("cart_id") int cart_id){
//        insertStatus(cart_id);
//        return "/admin/adminOrder";
//    }
//
//    // Method to update table cart
//    private void insertStatus(int cart_id) {
//        String jdbcUrl = DatabaseConfig.JDBC_URL;
//        String username = DatabaseConfig.USERNAME;
//        String password = DatabaseConfig.PASSWORD;
//
//        // Database table and column
//        String tableName = "orders";
//        String cartIdColumn = "cart_id";
//        String orderStatusColumn = "order_status";
//
//        // Data to insert
//        int cartId =cart_id;
//        String orderStatus = "Completed";
//
//
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//
//        try {
//
//            // Connect to db
//            connection = DriverManager.getConnection(jdbcUrl, username, password);
//
//            // Prepare SQL statement with placeholders for data
//            String sql = "INSERT INTO " + tableName + " (" + cartIdColumn + "," + orderStatusColumn+") VALUES (?,?)";
//            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//
//            // Set the value for the placeholder
//
//            preparedStatement.setInt(1, cartId);
//            preparedStatement.setString(2,orderStatus );
//
//
//            //Execute the insert statement
//            int rowsAffected = preparedStatement.executeUpdate();
//
//
//            if (rowsAffected > 0) {
//                System.out.println("Record updated successfully!");
//                System.out.println("Rows affected: " + rowsAffected);
//            } else {
//                System.out.println("Record not insert successfully!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (preparedStatement != null) {
//                    preparedStatement.close();
//                }
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}


