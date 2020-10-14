package com.example.gameclub.ui.authentication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.example.gameclub.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class AuthenticationViewModel extends ViewModel {
    SharedPreferences accounts;
    SharedPreferences.Editor accountsEditor;
    MainActivity ma;

    public AuthenticationViewModel() {

    }

    public void setMainActivity(MainActivity ma) {
        this.ma = ma;
    }


    public String[] login(String email, String password) {
        String userDetails[];
        String realPassword;
        String firstName;
        String lastName;
        String country;
        String interests;
        try {
            userDetails = accounts.getString(email, null).split(";");
            realPassword = userDetails[0];
            firstName = userDetails[1];
            lastName = userDetails[2];
            country = userDetails[3];
            interests = userDetails[4];
            System.out.println(userDetails);
        } catch (NullPointerException e) {
            return null;
        }
        if (!password.equals(realPassword)) {
            return null;
        } else {
            System.out.println(userDetails);
            ma.setEmail(email);
            ma.setFirstName(firstName);
            ma.setLastName(lastName);
            ma.setCountry(country);
            ma.setInterests(interests);
            return userDetails;
        }



    }
}
