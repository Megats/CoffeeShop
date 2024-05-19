package com.example.groupproject.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class CheckoutController {
    @GetMapping("/checkout")
    public String checkout(Model model) {
        // Add data to the model
        return "checkout"; // This will return the name of your HTML template (e.g., example.html)
    }

}
