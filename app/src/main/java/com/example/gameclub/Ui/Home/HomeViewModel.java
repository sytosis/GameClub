package com.example.gameclub.Ui.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private boolean authenticated;
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public void toggleAuthenticated() {
        authenticated = !authenticated;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
    public LiveData<String> getText() {
        return mText;
    }
}