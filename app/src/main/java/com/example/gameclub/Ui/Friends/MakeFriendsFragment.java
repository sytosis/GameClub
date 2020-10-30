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

/**
 * Make friends Fragments allows users to add new friends
 * @author John Roby John
 * @author Kevin Chua
 * @author Jordan Ng
 * @author Robert Francis
 */
public class MakeFriendsFragment extends Fragment {

    private Button homeButton;
    private Button jack;
    private Button rose;
    private Button jason;
    private Button sam;
    private Button sue;
    private Button clay;

    public MakeFriendsFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.make_new_friends, container, false);
        homeButton = root.findViewById(R.id.back_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(MakeFriendsFragment.this).navigate((R.id.action_nav_make_friends_to_nav_home));
            }
        });
        // Add friend to user if clicked on

        jack = root.findViewById(R.id.jack_button);
        jack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.currentUser.addFriend("jack");
            }
        });
        rose = root.findViewById(R.id.rose_button);
        rose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.currentUser.addFriend("rose");
            }
        });
        jason = root.findViewById(R.id.jason_button);
        jason.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.currentUser.addFriend("jason");
            }
        });
        sam = root.findViewById(R.id.sam_button);
        sam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.currentUser.addFriend("sam");
            }
        });
        sue = root.findViewById(R.id.sue_button);
        sue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.currentUser.addFriend("sue");
            }
        });
        clay = root.findViewById(R.id.clay_button);
        clay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.currentUser.addFriend("clay");
            }
        });

        return root;
    }
}
