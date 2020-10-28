package com.example.gameclub.Ui.Friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.gameclub.Games.Bingo;
import com.example.gameclub.R;

public class MakeFriendsFragment extends Fragment {

    static View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.make_new_friends, container, false);

        Button backButton = root.findViewById(R.id.back_button);



        return root;
    }
}
