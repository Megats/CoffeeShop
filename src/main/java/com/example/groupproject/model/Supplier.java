package com.example.groupproject.model;

public class Supplier {
    private String supplierName;
    private String supplierAddress;
    private String supplierPhone;
    private String supplierEmail;

    // constructor
    public Supplier(String supplierName, String supplierAddress, String supplierPhone, String supplierEmail)
    {
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
        this.supplierPhone = supplierPhone;
        this.supplierEmail = supplierEmail;
    }

    // all data getter
    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

}
