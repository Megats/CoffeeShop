package com.example.groupproject.controller;

//import dbconfig.DatabaseConfig;
import com.example.groupproject.dbconfig.DatabaseConfig;
import com.example.groupproject.dto.paymentdto;
import com.example.groupproject.model.Payment;
import com.example.groupproject.service.paymentService;
import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Controller
public class PaymentController {
    private final paymentService paymentService;

    public PaymentController(paymentService payment){
        this.paymentService = payment;
    }

    @GetMapping("/admin/payment")
    public String adminPayment(Model model) throws SQLException {
        //get value from implmentation
        List<Payment> payments = retrievePayments();
        System.out.println("Payment id: " + payments.get(0).getPayment_id());
        model.addAttribute("payments", payments);
        return "admin/payment"; // This will return the name of your HTML template (e.g., example.html)
    }

    @GetMapping("/payment")
    public String payment(Model model) throws SQLException {
        //get value from implmentation
//        List<paymentdto> payments = paymentService.findAllPayment();
        List<Payment> payments = retrievePayments();
        model.addAttribute("payments", payments);
        return "payment"; // This will return the name of your HTML template (e.g., example.html)
    }

    @GetMapping("/paymentSuccess")
    public String handlePaymentSuccess(@RequestParam("payment_id") int paymentId) {
        // Update the payment status to "Paid" in the database
        try {
            UpdateStatus(paymentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //get value from implmentation
        return "paymentSuccess"; // This will return the name of your HTML template (e.g., example.html)
    }

    @PostMapping("/payment")
    public String processPayment(@RequestParam("order_id") int order_id,
                                 @RequestParam("payment_status") String payment_status,
                                 @RequestParam("payment_total") double total,
                                 HttpServletResponse response) throws StripeException, IOException {

        // Process the payment based on the received parameters
        System.out.println("POST::: Order ID: " + order_id + ", Payment Status: " + payment_status + ", Total: " + total);

        //assign post value insert for setPrice
        double price = total;

        //add data to database
        Payment payment = new Payment(order_id, total, payment_status);
        AddPayment(payment);
        System.out.println("Payment id is " + payment.getPayment_id());

        // Set your Stripe API Key
        Stripe.apiKey = "sk_test_51PEWXdCVQMcEGtfuYpFL60VeiSXXIuqETALW960OQwtOAFJkNrT4H4COiSik9u9ThR9MD0G7ujeBY0UiufqyeTf200kSZqbE1q";
        StripeClient client = new StripeClient("sk_test_51PEWXdCVQMcEGtfuYpFL60VeiSXXIuqETALW960OQwtOAFJkNrT4H4COiSik9u9ThR9MD0G7ujeBY0UiufqyeTf200kSZqbE1q");

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("MYR")
                .setUnitAmount((long) (total * 100)) // Stripe expects amount in cents
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(payment.getPayment_status()) // Replace with your product name
                                .build())
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPriceData(priceData)
                .setQuantity(1L)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(lineItem)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/paymentSuccess?payment_id=" + payment.getPayment_id())
                .setCancelUrl("http://localhost:8080/cancel")
                .build();


        return "redirect:" + client.checkout().sessions().create(params).getUrl();
    }

    //Get list of payment
    private List<Payment> retrievePayments() throws SQLException {
        // Replace with your connection details
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column names (replace as needed)
        String tableName = "payment";
        String paymentColumn = "payment_id";
        String orderColumn = "order_id"; // Assuming ID is auto-incremented after insert
        String PaymentTotal = "payment_total";
        String PaymentStatus = "payment_status"; // Add the age column name

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        //Declare an array to store data
        List<Payment> payments = new ArrayList<>();

        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a statement object
            statement = connection.createStatement();

            // Write the SQL query to retrieve recently inserted data
            // Assuming auto-increment on ID, retrieve the maximum ID
            String sql = "SELECT payment_id, order_id, payment_total, payment_status FROM " + tableName ;

            // Execute the query and get the results
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                    int payment_id = resultSet.getInt(paymentColumn);
                    int order_id = resultSet.getInt(orderColumn);
                    double total = resultSet.getDouble(PaymentTotal);
                    String status = resultSet.getString(PaymentStatus);
                    //store the value in the model
                    Payment payment = new Payment(payment_id, order_id, total, status);
                    payments.add(payment);
                    System.out.println("id is " + payment.getPayment_id());
//                    for (Payment p : payments) {
//                        System.out.println("Payment collection: " + p);
//                    }

                    System.out.println("Recently Inserted Data:");
                    System.out.println("ID: " + payment_id + ", PaymentTotal: " + total + ", PaymentStatus: " + status);
                    System.out.println("Payment: " + payments);

                }

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
        return payments;
    }

    //Insert payment into database
    public void AddPayment(Payment payment) {

        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column
        String tableName = "payment";
        String orderColumn = "order_id";
        String paymentStatus = "payment_status";
        String payment_total = "payment_Total";
        String paymentDate = "payment_date";

        // Data to insert
        int order_id = payment.getOrder_id();
        String payment_status = payment.getPayment_status();
        double total = payment.getPayment_total();
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Convert LocalDateTime to java.sql.Date
        java.sql.Date sqlDate = java.sql.Date.valueOf(currentDateTime.toLocalDate());

        Timestamp timestamp = Timestamp.valueOf(currentDateTime);


        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

        // Connect to db
        connection = DriverManager.getConnection(jdbcUrl, username, password);

        // Prepare SQL statement with placeholders for data
        String sql = "INSERT INTO " + tableName + " (" + orderColumn + ", " + paymentStatus + ", " +  payment_total + ", " + paymentDate + ")  VALUES (?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        System.out.println("Order id try to insert is " + payment.getOrder_id());

        // Set the value for the placeholder
        preparedStatement.setInt(1, order_id);
        preparedStatement.setString(2, payment_status);
        preparedStatement.setDouble(3, total);
        preparedStatement.setDate(4,  sqlDate);

        //print value that has been inserted
        System.out.println("PAYMENTT "+payment);

        //Execute the insert statement
        int rowsAffected = preparedStatement.executeUpdate();

        // Retrieve the generated keys
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

        if (generatedKeys.next()) {
            int generatedPaymentId = generatedKeys.getInt(1);
            payment.setPayment_id(generatedPaymentId); // Set the generated payment ID in your Payment object
        }

        if (rowsAffected > 0) {
        System.out.println("Record updated successfully!");
        System.out.println("Rows affected: " + rowsAffected);
        System.out.println("Payment id for the record is " + payment.getPayment_id());
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

    //Update payment status based on payment id
    public void UpdateStatus(int payment_id) throws SQLException {
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column
        String tableName = "payment";
        String orderColumn = "order_id";
        String nameColumn = "payment_status";

        // Data to insert
        int paymentId = payment_id;
        String payment_status = "Paid";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare SQL statement with placeholders
            String sql = "UPDATE " + tableName + " SET " + nameColumn + " = ? WHERE " + orderColumn + " = ?";
            statement = connection.prepareStatement(sql);

            // Set values for placeholders
            statement.setString(1, payment_status);
            statement.setInt(2, paymentId);

            // Execute the UPDATE statement
            int rowsAffected = statement.executeUpdate();

            // Check if any rows were updated
            if (rowsAffected > 0) {
                System.out.println("Record updated successfully!");
            } else {
                System.out.println("No records updated!");
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

    //Update payment status
    public void UpdatePaymentStatus(Payment payment) throws SQLException {

        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column
        String tableName = "payment";
        String orderColumn = "order_id";
        String nameColumn = "payment_status";

        // Data to insert
        int order_id = payment.getOrder_id();
        String payment_status = "Paid";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare SQL statement with placeholders
            String sql = "UPDATE " + tableName + " SET " + nameColumn + " = ? WHERE " + orderColumn + " = ?";
            statement = connection.prepareStatement(sql);

            // Set values for placeholders
            statement.setString(1, payment_status);
            statement.setInt(2, order_id);

            // Execute the UPDATE statement
            int rowsAffected = statement.executeUpdate();

            // Check if any rows were updated
            if (rowsAffected > 0) {
                System.out.println("Record updated successfully!");
            } else {
                System.out.println("No records updated!");
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


//    public Payment RetrievePayment() throws SQLException {
//
//        Payment Payment = null;
//        // Replace with your connection details
//        String jdbcUrl = DatabaseConfig.JDBC_URL;
//        String username = DatabaseConfig.USERNAME;
//        String password = DatabaseConfig.PASSWORD;
//
//        // Database table and column names (replace as needed)
//        String tableName = "payment";
//        String idColumn = "payment_id"; // Assuming ID is auto-incremented after insert
//        String PaymentTotal = "payment_total";
//        String PaymentStatus = "payment_status"; // Add the age column name
//
//        Connection connection = null;
//        Statement statement = null;
//        ResultSet resultSet = null;
//
//        try {
//            // Connect to MariaDB
//            connection = DriverManager.getConnection(jdbcUrl, username, password);
//
//            // Create a statement object
//            statement = connection.createStatement();
//
//            // Write the SQL query to retrieve recently inserted data
//            // Assuming auto-increment on ID, retrieve the maximum ID
//            String sql = "SELECT payment_id, payment_total, payment_status FROM " + tableName ;
//
//            // Execute the query and get the results
//            resultSet = statement.executeQuery(sql);
//
//            //check if the result set have more than 1 row
//            do{
//                if (resultSet.next()) {
//                    int id = resultSet.getInt(idColumn);
//                    double total = resultSet.getDouble(PaymentTotal);
//                    String status = resultSet.getString(PaymentStatus);
//                    //store the value in the model
//                    Payment payment = new Payment(id, total, status);
//                    payment.add(payment);
//
//                    System.out.println("Recently Inserted Data:");
//                    System.out.println("ID: " + id + ", PaymentTotal: " + total + ", PaymentStatus: " + status);
//                    return payment;
//
//                } else {
//                    System.out.println("No data found!");
//                }
//            }while (resultSet.next());
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // Close resources (result set, statement, connection)
//            if (resultSet != null) {
//                resultSet.close();
//            }
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }
//        return Payment;
//    }
    }
}