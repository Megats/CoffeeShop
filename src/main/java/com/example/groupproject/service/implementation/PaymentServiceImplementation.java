package com.example.groupproject.service.implementation;

import com.example.groupproject.dto.paymentdto;
import com.example.groupproject.model.Payment;
import com.example.groupproject.repository.PaymentRepository;
import com.example.groupproject.service.paymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Service Class
@Service
public class PaymentServiceImplementation implements paymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentServiceImplementation(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<paymentdto> findAllPayment(){
        List<Payment> payments  = paymentRepository.findAll();
        return payments.stream().map(this::mapTopaymentdto).collect(Collectors.toList());
    }

    private paymentdto mapTopaymentdto(Payment payment){
        return paymentdto.builder()
                .payment_id(payment.getPayment_id())
                .order_id(payment.getOrder_id())
                .payment_total(payment.getPayment_total())
                .payment_status(payment.getPayment_status())
                .build();
    }
}
