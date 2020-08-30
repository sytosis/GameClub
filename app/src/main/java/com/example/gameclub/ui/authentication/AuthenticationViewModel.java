package com.example.gameclub.ui.authentication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

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

    public String login(String username, String password) {
        String testAccount = accounts.getString(username,"UnknownAccount");
        if (!password.equals(testAccount)) {
            return null;
        } else {
            return username;
        }
    }

    public String register(String username, String password) {
        //probably implement a better check but then again you dont have to
        String testAccount = accounts.getString(username,"UnknownAccount");
        if (testAccount.equals("UnknownAccount")) {
            accountsEditor.putString(username,password);
            accountsEditor.apply();
            return username;
        } else {
            return null;
        }
    }
}
