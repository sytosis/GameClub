package com.example.gameclub.Ui.Friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.MainActivity;
import com.example.gameclub.R;
import com.example.gameclub.Ui.Authentication.AuthenticationFragment;
import com.example.gameclub.Users.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

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
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public MakeFriendsFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.make_new_friends, container, false);
        final EditText editText = root.findViewById(R.id.editText);
        editText.setHint("Type Here...");
        editText.setText("");
        Button search = root.findViewById(R.id.button2);
        homeButton = root.findViewById(R.id.back_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(MakeFriendsFragment.this).navigate((R.id.action_nav_make_friends_to_nav_home));
            }
        });
        final TextView results = root.findViewById(R.id.result);
        results.setVisibility(View.INVISIBLE);

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

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<User> users = AuthenticationFragment.existingUsers;
                String email = editText.getText().toString();
                results.setText("User doesn't exist");
                results.setVisibility(View.VISIBLE);
                for (int i = 0; i < users.size(); ++i) {
                    if (users.get(i).getEmail().equals(email)) {
                        MainActivity.currentUser.addFriend(email);
                        results.setText("Friend Added");
                        results.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return root;
    }
}
