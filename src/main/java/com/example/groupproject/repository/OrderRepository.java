package com.example.groupproject.repository;

import com.example.groupproject.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// OrderRepository.java
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // You can add custom query methods here if needed
}
