package com.example.groupproject.controller;

//import com.example.groupproject.model.Supplier;
import com.example.groupproject.dto.UserDto;
import com.example.groupproject.entity.User;
import com.example.groupproject.service.UserService;
        import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
/*
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
*/


@Controller
//@RequestMapping("/register")
public class RegisterController {

    private UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    //handler method to handle user registration form request
//    @GetMapping
    @GetMapping("/register")
    public String register(Model model) {
        //create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        System.out.println("Register show controller");
        return "register";
    }

    //Handler method to handle user registration form submit request
//    @PostMapping
    @PostMapping("/register/save")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result, Model model) {
        System.out.println("post mapping for register");

        User existingUser = userService.findUserByEmail(userDto.getEmail());
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "There is already an account registered with the same email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }
        userService.saveUser(userDto);
        System.out.println("Register saved");
        return "redirect:/register?success";
    }

}
