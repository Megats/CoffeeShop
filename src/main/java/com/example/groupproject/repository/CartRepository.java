package com.example.groupproject.repository;

import com.example.groupproject.model.Cart;
import com.example.groupproject.model.Order;
import com.example.groupproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// CartRepository.java
@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {


}
