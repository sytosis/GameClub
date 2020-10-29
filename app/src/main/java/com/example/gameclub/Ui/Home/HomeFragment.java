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
    private Button gameButton;
    private Button new_friends;
    private Button rightButton;
    private Button homeBack;
    private Button profileButton;
    private Button backButton;
    private boolean game_page = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final MainActivity main = (MainActivity) requireActivity();
        final TextView homeText = root.findViewById(R.id.text_home);
        homeText.setText("Hi " + MainActivity.currentUser.getFirstName() + ", what would you like to do?");

        backButton = root.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game_page = false;
                gameButton.setVisibility(View.VISIBLE);
                profileButton.setVisibility(View.VISIBLE);
                new_friends.setBackgroundResource(R.drawable.make_friends_button);
                rightButton.setBackgroundResource(R.drawable.friends_and_messages_button);
                backButton.setVisibility(View.INVISIBLE);
            }
        });

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
                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_friendsListFragment);
                } else {
                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_friendsListFragment);
                }
            }
        });

        gameButton = root.findViewById(R.id.game_button);
        gameButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  game_page = true;
                  backButton.setVisibility(View.VISIBLE);
                  gameButton.setVisibility(View.INVISIBLE);
                  profileButton.setVisibility(View.INVISIBLE);
                  new_friends.setBackgroundResource(R.drawable.bingo_button);
                  rightButton.setBackgroundResource(R.drawable.chess_button);
              }
        });

        profileButton = root.findViewById(R.id.profile_button);
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