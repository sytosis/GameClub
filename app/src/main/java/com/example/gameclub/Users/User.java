package com.example.gameclub.Users;

import android.widget.ArrayAdapter;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String id, email, password, firstName, lastName, country, interest, friendList;

    public User(String userId, String e, String pass, String first, String last, String c, String i, String friends) {
        id = userId;
        email = e;
        password = pass;
        firstName = first;
        lastName = last;
        country = c;
        interest = i;
        friendList = friends;
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

    public void setEmail(String email) {
        mDatabase.child("users").child(id).child("email").setValue(email);
        this.email = email;
    }

    public void setFirst(String first) {
        mDatabase.child("users").child(id).child("firstName").setValue(first);
        this.firstName = first;
    }

    public void setLast(String last) {
        mDatabase.child("users").child(id).child("lastName").setValue(last);
        this.lastName = last;
    }

    public void setCountry(String country) {
        mDatabase.child("users").child(id).child("country").setValue(country);
        this.country = country;
    }

    public void setInterest(String interest) {
        mDatabase.child("users").child(id).child("interest").setValue(interest);
        this.interest = interest;
    }

    public List<String> getFriendList() {
        List<String> tempList = new ArrayList<>();
        String[] str = friendList.split(",");
        tempList = Arrays.asList(str);
        return tempList;
    }

    public void addFriend(String email) {
        friendList = friendList + email + ",";
        mDatabase.child("users").child(id).child("friends").setValue(friendList);
    }
}