package com.example.groupproject.model;

import java.sql.Date;

public class Review {
    private int idReview;
    private String custName;
    private int idPayment;
    private String descReview;
    private Date dateReview;
    private int ratingValue;

    public Review(int idReview, String custName, String descReview, Date dateReview, int ratingValue) {
        this.idReview = idReview;
        this.custName = custName;
        this.descReview = descReview;
        this.dateReview = dateReview;
        this.ratingValue = ratingValue;
    }

    public Review( int idPayment, String descReview, int ratingValue, Date dateReview) {
        this.idPayment = idPayment;
        this.descReview = descReview;
        this.ratingValue = ratingValue;
        this.dateReview = dateReview;
    }

    public Review( int idPayment, String descReview, int ratingValue) {
        this.idPayment = idPayment;
        this.descReview = descReview;
        this.ratingValue = ratingValue;
    }

    public  Review(){}


    //getter
    public int getIdReview() {
        return idReview;
    }

    public String getCustName() {
        return custName;
    }

    public String getDescReview() {
        return descReview;
    }

    public Date getDateReview() {
        return dateReview;
    }

    public int getIdPayment() { return idPayment;}

    public int getRatingValue() { return ratingValue;}

    //setter

    public void setIdReview(int idReview) {
        this.idReview = idReview;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setDescReview(String descReview) {
        this.descReview = descReview;
    }

    public void setDateReview(Date dateReview) {
        this.dateReview = dateReview;
    }

    public void setRatingValue(int ratingValue) { this.ratingValue = ratingValue;}


}
