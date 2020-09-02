package com.example.gameclub.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.MainActivity;
import com.example.gameclub.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button gameButton;
    Button friendButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        gameButton = root.findViewById(R.id.game_button);
        friendButton = root.findViewById(R.id.friend_button);
        final MainActivity main = (MainActivity) requireActivity();
        main.disableNav(true);
        final HomeFragment framgent = this;
        gameButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  main.disableNav(false);
                  NavHostFragment.findNavController(HomeFragment.this).navigate((R.id.action_nav_home_to_nav_gallery));
              }
        });
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}