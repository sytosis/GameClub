package com.example.gameclub.Ui.Friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.MainActivity;
import com.example.gameclub.R;
import com.example.gameclub.Users.ProfileFragment;
import com.example.gameclub.Users.User;

public class OtherProfileFragment extends Fragment {

    private static View root;
    public Button edit;
    private Boolean profile = true;
    private ImageView profilePic;

    public OtherProfileFragment() {

    }

    /**
     * Display the current user's details
     */
    public void printPLayerInfo(User user) {
        ((TextView) root.findViewById(R.id.first_name_text)).setText(user.getFirstName());
        ((TextView) root.findViewById(R.id.last_name_text)).setText(user.getLastName());
        ((TextView) root.findViewById(R.id.email_text)).setText(user.getEmail());
        ((TextView) root.findViewById(R.id.interests_text)).setText(user.getInterest());
        ((TextView) root.findViewById(R.id.country_text)).setText(user.getCountry());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profile_other, container, false);
        edit = root.findViewById(R.id.edit_profile_button);
        profilePic = root.findViewById(R.id.profile_photo);
        final Button homeButton = root.findViewById(R.id.back_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(OtherProfileFragment.this).navigate((R.id.action_OtherProfileFragment_to_friendsListFragment));
            }
        });

        final EditText email = root.findViewById(R.id.edit_email);
        final EditText name = root.findViewById(R.id.edit_name);
        final EditText surname = root.findViewById(R.id.edit_surname);
        final EditText country = root.findViewById(R.id.edit_country);
        final EditText interest = root.findViewById(R.id.edit_interest);

        // Display user details
        if (FriendsListFragment.userNum == 1) {
            printPLayerInfo(FriendsListFragment.user1);
            profilePic.setBackgroundResource(getPic(FriendsListFragment.user1.getFirstName()));
        }
        else if (FriendsListFragment.userNum == 2) {
            printPLayerInfo(FriendsListFragment.user2);
            profilePic.setBackgroundResource(getPic(FriendsListFragment.user2.getFirstName()));
        }
        else if (FriendsListFragment.userNum == 3) {
            printPLayerInfo(FriendsListFragment.user3);
            profilePic.setBackgroundResource(getPic(FriendsListFragment.user3.getFirstName()));
        }
        else if (FriendsListFragment.userNum == 4) {
            printPLayerInfo(FriendsListFragment.user4);
            profilePic.setBackgroundResource(getPic(FriendsListFragment.user4.getFirstName()));
        }


        return root;
    }

    /**
     * Returns user picture id
     */
    public int getPic(String name) {
        if (name.equals("jason")) {
            return R.drawable.jason_photo;
        } else if (name.equals("jack")) {
            return R.drawable.jack_photo;
        } else if (name.equals("rose")) {
            return R.drawable.rose_photo;
        } else if (name.equals("sam")) {
            return R.drawable.jay_photo;
        } else if (name.equals("sue")) {
            return R.drawable.sue_photo;
        } else if (name.equals("clay")) {
            return R.drawable.clay_photo;
        }
        return R.drawable.jason_photo;
    }
}
