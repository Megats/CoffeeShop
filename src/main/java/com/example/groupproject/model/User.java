package com.example.groupproject.model;

public class User {
    private int id;
    private String name;
    private String phonenum;
    private String email;
    private String password;

    private User(int id, String name, String phonenum, String email, String password) {
        this.id = id;
        this.name = name;
        this.phonenum = phonenum;
        this.email = email;
        this.password = password;
    }

    //Getter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
