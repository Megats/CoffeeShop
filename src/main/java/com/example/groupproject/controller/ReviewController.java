package com.example.groupproject.controller;

import com.example.groupproject.dbconfig.DatabaseConfig;
import com.example.groupproject.model.Review;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReviewController {

//DISPLAY FEEDBACK FOR ALL USER
    @GetMapping("/reviewAllDisplay")
    public String DisplayReview(Model model) throws SQLException {
        // Add data to the model
        model.addAttribute("review", displayReview());
        return "reviewAllDisplay"; // This will return the name of your HTML template (e.g., example.html)
    }

//DISPLAY FEEDBACK BASED ON ID
    @GetMapping("/reviewUserDisplay")
    public String DisplayReviewOnlyForUser(Model model) throws SQLException {
        // Add data to the model
        model.addAttribute("review", displayReviewOnlyForUser());
        for (Review review : displayReview()) {
            System.out.println(review.getIdPayment());
        }

        return "reviewUserDisplay"; // This will return the name of your HTML template (e.g., example.html)
    }

//DISPLAY ADMIN
    @GetMapping("admin/reviewAdmin")
    public String DisplayReviewAdmin(Model model) throws SQLException {
        // Add data to the model
        model.addAttribute("review", displayReviewAdmin());
        for (Review review : displayReviewAdmin()) {
            System.out.println(review.getIdPayment());
        }
        return "admin/reviewAdmin"; // This will return the name of your HTML template (e.g., example.html)
    }

    @PostMapping("/reviewAdminDelete")
    public String deleteAdminReview(@RequestParam("id") int id) throws SQLException {
        deleteReview(id);
        System.out.println("Id to delete is " + id);
        return "redirect:/reviewAdmin";
    }

//INSERT REVIEW

    @GetMapping("/reviewInsert")
    public String ViewPageInsert(Model model) throws SQLException {
        model.addAttribute("review", displayReview());
        return "reviewInsert"; // This will return the name of your HTML template (e.g., example.html)
    }

    @PostMapping("/reviewInsert")
    public String insertReview(@RequestParam("payment_id") int payment_id,
                               @RequestParam("review_desc") String review_desc,
                               @RequestParam("rating_value") int rating_value,
                               HttpServletResponse response) throws SQLException {

        // Process the review based on the received parameters
        System.out.println("POST::: Payment ID: " + payment_id + ", Review Desc: " + review_desc + ", Rating: " + rating_value);

        Review review = new Review(payment_id, review_desc, rating_value);
        insertReview(review);

        return "redirect:/reviewAllDisplay";
    }
//DELETE REVIEW
    @PostMapping("/reviewDelete")
    public String updateReview(@RequestParam("id") int id) throws SQLException {
        deleteReview(id);
        System.out.println("Id to delete is " + id);
        return "redirect:/reviewUserDisplay";
    }


    @PostMapping("admin/reviewDelete")
    public String deleteRev(@RequestParam("payment_id") int id) throws SQLException {
        deleteReview(id);
        System.out.println("Id to delete is " + id);
        return "redirect:reviewAdmin";
    }


//UPDATE REVIEW

    @GetMapping("reviewUpdate/{id}")
    public String editReview(Model model, @PathVariable int id) throws SQLException {
        System.out.println("Id passed in url is: " + id);
        model.addAttribute("review", getReviewbyID(id));

        return "reviewUpdate";
    }
    @PostMapping("/reviewUpdate")
    public String updateReview(@RequestParam("payment_id") int payment_id,
                               @RequestParam("review_desc") String review_desc,
                               @RequestParam("rating_value") int rating_value,
                               HttpServletResponse response) throws SQLException

    {
        Review review = new Review(payment_id, review_desc, rating_value);
        updateReviewFromDatabase(review);

        /*return "redirect:/reviewUpdate/"+payment_id;*/
        return "redirect:/reviewUserDisplay";

    }

    public List<Review> displayReview() throws SQLException {
        // Replace with your connection details
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column names (replace as needed)
        String tableName = "review";
        String idReview = "review_id"; // Assuming ID is auto-incremented after insert
        String idPayment = "payment_id";
        String descReview = "review_desc"; // Add the age column name
        String dateReview = "review_date";
        String ratingValue = "rating_value";


        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
//create array
        List<Review> reviews = new ArrayList<>();
        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a statement object
            statement = connection.createStatement();

            // Write the SQL query to retrieve recently inserted data
            // Assuming auto-increment on ID, retrieve the maximum ID
            String sql = "SELECT review_id,  review_desc, review_date ,rating_value FROM " + tableName;

            // Execute the query and get the results
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int review_id = resultSet.getInt(idReview);
                String review_desc = resultSet.getString(descReview);
                Date review_date = resultSet.getDate(dateReview);
                int rating_value = resultSet.getInt(ratingValue);

                Review review = new Review(review_id, review_desc, rating_value, review_date);
                reviews.add(review);

                System.out.println("Recently Inserted Data:");
                System.out.println("review_id: " + review_id + "review_desc: " + review_desc + "rating_value: " + rating_value
                        + "review_date: " + review_date);
            }
            return reviews;

        } catch (
                SQLException e) {
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
        return reviews;
    }

    public List<Review> displayReviewOnlyForUser() throws SQLException {
        // Replace with your connection details
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column names (replace as needed)
        String tableName = "review";
        String idReview = "review_id"; // Assuming ID is auto-incremented after insert
        String idPayment = "payment_id";
        String descReview = "review_desc"; // Add the age column name
        String dateReview = "review_date";
        String ratingValue = "rating_value";


        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
//create array
        List<Review> reviews = new ArrayList<>();
        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a statement object
            statement = connection.createStatement();

            // Write the SQL query to retrieve recently inserted data
            // Assuming auto-increment on ID, retrieve the maximum ID
            String sql = "SELECT review_id,  review_desc, review_date ,rating_value FROM " + tableName;

            // Execute the query and get the results
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int review_id = resultSet.getInt(idReview);
                String review_desc = resultSet.getString(descReview);
                Date review_date = resultSet.getDate(dateReview);
                int rating_value = resultSet.getInt(ratingValue);

                Review review = new Review(review_id, review_desc, rating_value, review_date);
                reviews.add(review);

                System.out.println("Recently Inserted Data:");
                System.out.println("review_id: " + review_id + "review_desc: " + review_desc + "rating_value: " + rating_value
                        + "review_date: " + review_date);
            }
            return reviews;

        } catch (
                SQLException e) {
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
        return reviews;
    }

    public List<Review> displayReviewAdmin() throws SQLException {
        // Replace with your connection details
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column names (replace as needed)
        String tableName = "review";
        String idReview = "review_id"; // Assuming ID is auto-incremented after insert
        String idPayment = "payment_id";
        String descReview = "review_desc"; // Add the age column name
        String dateReview = "review_date";
        String ratingValue = "rating_value";


        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
//create array
        List<Review> reviews = new ArrayList<>();
        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a statement object
            statement = connection.createStatement();

            // Write the SQL query to retrieve recently inserted data
            // Assuming auto-increment on ID, retrieve the maximum ID
            String sql = "SELECT review_id,  review_desc, review_date ,rating_value FROM " + tableName;

            // Execute the query and get the results
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int review_id = resultSet.getInt(idReview);
                String review_desc = resultSet.getString(descReview);
                Date review_date = resultSet.getDate(dateReview);
                int rating_value = resultSet.getInt(ratingValue);

                Review review = new Review(review_id, review_desc, rating_value, review_date);
                reviews.add(review);

                System.out.println("Recently Inserted Data:");
                System.out.println("review_id: " + review_id + "review_desc: " + review_desc + "rating_value: " + rating_value
                        + "review_date: " + review_date);
            }
            return reviews;

        } catch (
                SQLException e) {
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
        return reviews;
    }

    public void insertReview(Review review) throws SQLException {
    // Replace with your connection details
            String jdbcUrl = DatabaseConfig.JDBC_URL;
            String username = DatabaseConfig.USERNAME;
            String password = DatabaseConfig.PASSWORD;

    // Database table and column names (replace as needed)
            String tableName = "review";
            String idPayment = "payment_id";
            String descReview = "review_desc";
            String dateReview = "review_date";
            String ratingValue = "rating_value";

    // Data to insert (modify as needed)
            int payment_id = review.getIdPayment();
            String review_desc = review.getDescReview();
            LocalDate currentDate = LocalDate.now();
            java.sql.Date date = java.sql.Date.valueOf(currentDate);
            int rating_value = review.getRatingValue();

            Connection connection = null;
            PreparedStatement statement = null;

            try {
    // Connect to MariaDB
                connection = DriverManager.getConnection(jdbcUrl, username, password);

    // Prepare SQL statement with placeholders for data
                String sql = "INSERT INTO " + tableName + " (" + idPayment + ", " + descReview + ", " + dateReview + "," + ratingValue + ") VALUES (?, ?, ?, ?)";
                statement = connection.prepareStatement(sql);

    // Set values for placeholders
                statement.setInt(1, 1);
                statement.setString(2, review_desc);
                statement.setDate(3, date);
                statement.setInt(4, rating_value);

    // Execute the INSERT statement
                int rowsAffected = statement.executeUpdate();

    // Check if any rows were inserted
                if (rowsAffected > 0) {
                    System.out.println("Record inserted successfully!");
                } else {
                    System.out.println("No records inserted!");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Close resources (connection, statement)
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
            System.out.println(statement);
        }

    public void deleteReview(int id) throws SQLException {
        // Replace with your connection details
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column names (replace as needed)
        String tableName = "review";
        String idReview = "review_id";  // Assuming deletion based on ID

        // ID of the record to delete (modify as needed)
        int idToDelete = id;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare SQL statement with placeholder
            String sql = "DELETE FROM " + tableName + " WHERE " + idReview + " = ?";
            statement = connection.prepareStatement(sql);

            // Set value for placeholder
            statement.setInt(1, id);

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

    public void deleteReviewAdmin(int id) throws SQLException {
        // Replace with your connection details
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column names (replace as needed)
        String tableName = "review";
        String idReview = "review_id";  // Assuming deletion based on ID

        // ID of the record to delete (modify as needed)
        int idToDelete = id;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare SQL statement with placeholder
            String sql = "DELETE FROM " + tableName + " WHERE " + idReview + " = ?";
            statement = connection.prepareStatement(sql);

            // Set value for placeholder
            statement.setInt(1, id);

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

    //GET ID REVIEW METHOD
    public Review getReviewbyID(int id) {
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        String tableName = "review";
        String idReview = "review_id";
        String custName = "cust_name";
        String idPayment = "payment_id";
        String descReview = "review_desc";
        String dateReview = "review_date";
        String ratingValue = "rating_value";

        Review review = new Review();

        try {
            // Connect to MariaDB
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            // Create a statement object
            Statement statement = connection.createStatement();

            String sql = "SELECT *" + "FROM " + tableName + " WHERE " + idReview + " = " + id;
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    int review_id = resultSet.getInt(idReview);
                    String review_desc = resultSet.getString(descReview);
                    Date review_date = resultSet.getDate(dateReview);
                    int rating_value = resultSet.getInt(ratingValue);

                    //create Product object and add to list
                    review = new Review(review_id, review_desc, rating_value, review_date);

                    return review;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database errors
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection errors
        }

        return review;
    }

    private void updateReviewFromDatabase(Review review) throws SQLException {
        // Replace with your connection details
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column names (replace as needed)
        String tableName = "review";
        String idReview = "review_id";
        String custName = "cust_name";
        String idPayment = "payment_id";
        String descReview = "review_desc";
        String dateReview = "review_date";
        String ratingValue = "rating_value";


        // Data to update (modify as needed)
        String desc = review.getDescReview();
        int rating = review.getRatingValue();
        int id = review.getIdPayment();


        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare SQL statement with placeholders
            String sql = "UPDATE " + tableName + " SET " +
                    descReview + " = ?, " +
                    ratingValue + " = ? " +
                    "WHERE " + idReview + " = ?";
            statement = connection.prepareStatement(sql);

            // Set values for placeholders
            statement.setString(1, desc);
            statement.setInt(2, rating);
            statement.setInt(3, id);


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


}

