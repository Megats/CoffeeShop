//package com.example.groupproject.controller;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//
//public class UserController {
//    @Autowired
//    private wJdbcTemplate jdbcTemplate;
//
//    @GetMapping("/users")
//    public List<Map<String, Object>> getUsers() {
//        return jdbcTemplate.queryForList("SELECT * FROM customer");
//    }
//
//}
