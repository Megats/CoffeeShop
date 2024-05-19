package com.example.groupproject.controller;

import com.example.groupproject.dbconfig.DatabaseConfig;
import com.example.groupproject.dto.UserDto;
import com.example.groupproject.entity.User;
import com.example.groupproject.repository.UserRepository;
import com.example.groupproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class UserController {
    private final UserRepository userRepository;
    private UserService userService;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    //handler method to handle list of users
   //get list of users
    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users",users);
        return "./admin/userlist";
    }

    @RequestMapping(value="/usersDelete/{id}", method = RequestMethod.GET)
    public String handleUserDelete(@PathVariable String id){
        System.out.println(id);
        System.out.println("delete user");
        return "redirect:/users";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal){
        //get the logged-in user details
        String email = principal.getName();
        User user = userService.findUserByEmail(email);
        model.addAttribute("user", user);
        return "profile";
    }
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute UserDto userDto){
        //save updated user details
        userService.saveUser(userDto);
        return "redirect:/profile";
    }





}
