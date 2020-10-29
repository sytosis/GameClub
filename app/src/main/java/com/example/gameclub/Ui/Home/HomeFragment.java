package com.example.gameclub.Ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.MainActivity;
import com.example.gameclub.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button gameButton;
    Button leftButton;
    Button rightButton;
    Button homeBack;
    Button profileButton;
    Boolean onBingoHome = false;
    Boolean onChessHome = false;
    Boolean onPlayHome = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final MainActivity main = (MainActivity) requireActivity();
        final TextView homeText = root.findViewById(R.id.text_home);
        homeText.setText("Hi " + MainActivity.currentUser.getFirstName() + ", what would you like to do?");
        gameButton = root.findViewById(R.id.game_button);
        leftButton = root.findViewById(R.id.make_new_friends_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onPlayHome) {
                    leftButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.invite_friends));
                    rightButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.random_people));
                    homeText.setText("How would you like to play Bingo?");
                    onPlayHome = false;
                    onBingoHome = true;
                } else if (onBingoHome) {
                    NavHostFragment.findNavController(HomeFragment.this).navigate((R.id.action_nav_home_to_nav_gallery));
                } else if (onChessHome) {
                    NavHostFragment.findNavController(HomeFragment.this).navigate((R.id.action_nav_home_to_nav_chess));
                } else {
                    NavHostFragment.findNavController(HomeFragment.this).navigate((R.id.action_nav_home_to_nav_make_friends));
                }
            }
        });
        rightButton = root.findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onPlayHome) {
                    leftButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.invite_friends));
                    rightButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.random_people));
                    homeText.setText("How would you like to play Chess?");
                    onPlayHome = false;
                    onChessHome = true;
                } else if (onBingoHome) {
                    NavHostFragment.findNavController(HomeFragment.this).navigate((R.id.action_nav_home_to_nav_gallery));
                } else if (onChessHome) {
                    NavHostFragment.findNavController(HomeFragment.this).navigate((R.id.action_nav_home_to_nav_chess));
                }
            }
        });
        profileButton = root.findViewById(R.id.profile_button);
        homeBack = root.findViewById(R.id.home_back);
        homeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onChessHome || onBingoHome) {
                    onChessHome = false;
                    onBingoHome = false;
                    leftButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.bingo_button));
                    rightButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.chess_button));
                    homeText.setText("What game would you like to play?");
                    onPlayHome = true;
                } else {
                    leftButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.make_friends_button));
                    rightButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.friends_and_messages_button));
                    ViewGroup.LayoutParams lp = leftButton.getLayoutParams();
                    lp.width = (int) (leftButton.getWidth() / 1.2);
                    profileButton.setVisibility(View.VISIBLE);
                    leftButton.setLayoutParams(lp);
                    rightButton.setLayoutParams(lp);
                    gameButton.setVisibility(View.VISIBLE);
                    homeBack.setVisibility(View.INVISIBLE);
                    homeText.setText("Hi " + MainActivity.currentUser.getFirstName() + ", what would you like to do?");
                    onPlayHome = false;
                }
            }
        });
        //chessButton = root.findViewById(R.id.chess_button);
        gameButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  leftButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.bingo_button));
                  rightButton.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.chess_button));
                  ViewGroup.LayoutParams lp = leftButton.getLayoutParams();
                  lp.width = (int) (leftButton.getWidth()*1.2);
                  leftButton.setLayoutParams(lp);
                  rightButton.setLayoutParams(lp);
                  profileButton.setVisibility(View.INVISIBLE);
                  gameButton.setVisibility(View.INVISIBLE);
                  homeBack.setVisibility(View.VISIBLE);
                  homeText.setText("What game would you like to play?");
                  onPlayHome = true;
              }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_profile);
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
        return root;
    }
}