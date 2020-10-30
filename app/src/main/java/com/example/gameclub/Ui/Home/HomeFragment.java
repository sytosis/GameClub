package com.example.gameclub.Ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.gameclub.MainActivity;
import com.example.gameclub.R;

/**
 * Home fragment one a user has logged in this page controls the users next actions
 * @author John Roby John
 * @author Kevin Chua
 * @author Jordan Ng
 * @author Robert Francis
 */
public class HomeFragment extends Fragment {

    //Variables that contain the buttons used within the XML for referral
    private Button gameButton;
    private Button new_friends;
    private Button rightButton;
    private Button profileButton;
    private Button backButton;
    private boolean game_page = false;

    public HomeFragment() {

    }

    //runs the moment the page loads
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final MainActivity main = (MainActivity) requireActivity();
        final TextView homeText = root.findViewById(R.id.text_home);
        //sets the main text of the home screen
        homeText.setText("Hi " + MainActivity.currentUser.getFirstName() + ", what would you like to do?");

        //changes the layout of the home screen depending on which page the user is currently on
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
                ViewGroup.LayoutParams lp = new_friends.getLayoutParams();
                lp.width = (int) (new_friends.getWidth()/1.2);
                new_friends.setLayoutParams(lp);
                rightButton.setLayoutParams(lp);
            }
        });

        //loads either bingo or the make friends depending on the page
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

        //loads chess or friends list depending on the page the user is on
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

        //changes the layout of the home screen depending on which page the user is currently on
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
                  ViewGroup.LayoutParams lp = new_friends.getLayoutParams();
                  lp.width = (int) (new_friends.getWidth()*1.2);
                  new_friends.setLayoutParams(lp);
                  rightButton.setLayoutParams(lp);
              }
        });

        //loads the profile
        profileButton = root.findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_profile);
            }
        });

        return root;
    }
}