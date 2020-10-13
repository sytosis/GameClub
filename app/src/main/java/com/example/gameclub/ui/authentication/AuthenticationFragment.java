package com.example.gameclub.ui.authentication;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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

import com.example.gameclub.MainActivity;
import com.example.gameclub.R;
import com.example.gameclub.Users.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AuthenticationFragment extends Fragment {
    private AuthenticationViewModel authenticationViewModel;
    private Integer pageNumber = 1;
    SharedPreferences sharedPreferences;
    Button registerButton;
    Button loginButton;
    Button nextButton;
    Button backButton;
    Button continueButton;
    TextView topText;
    TextView bottomText;
    TextView orText;
    EditText topEditText;
    EditText bottomEditText;
    String registerEmail;
    String registerPassword;
    String registerFirstName;
    String registerLastName;
    String registerCountry;
    String registerInterests;
    View view;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    List<User> userList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        authenticationViewModel =
                ViewModelProviders.of(this).get(AuthenticationViewModel.class);
        sharedPreferences = requireActivity().getSharedPreferences("accounts", MODE_PRIVATE);
        //getContext().getSharedPreferences("accounts",0).edit().clear().commit();
        authenticationViewModel.setSharedPreferences(sharedPreferences);
        authenticationViewModel.setMainActivity((MainActivity) getActivity());
        View root = inflater.inflate(R.layout.login_signup, container, false);
        registerButton = root.findViewById(R.id.register_button);
        loginButton = root.findViewById(R.id.login_button);
        nextButton = root.findViewById(R.id.next_button);
        backButton = root.findViewById(R.id.back_button);
        continueButton = root.findViewById(R.id.continue_button);
        topEditText = root.findViewById(R.id.topText);
        bottomEditText = root.findViewById(R.id.bottomText);
        topText = root.findViewById(R.id.authenticationTextTop);
        bottomText = root.findViewById(R.id.authenticationTextBottom);
        view = root.findViewById(R.id.layout);
        orText = root.findViewById(R.id.orText);
        final MainActivity ma = (MainActivity) requireActivity();
        ma.disableNav(true);
        final AuthenticationFragment fragment = this;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNumber = 2;
                fragment.refreshPageLayout();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNumber = 9;
                fragment.refreshPageLayout();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement checks later
                if (pageNumber == 2) {
                    registerFirstName = topEditText.getText().toString();
                    if (registerFirstName.equals("")) {
                        pageNumber = pageNumber - 1;
                        System.out.println("INVALID");
                    }

                    if (registerFirstName.contains(";")) {
                        pageNumber = pageNumber - 1;
                        System.out.println("NO ; ALLOWED");
                    }
                } else if (pageNumber == 3) {
                    registerLastName = topEditText.getText().toString();
                    if (registerLastName.equals("")) {
                        pageNumber = pageNumber - 1;
                        System.out.println("INVALID");
                    }
                    if (registerLastName.contains(";")) {
                        pageNumber = pageNumber - 1;
                        System.out.println("NO ; ALLOWED");
                    }
                } else if (pageNumber == 4) {
                    registerCountry = topEditText.getText().toString();
                    if (registerCountry.equals("")) {
                        pageNumber = pageNumber - 1;
                        System.out.println("INVALID");
                    }
                    if (registerCountry.contains(";")) {
                        pageNumber = pageNumber - 1;
                        System.out.println("NO ; ALLOWED");
                    }
                } else if (pageNumber == 5) {
                    registerInterests = topEditText.getText().toString();
                    if (registerInterests.equals("")) {
                        pageNumber = pageNumber - 1;
                        System.out.println("INVALID");
                    }
                    if (registerInterests.contains(";")) {
                        pageNumber = pageNumber - 1;
                        System.out.println("NO ; ALLOWED");
                    }
                } else if (pageNumber == 6) {
                    registerEmail = topEditText.getText().toString();
                    System.out.println(registerEmail);
                    System.out.println(authenticationViewModel.checkEmailUsed(registerEmail));
                    if (authenticationViewModel.checkEmailUsed(registerEmail)) {
                        pageNumber = pageNumber - 1;
                        System.out.println("EMAIL USED");
                    }
                    if (registerEmail.contains(";")) {
                        pageNumber = pageNumber - 1;
                        System.out.println("NO ; ALLOWED");
                    }
                } else if (pageNumber == 7) {
                    registerPassword = topEditText.getText().toString();
                    if (registerPassword.equals("")) {
                        pageNumber = pageNumber - 1;
                        System.out.println("INVALID");
                    }
                    if (registerPassword.contains(";")) {
                        pageNumber = pageNumber - 1;
                        System.out.println("NO ; ALLOWED");
                    }
                } else if (pageNumber == 9) {
                    if (authenticationViewModel.login(topEditText.getText().toString(),bottomEditText.getText().toString()) != null) {
                        //display registered and go to home page
                        NavHostFragment.findNavController(AuthenticationFragment.this).navigate(R.id.action_AuthenticationFragment_to_HomeFragment);
                        System.out.println("Logged in");
                    } else {
                        //display login failed, account already exists or account doesnt exist
                        System.out.println("Incorrect combination");
                    }
                }
                if (pageNumber != 9) {
                    pageNumber = pageNumber + 1;
                    fragment.refreshPageLayout();
                }

            }

        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageNumber == 9) {
                    pageNumber = 1;
                    fragment.refreshPageLayout();
                } else {
                    pageNumber = pageNumber - 1;
                    fragment.refreshPageLayout();
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (authenticationViewModel.register(registerEmail,registerPassword,registerFirstName,registerLastName,registerCountry,registerInterests) != null) {
                    //Check user does not already exist

                    //Add new user to database
                    writeNewUser("0", registerEmail, registerPassword);

                    //display registered and go to home page
                    NavHostFragment.findNavController(AuthenticationFragment.this).navigate(R.id.action_AuthenticationFragment_to_HomeFragment);
                    System.out.println("registered");
                } else {
                    //display failed to register, username exists
                    System.out.println("register failed");
                }
            }
        });
        refreshPageLayout();
        return root;
    }

    /*
     *  Creates new user in firebase database
     */
    public void writeNewUser(String userId, String email, String password) {
        mDatabase.child("users").child(userId).child("email").setValue(email);
        mDatabase.child("users").child(userId).child("password").setValue(password);
        Log.d("writeNewUser", userId);
    }

    public void refreshPageLayout() {
        topEditText.getText().clear();
        bottomEditText.getText().clear();
        if (pageNumber == 1) {
            topText.setVisibility(View.VISIBLE);
            bottomText.setVisibility(View.VISIBLE);
            topText.setTypeface(Typeface.DEFAULT_BOLD);
            topText.setTextSize(70);
            bottomText.setTypeface(Typeface.DEFAULT_BOLD);
            bottomText.setTextColor(Color.WHITE);
            topText.setTextColor(Color.WHITE);
            view.setBackgroundResource(R.drawable.backgroundimghome);
            topText.setText(" GameClub");
            bottomText.setText("Let's start by setting up your account");
            registerButton.setVisibility(View.VISIBLE);
            registerButton.setText("I'm new");
            loginButton.setText("I have an account");
            loginButton.setVisibility(View.VISIBLE);
            orText.setVisibility(View.VISIBLE);
            orText.setTypeface(Typeface.DEFAULT_BOLD);
            loginButton.setTypeface(Typeface.DEFAULT_BOLD);
            registerButton.setTypeface(Typeface.DEFAULT_BOLD);
            loginButton.setTextColor(Color.WHITE);
            registerButton.setTextColor(Color.WHITE);
            orText.setTextColor(Color.WHITE);
            topEditText.setVisibility(View.GONE);
            bottomEditText.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            continueButton.setVisibility(View.GONE);
        } else if (pageNumber == 2) {
            view.setBackgroundResource(R.drawable.wood_background);
            bottomText.setText("What's your first name?");
            topEditText.setHint("Type your first name here");
            bottomText.setTextColor(Color.BLACK);
            bottomText.setTextSize(50);
            orText.setVisibility(View.GONE);
            topText.setVisibility(View.GONE);
            registerButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            topEditText.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        } else if (pageNumber == 3) {
            bottomText.setText("What's your family name?");
            topEditText.setHint("Type your surname here");
        } else if (pageNumber == 4) {
            bottomText.setText("What country do you live in?");
            topEditText.setHint("Type your country here");
        } else if (pageNumber == 5) {
            bottomText.setText("What are you interested in?");
            topEditText.setHint("Food, animals etc...");
        } else if (pageNumber == 6) {
            bottomText.setText("What's your email?");
            topEditText.setHint("Type your email here");
        } else if (pageNumber == 7) {
            bottomText.setText("Please set up your password");
            topEditText.setHint("Type your new password here");
        } else if (pageNumber == 8) {
            bottomText.setText("You're all set!");
            topEditText.setVisibility(View.GONE);
            continueButton.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            continueButton.setVisibility(View.VISIBLE);
        } else if (pageNumber == 9) {
            bottomText.setText("Please log in");
            topEditText.setHint("Email here");
            bottomEditText.setHint("Password here");
            nextButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
            bottomText.setVisibility(View.VISIBLE);
            topEditText.setVisibility(View.VISIBLE);
            bottomEditText.setVisibility(View.VISIBLE);
            topText.setVisibility(View.GONE);
            registerButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            orText.setVisibility(View.GONE);
        }
    }
}
