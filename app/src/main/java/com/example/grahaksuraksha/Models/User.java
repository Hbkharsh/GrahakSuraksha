package com.example.grahaksuraksha.Models;

import androidx.annotation.NonNull;

public class User {
    private String _id;
    String name ;
    String email;
    String password;
    String confirmPassword;

    public User(String name, String email, String password,String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword=confirmPassword;
    }

    public String getUserId() {
        return _id;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
