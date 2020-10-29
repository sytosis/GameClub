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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.Games.Bingo;
import com.example.gameclub.Games.BingoFragment;
import com.example.gameclub.MainActivity;
import com.example.gameclub.R;

public class ProfileFragment extends Fragment {

    private static View root;
    public Button edit;
    private Boolean profile = true;

    public void printPLayerInfo() {
        ((TextView) root.findViewById(R.id.first_name_text)).setText(MainActivity.currentUser.getFirstName());
        ((TextView) root.findViewById(R.id.last_name_text)).setText(MainActivity.currentUser.getLastName());
        ((TextView) root.findViewById(R.id.email_text)).setText(MainActivity.currentUser.getEmail());
        ((TextView) root.findViewById(R.id.interests_text)).setText(MainActivity.currentUser.getInterest());
        ((TextView) root.findViewById(R.id.country_text)).setText(MainActivity.currentUser.getCountry());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profile, container, false);
        edit = root.findViewById(R.id.edit_profile_button);
        final Button homeButton = root.findViewById(R.id.back_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(ProfileFragment.this).navigate((R.id.action_nav_profile_to_home));
            }
        });
        final EditText email = root.findViewById(R.id.edit_email);
        final EditText name = root.findViewById(R.id.edit_name);
        final EditText surname = root.findViewById(R.id.edit_surname);
        final EditText country = root.findViewById(R.id.edit_country);
        final EditText interest = root.findViewById(R.id.edit_interest);

        printPLayerInfo();


        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (profile) {
                    ((TextView) root.findViewById(R.id.first_name_text)).setVisibility(View.INVISIBLE);
                    ((TextView) root.findViewById(R.id.last_name_text)).setVisibility(View.INVISIBLE);
                    ((TextView) root.findViewById(R.id.email_text)).setVisibility(View.INVISIBLE);
                    ((TextView) root.findViewById(R.id.interests_text)).setVisibility(View.INVISIBLE);
                    ((TextView) root.findViewById(R.id.country_text)).setVisibility(View.INVISIBLE);

                    email.setVisibility(View.VISIBLE);
                    email.setHint("New Email");
                    email.setText(MainActivity.currentUser.getEmail());

                    name.setVisibility(View.VISIBLE);
                    name.setHint("New First Name");
                    name.setText(MainActivity.currentUser.getFirstName());

                    surname.setVisibility(View.VISIBLE);
                    surname.setHint("New Surname");
                    surname.setText(MainActivity.currentUser.getLastName());

                    country.setVisibility(View.VISIBLE);
                    country.setHint("New Country");
                    country.setText(MainActivity.currentUser.getCountry());

                    interest.setVisibility(View.VISIBLE);
                    interest.setHint("New Interest");
                    interest.setText(MainActivity.currentUser.getInterest());
                    String edit_text = "Make Changes";
                    edit.setText(edit_text);

                    profile = false;
                } else {
                    if (!email.getText().toString().equals("")) {
                        MainActivity.currentUser.setEmail(email.getText().toString());
                    }
                    if (!surname.getText().toString().equals("")) {
                        MainActivity.currentUser.setLast(surname.getText().toString());
                    }
                    if (!name.getText().toString().equals("")) {
                        MainActivity.currentUser.setFirst(name.getText().toString());
                    }
                    if (!country.getText().toString().equals("")) {
                        MainActivity.currentUser.setCountry(country.getText().toString());
                    }
                    if (!interest.getText().toString().equals("")) {
                        MainActivity.currentUser.setInterest(interest.getText().toString());
                    }

                    ((TextView) root.findViewById(R.id.first_name_text)).setVisibility(View.VISIBLE);
                    ((TextView) root.findViewById(R.id.last_name_text)).setVisibility(View.VISIBLE);
                    ((TextView) root.findViewById(R.id.email_text)).setVisibility(View.VISIBLE);
                    ((TextView) root.findViewById(R.id.interests_text)).setVisibility(View.VISIBLE);
                    ((TextView) root.findViewById(R.id.country_text)).setVisibility(View.VISIBLE);

                    printPLayerInfo();

                    email.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                    surname.setVisibility(View.INVISIBLE);
                    country.setVisibility(View.INVISIBLE);
                    interest.setVisibility(View.INVISIBLE);

                    String edit_text = "Edit Profile";
                    edit.setText(edit_text);

                    profile = true;
                }
            }
        });

        return root;
    }
}
