package com.example.groupproject.controller;

import com.example.groupproject.Services.CartService;
import com.example.groupproject.Services.OrderServices;
import com.example.groupproject.Services.ProductService;
import com.example.groupproject.Services.impl.OrderServiceImpl;
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
}




