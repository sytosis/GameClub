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
import com.example.gameclub.Ui.Authentication.AuthenticationFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button gameButton;
    Button new_friends;
    Button rightButton;
    Button homeBack;
    Button profileButton;
    private boolean game_page = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final MainActivity main = (MainActivity) requireActivity();
        final TextView homeText = root.findViewById(R.id.text_home);
        homeText.setText("Hi " + MainActivity.currentUser.getFirstName() + ", what would you like to do?");
        gameButton = root.findViewById(R.id.game_button);
        new_friends = root.findViewById(R.id.make_new_friends_button);
        new_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game_page) {
                    game_page = false;
                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_gallery);
                } else {
                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_make_friends);
                }
            }
        });
        rightButton = root.findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game_page) {
                    game_page = false;
                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_chess);
                } else {
                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_friendsListFragment);
                }
            }
        });
        profileButton = root.findViewById(R.id.profile_button);
        homeBack = root.findViewById(R.id.home_back);
        homeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        gameButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  game_page = true;
                  gameButton.setVisibility(View.INVISIBLE);
                  profileButton.setVisibility(View.INVISIBLE);
                  new_friends.setBackgroundResource(R.drawable.bingo_button);
                  rightButton.setBackgroundResource(R.drawable.chess_button);
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