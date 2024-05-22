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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    String jdbcUrl = DatabaseConfig.JDBC_URL;
    String username = DatabaseConfig.USERNAME;
    String password = DatabaseConfig.PASSWORD;

//    private final paymentService paymentService;
//
//    public PaymentController(paymentService payment){
//        this.paymentService = payment;
//    }

    @GetMapping("/admin/payment")
    public String adminPayment(Model model) throws SQLException {
        //get value from implmentation
        List<Payment> payments = retrievePayments();
        model.addAttribute("payments", payments);
        for(Payment payment : payments){
            System.out.println("Payment ID: " + payment.getPayment_id());
            System.out.println("Order ID: " + payment.getOrder_id());
            System.out.println("Total: " + payment.getPayment_total());
            System.out.println("Status: " + payment.getPayment_status());
            System.out.println("Payment Method: " + payment.getPayment_method());
        }

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
    public String handlePaymentSuccess(@RequestParam("payment_id") int paymentId) throws SQLException {
        // Update the payment status to "Paid" in the database
        Payment payment = new Payment();
        payment = findbypaymentid(paymentId);
        System.out.println("Current payment method is " + payment.getPayment_method());
        try {
            if(payment.getPayment_method().equals("Payment Gateway")){
                UpdateStatus(paymentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //get value from implmentation
        return "paymentSuccess"; // This will return the name of your HTML template (e.g., example.html)
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout"; // This will return the name of your HTML template (e.g., example.html)
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam("payment_method") String payment_method,
                                 @RequestParam("total") double total,
                                 @RequestParam("order_id") int order_id,
                                 HttpServletResponse response) throws StripeException, IOException {


        //assign post value insert for setPrice
        double price = total;
        String payment_status = "Pending";

        //add data to database
        Payment payment = new Payment(order_id, total, payment_status,payment_method);
        AddPayment(payment);

        //Payment Gateway
        if(payment_method.equals("Payment Gateway")) {
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
        else {
            return "redirect:paymentSuccess?payment_id=" + payment.getPayment_id();
        }
    }


    @PostMapping("/deletePayment")
    public String delete(@RequestParam("payment_id") int payment_id) throws SQLException {
        DeletePayment(payment_id);
        return "redirect:admin/payment";
    }

    @GetMapping("admin/edit_payment/{id}")
    public String editPayment(Model model, @PathVariable("id") int id) throws SQLException {
        System.out.println("Id passed in url is: " + id);

        model.addAttribute("payment", findbypaymentid(id));

        return "admin/edit_payment";
    }

    @PostMapping("/edit_payment")
    public String updatePayment(@RequestParam("payment_id") int payment_id,
                                @RequestParam("payment_status") String payment_status) throws SQLException {

        UpdatePaymentStatus(payment_id, payment_status);
        return "redirect:admin/payment";
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
        String PaymentMethod = "payment_method";

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
            String sql = "SELECT payment_id, order_id, payment_total, payment_status, payment_method FROM " + tableName ;

            // Execute the query and get the results
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                    int payment_id = resultSet.getInt(paymentColumn);
                    int order_id = resultSet.getInt(orderColumn);
                    double total = resultSet.getDouble(PaymentTotal);
                    String status = resultSet.getString(PaymentStatus);
                    String payment_method = resultSet.getString(PaymentMethod);
                    //store the value in the model
                    Payment payment = new Payment(payment_id, order_id, total, status, payment_method);
                    payments.add(payment);
                }
            return payments;

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
        String paymentMethod = "payment_method";

        // Data to insert
        int order_id = payment.getOrder_id();
        String payment_status = payment.getPayment_status();
        double total = payment.getPayment_total();
        String payment_method = payment.getPayment_method();
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
        String sql = "INSERT INTO " + tableName + " (" + orderColumn + ", " + paymentStatus + ", " +  payment_total + ", " + paymentMethod + "," + paymentDate +")  VALUES (?, ?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);


        // Set the value for the placeholder
        preparedStatement.setInt(1, order_id);
        preparedStatement.setString(2, payment_status);
        preparedStatement.setDouble(3, total);
        preparedStatement.setString(4, payment_method);
        preparedStatement.setDate(5,  sqlDate);

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
        String paymentColumn = "payment_id";
        String nameColumn = "payment_status";

        // Data to insert
        String payment_status = "Paid";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare SQL statement with placeholders
            String sql = "UPDATE " + tableName + " SET " + nameColumn + " = ? WHERE " + paymentColumn + " = ?";
            statement = connection.prepareStatement(sql);

            // Set values for placeholders
            statement.setString(1, payment_status);
            statement.setInt(2, payment_id);

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

    //Update payment status on admin
    public void UpdatePaymentStatus(int payment_id, String payment_status) throws SQLException {

        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column
        String tableName = "payment";
        String nameColumn = "payment_status";
        String orderColumn = "payment_id";


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
            statement.setInt(2, payment_id);

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

    //Search payment by payment id
    public Payment findbypaymentid(int id) throws SQLException {

        // Database table and column names (replace as needed)
        String tableName = "payment";
        String paymentColumn = "payment_id";
        String orderColumn = "order_id"; // Assuming ID is auto-incremented after insert
        String PaymentTotal = "payment_total";
        String PaymentStatus = "payment_status"; // Add the age column name
        String PaymentMethod = "payment_method";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Payment payment = new Payment();

        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a statement object
            statement = connection.createStatement();

            // Write the SQL query to retrieve recently inserted data
            // Assuming auto-increment on ID, retrieve the maximum ID
            String sql = "SELECT payment_id, order_id, payment_total, payment_status, payment_method FROM " + tableName +
                    " WHERE " + paymentColumn + " = " + id;

            // Execute the query and get the results
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                int payment_id = resultSet.getInt(paymentColumn);
                int order_id = resultSet.getInt(orderColumn);
                double total = resultSet.getDouble(PaymentTotal);
                String status = resultSet.getString(PaymentStatus);
                String payment_method = resultSet.getString(PaymentMethod);
                //store the value in the model
                payment = new Payment(payment_id, order_id, total, status, payment_method);

                return payment;
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
        return payment;

    }

    public void DeletePayment(int payment_id) throws SQLException {
        // Database table and column names (replace as needed)
        String tableName = "payment";
        String idColumn = "payment_id";  // Assuming deletion based on ID

        // ID of the record to delete (modify as needed)
        int idToDelete = payment_id;

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

    //Search Order by order id in payment table

    //View payment checkout order
//    public List<Payment> retrieveOrder(int order_id) throws SQLException {
//        String jdbcUrl = DatabaseConfig.JDBC_URL;
//        String username = DatabaseConfig.USERNAME;
//        String password = DatabaseConfig.PASSWORD;
//
//        // Database table and column
//        String tableName = "payment";
//        String orderColumn = "order_id";
//        String nameColumn = "payment_status";
//
//        // Data to insert
//        int order_id = order_id;
//
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        List<Payment> payments = new ArrayList<>();
//
//        try {
//            // Connect to MariaDB
//            connection = DriverManager.getConnection(jdbcUrl, username, password);
//
//            // Prepare SQL statement with placeholders
////            String sql = "SELECT * FROM " + tableName + " WHERE " + orderColumn + " = ?";
//            //sql join table payment and order and cart
//            String sql = "SELECT payment.payment_id, payment.order_id, payment.payment_total, payment.payment_status, " +
//                         "order.order_id, order.order_date, order.order_status, cart.cart_id, cart.product_id, cart.quantity " +
//                         "FROM payment INNER JOIN order ON payment.order_id = order.order_id INNER JOIN cart ON order.order_id = cart.order_id " +
//                         "WHERE payment.order_id = ?";
//            statement = connection.prepareStatement(sql);
//
//            // Set values for placeholders
//            statement.setInt(1, order_id);
//
//            // Execute the SELECT statement
//            resultSet = statement.executeQuery();
//
//            // Check if any rows were updated
//            if (resultSet.next()) {
//                int payment_id = resultSet.getInt("payment_id");
//                int order_id = resultSet.getInt("order_id");
//                double total = resultSet.getDouble("payment_total");
//                String status = resultSet.getString("payment_status");
//
//                Payment payment = new Payment(payment_id,order_id, )
//                payments.add()
//
//                return payments;
//            } else {
//                System.out.println("No records updated!");
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return payments;
//    }

}