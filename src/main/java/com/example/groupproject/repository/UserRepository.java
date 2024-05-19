package com.example.groupproject.repository;

import com.example.groupproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository <User, Long> {

    User findByEmail(String email);

}
