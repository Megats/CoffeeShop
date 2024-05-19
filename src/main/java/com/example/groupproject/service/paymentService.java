package com.example.groupproject.service;

import com.example.groupproject.dto.paymentdto;
import com.example.groupproject.model.Payment;

import java.util.List;

public interface paymentService {
    List<paymentdto> findAllPayment();
}
