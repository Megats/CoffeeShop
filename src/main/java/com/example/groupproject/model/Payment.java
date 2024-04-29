package com.example.groupproject.model;

public class Payment {
    private int id;
    private int groupId;
    private int paymentId;
    private String paymentDate;
    private String paymentType;
    private String paymentStatus;

    //contructor
    public Payment(int id, int groupId, int paymentId, String paymentDate, String paymentType, String paymentStatus)
    {
        this.id = id;
        this.groupId = groupId;
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
    }

    //getter
    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


}
