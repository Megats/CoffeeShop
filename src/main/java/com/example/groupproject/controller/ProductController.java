package com.example.groupproject.controller;

import com.example.groupproject.dbconfig.DatabaseConfig;
import com.example.groupproject.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.StandardCopyOption;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ProductController{

    @GetMapping("/menu")
    public String menu(Model model) {

        model.addAttribute("products", getProducts());
        return "menu";
    }
    @GetMapping("admin/menu")
    public String admin_menu(Model model){
        model.addAttribute("products",getProducts());
        return "admin/menu";
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

    @GetMapping("admin/product_add")
    public String products(Model model) {
        return "admin/product_add";
    }

    @GetMapping("admin/product_edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id) {
        System.out.println("Id passed in url is: " + id);
        model.addAttribute("product", getProductbyId(id));

        return "admin/product_edit";
    }

    @PostMapping("/add_cart")
    public String addCart(@RequestParam("product_id") int product_id, @RequestParam("produce_price") double product_price){
        insertCart(product_id,product_price);
        return "redirect:/menu";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@RequestParam("product_id") int productId,
                                @RequestParam("product_image") MultipartFile productImage,
                                @RequestParam("product_name") String productName,
                                @RequestParam("product_description") String productDescription,
                                @RequestParam("product_price") double productPrice,
                                @RequestParam("product_quantity") int productQuantity,
                                @RequestParam("product_category") String productCategory) throws SQLException {
        if (!productImage.isEmpty()) {
            try {
                // Save the image file to the upload directory
                String uploadDirectory = "src/main/resources/static/images/";
                String fileName = productImage.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDirectory + fileName);
                Files.copy(productImage.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
                fileName = "images/"+ fileName;

                Product product = new Product(fileName,productId,productName,productDescription,productPrice,productCategory,productQuantity);
                updateProductFromDatabase(product);

                } catch (IOException e) {
                throw new RuntimeException(e);
            }catch (Exception e){
                System.out.println("Error found");

            }
        }
        else {
            String fileName = "";
            Product product = new Product(fileName,productId,productName,productDescription,productPrice,productCategory,productQuantity);
            updateProductFromDatabase(product);

        }
        return "redirect:/admin/product_list?success=true";
    }


    @GetMapping("admin/product_list")
    public String product_list(Model model) {
        // Retrieve the list of products from the database
        List<Product> products = getProducts();

        // Add the list of products to the model attribute
        model.addAttribute("products", products);

        // Return the correct template path
        return "admin/product_list";
    }

    @PostMapping("/product_add")
    public String addProduct(@RequestParam("product_id") int productId,
                             @RequestParam("product_image") MultipartFile productImage,
                             @RequestParam("product_name") String productName,
                             @RequestParam("product_description") String productDescription,
                             @RequestParam("product_price") double productPrice,
                             @RequestParam("product_quantity") int productQuantity,
                             @RequestParam("product_category") String productCategory) {

        if (productImage.isEmpty()) {
            // Handle empty file
            return "redirect:/admin/product_add?error=emptyfile";
        }

        try {
            // Save the image file to the upload directory
            String uploadDirectory = "src/main/resources/static/images/";
            String fileName = productImage.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDirectory + fileName);
            Files.copy(productImage.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
            fileName = "images/"+ fileName;

            // Insert product into the database
            try (Connection connection = DriverManager.getConnection(DatabaseConfig.JDBC_URL, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD)) {
                String sql = "INSERT INTO product (product_id, product_image, product_name, product_description, product_price, product_quantity, product_category) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, productId);
                    preparedStatement.setString(2, fileName); // Save file name instead of path
                    preparedStatement.setString(3, productName);
                    preparedStatement.setString(4, productDescription);
                    preparedStatement.setDouble(5, productPrice);
                    preparedStatement.setInt(6, productQuantity);
                    preparedStatement.setString(7, productCategory);
                    preparedStatement.executeUpdate();

                    System.out.println("Product added successfully"); // Added print statement

                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            // Handle file upload or database errors
            return "redirect:/admin/product_add?error=uploadfailed";
        }

        return "redirect:/admin/product_add?success=true";
    }

    @PostMapping("/delete_product")
    public String deleteProduct(@RequestParam("product_id") int productId) {
        try (Connection connection = DriverManager.getConnection(DatabaseConfig.JDBC_URL, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD)) {
            String sql = "DELETE FROM product WHERE product_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, productId);
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Product with ID " + productId + " has been deleted");
                } else {
                    System.out.println("Product with ID " + productId + " was not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
            return "redirect:/admin/product_list?error=true";
        }

        return "redirect:/admin/product_list?success=true";
    }

    public Product getProductbyId(int product_id) {
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
        Product product = new Product(productImage,0,productName,productDesc,0,productCat,0);


        try {
            //Connect to MariaDB
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            //Create a statement object
            Statement statement = connection.createStatement();

            String sql = "SELECT *" + "FROM " + tableName + " WHERE " + productId + " = " + product_id;

            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    String image = resultSet.getString(productImage);
                    int id = resultSet.getInt(productId);
                    String name = resultSet.getString(productName);
                    String desc = resultSet.getString(productDesc);
                    double price = resultSet.getDouble(productPrice);
                    String category = resultSet.getString(productCat);
                    int qty = resultSet.getInt(productQty);
                    //create Product object and add to list
                    product = new Product(image, id, name, desc, price, category, qty);
                    System.out.println("descriptioon " + desc);

                    return product;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database errors
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection errors
        }

        // Return the name of your HTML template (e.g., menu.html)
        return product;

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

    // Method to delete the product from the database
    private void deleteProductFromDatabase(int productId) throws SQLException {
        // Replace with your connection details
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column names (replace as needed)
        String tableName = "product";
        String idProduct = "product_id";  // Assuming deletion based on ID

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Connect to the database
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare SQL statement with placeholder
            String sql = "DELETE FROM " + tableName + " WHERE " + idProduct + " = ?";
            statement = connection.prepareStatement(sql);

            // Set value for placeholder
            statement.setInt(1, productId);

            // Execute the DELETE statement
            int rowsAffected = statement.executeUpdate();

            // Check if any rows were deleted
            if (rowsAffected > 0) {
                System.out.println("Product with ID " + productId + " deleted successfully!");
            } else {
                System.out.println("No records deleted!");
            }
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

    // Method to update table
    private void updateProductFromDatabase(Product product) throws SQLException {
        // Replace with your connection details
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column names (replace as needed)
        String tableName = "product";
        String productImage = "product_image";
        String idColumn = "product_id";
        String productName= "product_name";
        String productDesc = "product_description";
        String productPrice = "product_price";
        String productCat = "product_category";
        String productQty = "product_quantity";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Connect to MariaDB
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare SQL statement with placeholders
            String sql = "UPDATE " + tableName + " SET " + productImage + " = ?, " + productName + " = ?," + productDesc + " = ?," + productPrice + " = ?," + productCat + " = ?," + productQty + " = ?" +
                     " WHERE " + idColumn + " = ?";
            statement = connection.prepareStatement(sql);

            // Set values for placeholders
            statement.setString(1, product.getProduct_image());
            statement.setString(2, product.getProduct_name());
            statement.setString(3, product.getProduct_description());
            statement.setDouble(4, product.getProduct_price());
            statement.setString(5, product.getProduct_category());
            statement.setInt(6, product.getProduct_quantity());
            statement.setInt(7, product.getProduct_id());

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

    // Method to update table cart
    private void insertCart(int id, double product_price){
        String jdbcUrl = DatabaseConfig.JDBC_URL;
        String username = DatabaseConfig.USERNAME;
        String password = DatabaseConfig.PASSWORD;

        // Database table and column
        String tableName = "cart";
        String userColumn = "user_id";
        String productColumn = "product_id";
        String quantityColumn = "cart_quantity";
        String priceColumn = "cart_total";

        // Data to insert
        int user_id = 1;
        int product_id = id;
        int cartQuantity = 1;
        double price = product_price;

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            // Connect to db
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare SQL statement with placeholders for data
            String sql = "INSERT INTO " + tableName + " (" + userColumn + ", " + productColumn + ", " +  quantityColumn + ", " + priceColumn + ")  VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set the value for the placeholder
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, product_id);
            preparedStatement.setDouble(3, cartQuantity);
            preparedStatement.setDouble(4,  price);


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

