package com.example.gameclub.Ui.Friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.Games.Bingo;
import com.example.gameclub.Games.BingoFragment;
import com.example.gameclub.MainActivity;
import com.example.gameclub.R;

public class MakeFriendsFragment extends Fragment {

    static View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.make_new_friends, container, false);

        Button homeButton = root.findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(MakeFriendsFragment.this).navigate((R.id.action_nav_bingo_to_home));
            }
        });


        return root;
    }
}
