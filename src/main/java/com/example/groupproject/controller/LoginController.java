package com.example.groupproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Wrong username/password");
        }
        return "login";
    }


    @PostMapping("/login")
    public String handleLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        System.out.println("Masuk handle login");
        // Now you have the username and password that were submitted in the form
        // You can add your logic here to handle the login

        // For now, let's just print them
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        return "login";
    }

}