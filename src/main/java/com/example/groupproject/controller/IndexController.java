package com.example.groupproject.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/index.html")
    public String index(Model model) {
        // Add data to the model
        String message = "Hello from the controller!";
        model.addAttribute("message", message);
        return "index"; // This will return the name of your HTML template (e.g., example.html)
    }

}
