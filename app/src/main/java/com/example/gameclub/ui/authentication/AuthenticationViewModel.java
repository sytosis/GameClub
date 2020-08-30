package com.example.gameclub.ui.authentication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class AuthenticationViewModel extends ViewModel {
    SharedPreferences accounts;
    SharedPreferences.Editor accountsEditor;

    public AuthenticationViewModel() {
    }


    @SuppressLint("CommitPrefEdits")
    public void setSharedPreferences(SharedPreferences sp) {
        accounts = sp;
        accountsEditor = accounts.edit();
    }


    public Set<String> login(String email, String password) {
        List<String> userDetails = new ArrayList<>();
        try {
            userDetails = new ArrayList<>(Objects.requireNonNull(accounts.getStringSet(email, null)));
        } catch (NullPointerException e) {
            return null;
        }

        if (userDetails.size() != 0) {
            if (!password.equals(userDetails.get(0))) {
                return null;
            } else {
                return accounts.getStringSet(email,null);
            }
        } else {
            return null;
        }

    }

    public Set<String> register(String email, String password, String firstName, String lastName, String country, String interests) {
        //probably implement a better check but then again you dont have to
        if (accounts.getStringSet(email, new HashSet<String>()).equals(new HashSet<String>())) {
            Set<String> set = new HashSet<>(Arrays.asList(password,firstName,lastName,country,interests));
            accountsEditor.putStringSet(email,set);
            accountsEditor.apply();
            return accounts.getStringSet(email,null);
        } else {
            return null;
        }
    }
}
