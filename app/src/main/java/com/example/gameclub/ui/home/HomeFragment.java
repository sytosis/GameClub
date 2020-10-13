package com.example.gameclub.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.MainActivity;
import com.example.gameclub.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button gameButton;
    Button leftButton;
    Button rightButton;
    Button profileButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView homeText= root.findViewById(R.id.text_home);
        gameButton = root.findViewById(R.id.game_button);
        leftButton = root.findViewById(R.id.left_button);
        rightButton = root.findViewById(R.id.right_button);
        profileButton = root.findViewById(R.id.profile_button);
        //chessButton = root.findViewById(R.id.chess_button);
        final MainActivity main = (MainActivity) requireActivity();
        gameButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  leftButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.bingo_button));
                  rightButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.chess_button));
                  profileButton.setVisibility(View.INVISIBLE);
                  gameButton.setVisibility(View.INVISIBLE);
              }
        });
        /*
        chessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this).navigate((R.id.action_nav_home_to_nav_chess));
            }
        });
         */
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                homeText.setText("Hi " + main.getFirstName() + ", what would you like to do?");
            }
        });
        return root;
    }
}