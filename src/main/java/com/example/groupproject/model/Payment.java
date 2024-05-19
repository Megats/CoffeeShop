package com.example.groupproject.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@NoArgsConstructor
//@Builder
@Entity
@Table(name = "payment")
public class Payment {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //declare variables
    private int payment_id;
    @Setter
    private int order_id;
    @Setter
    private String payment_status;
    private double payment_total;
    @Setter
    private Date payment_date;
    private String payment_method;

    //constructor
    public Payment(int payment_id, int order_id, double total, String status, String payment_method) {
        this.payment_id = payment_id;
        this.order_id = order_id;
        this.payment_status = status;
        this.payment_total = total;
        this.payment_method = payment_method;
    }

    public Payment(int order_id, double total, String status, String payment_method) {
        this.order_id = order_id;
        this.payment_status = status;
        this.payment_total = total;
        this.payment_method = payment_method;
    }

    public Payment(int payment_id,int order_id, String payment_status, double payment_total) {
        this.payment_id = payment_id;
        this.order_id = order_id;
        this.payment_status = payment_status;
        this.payment_total = payment_total;
    }

    public Payment(int payment_id, int order_id, String payment_status, double payment_total, Date payment_date, String payment_method) {
        this.payment_id = payment_id;
        this.order_id = order_id;
        this.payment_status = payment_status;
        this.payment_total = payment_total;
        this.payment_date = payment_date;
        this.payment_method = payment_method;
    }


    //getters and setters
    public int getPayment_id() {
        return payment_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public double getPayment_total() {
        return payment_total;
    }
    public void setPayment_total(int payment_total) {
        this.payment_total = payment_total;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void add(Payment payment) {
    }
}
