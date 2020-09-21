package com.example.gameclub.ui.authentication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.MainActivity;
import com.example.gameclub.R;
import com.example.gameclub.ui.gallery.BingoViewModel;
import com.example.gameclub.ui.home.HomeFragment;

import java.util.Objects;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        authenticationViewModel =
                ViewModelProviders.of(this).get(AuthenticationViewModel.class);
        sharedPreferences = requireActivity().getSharedPreferences("accounts", MODE_PRIVATE);
        authenticationViewModel.setSharedPreferences(sharedPreferences);
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
                } else if (pageNumber == 3) {
                    registerLastName = topEditText.getText().toString();
                } else if (pageNumber == 4) {
                    registerCountry = topEditText.getText().toString();
                } else if (pageNumber == 5) {
                    registerInterests = topEditText.getText().toString();
                } else if (pageNumber == 6) {
                    registerEmail = topEditText.getText().toString();
                    System.out.println(registerEmail);
                } else if (pageNumber == 7) {
                    registerPassword = topEditText.getText().toString();
                } else if (pageNumber == 9) {
                    if (authenticationViewModel.login(topEditText.getText().toString(),bottomEditText.getText().toString()) != null) {
                        //display registered and go to home page
                        ma.disableNav(false);
                        ma.setUser(topEditText.getText().toString());
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
                    //display registered and go to home page
                    ma.disableNav(false);
                    ma.setUser(registerEmail);
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

    public void refreshPageLayout() {
        topEditText.getText().clear();
        bottomEditText.getText().clear();
        if (pageNumber == 1) {
            topText.setVisibility(View.VISIBLE);
            bottomText.setVisibility(View.VISIBLE);
            topText.setText("Welcome to GameClub!");
            bottomText.setText("Let's start by setting up your account");
            registerButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            orText.setVisibility(View.VISIBLE);
            topEditText.setVisibility(View.GONE);
            bottomEditText.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            continueButton.setVisibility(View.GONE);
        } else if (pageNumber == 2) {

            bottomText.setText("What's your first name?");
            topEditText.setHint("Type your first name here");
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
