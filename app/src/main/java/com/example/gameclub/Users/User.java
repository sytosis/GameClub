package com.example.gameclub.Users;

public class User {

    private String id, email, password;

    public User(String userId, String e, String pass) {
        id = userId;
        email = e;
        password = pass;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}