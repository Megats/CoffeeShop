package com.example.groupproject.controller;

import com.example.groupproject.Services.CartService;
import com.example.groupproject.Services.OrderServices;
import com.example.groupproject.Services.ProductService;
import com.example.groupproject.Services.impl.OrderServiceImpl;
import com.example.groupproject.dbconfig.DatabaseConfig;
import com.example.groupproject.dto.CartDto;
import com.example.groupproject.dto.OrderDto;
import com.example.groupproject.dto.ProductDto;
import com.example.groupproject.model.Cart;
import com.example.groupproject.model.Order;
import com.example.groupproject.model.Product;
import com.example.groupproject.repository.CartRepository;
import com.example.groupproject.repository.OrderRepository;
import com.example.groupproject.repository.ProductRepository;
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
    private final CartService cartService;
    private final ProductService productService;
    private final OrderServices orderServices;

    public OrderController(CartService cartService, ProductService productService, OrderServices orderServices) {
        this.cartService = cartService;
        this.productService = productService;
        this.orderServices = orderServices;
    }

    @GetMapping("/order")
    public String getCartPage(Model model) {
        List<OrderDto> orders = orderServices.getAllOrders();
        List<CartDto> carts = new ArrayList<>();
        List<ProductDto> products = new ArrayList<>();

        for (OrderDto order : orders) {
            CartDto cartDto = cartService.getCartById(order.getCart_id());
            carts.add(cartDto);
            for (CartDto cart : carts) {
                ProductDto productDto = productService.getProductById(cart.getProduct_id());
                products.add(productDto);
            }
        }

        model.addAttribute("carts", carts);
        model.addAttribute("orders", orders);
        model.addAttribute("products", products);
        return "order";
    }
    @GetMapping("/adminOrder")
    public String getAdminCartPage(Model model) {
        List<OrderDto> orders = orderServices.getAllOrders();
        List<CartDto> carts = new ArrayList<>();
        List<ProductDto> products = new ArrayList<>();

        for (OrderDto order : orders) {
            CartDto cartDto = cartService.getCartById(order.getCart_id());
            carts.add(cartDto);
            for (CartDto cart : carts) {
                ProductDto productDto = productService.getProductById(cart.getProduct_id());
                products.add(productDto);
            }
        }

        model.addAttribute("carts", carts);
        model.addAttribute("orders", orders);
        model.addAttribute("products", products);
        return "/admin/adminOrder";
    }
    @PostMapping("/add_status")
    public String updateOrderDatabase(@RequestParam("order_id") int order_id)
    {
        updateOrder(order_id);
        return "redirect:/adminOrder";
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




