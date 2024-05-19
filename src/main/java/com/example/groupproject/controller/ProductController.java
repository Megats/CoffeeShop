package com.example.groupproject.controller;

import com.example.groupproject.Services.ProductService;
import com.example.groupproject.dto.CartDto;
import com.example.groupproject.dto.ProductDto;
import com.example.groupproject.model.Cart;
import com.example.groupproject.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController{
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/menu")
    public String listMenu(Model model) {
        List<ProductDto> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "menu";
    }

    @GetMapping("/menu_drink")
    public String drink(Model model) {
        List<ProductDto> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "menu_drink";
    }
    @GetMapping("/menu_dessert")
    public String desserts(Model model) {
        List<ProductDto> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "menu_dessert";
    }


}
