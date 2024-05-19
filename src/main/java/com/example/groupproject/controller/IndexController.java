package com.example.groupproject.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String index(Model model) {
        // Add data to the model
        String message = "Hello from the controller!";
        model.addAttribute("message", message);
        return "index"; // This will return the name of your HTML template (e.g., example.html)
    }

    // Add method to catch the page if no path is specified
    @GetMapping("/")
    public String home(Model model) {
        return "index"; // This will return the name of your HTML template (e.g., example.html)
    }

    @GetMapping("/admin/home_admin")
    public String admin(Model model) {
        String message = "Hello from the controller!";
        model.addAttribute("message", message);
        return "home";
    }

    @GetMapping("/admin/home")
    public String order(Model model) {
        String message = "Hello from the controller!";
        model.addAttribute("message", message);
        return "/admin/home";
    }



}
