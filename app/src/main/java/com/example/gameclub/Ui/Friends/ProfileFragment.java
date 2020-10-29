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

public class ProfileFragment extends Fragment {

    private static View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        final Button homeButton = root.findViewById(R.id.back_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(ProfileFragment.this).navigate((R.id.action_nav_profile_to_home));
            }
        });
        ((TextView) root.findViewById(R.id.first_name_text)).setText(MainActivity.currentUser.getFirstName());
        ((TextView) root.findViewById(R.id.last_name_text)).setText(MainActivity.currentUser.getLastName());
        ((TextView) root.findViewById(R.id.email_text)).setText(MainActivity.currentUser.getEmail());
        ((TextView) root.findViewById(R.id.interests_text)).setText(MainActivity.currentUser.getInterest());
        ((TextView) root.findViewById(R.id.country_text)).setText(MainActivity.currentUser.getCountry());

        return root;
    }
}
