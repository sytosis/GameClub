package com.example.gameclub.Users;

public class User {

    private String id, email, password, firstName, lastName, country, interest;

    public User(String userId, String e, String pass, String first, String last, String c, String i) {
        id = userId;
        email = e;
        password = pass;
        firstName = first;
        lastName = last;
        country = c;
        interest = i;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }

    public String getInterest() {
        return interest;
    }
}