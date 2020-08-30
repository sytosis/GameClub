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
    SharedPreferences sharedPreferences;
    Button registerButton;
    Button loginButton;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        authenticationViewModel =
                ViewModelProviders.of(this).get(AuthenticationViewModel.class);
        sharedPreferences = requireActivity().getSharedPreferences("accounts", MODE_PRIVATE);
        authenticationViewModel.setSharedPreferences(sharedPreferences);
        View root = inflater.inflate(R.layout.login_signup, container, false);

        registerButton = root.findViewById(R.id.register_button);
        loginButton = root.findViewById(R.id.login_button);
        final MainActivity ma = (MainActivity) requireActivity();
        ma.disableNav(true);
        final EditText usernameText = root.findViewById(R.id.editTextUsername);
        final EditText passwordText = root.findViewById(R.id.editTextPassword);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (authenticationViewModel.register(usernameText.getText().toString(),passwordText.getText().toString()) != null) {
                    //display registered and go to home page
                    ma.disableNav(false);
                    ma.setUser(usernameText.getText().toString());
                    NavHostFragment.findNavController(AuthenticationFragment.this).navigate(R.id.action_AuthenticationFragment_to_HomeFragment);
                    System.out.println("registered");
                } else {
                    //display failed to register, username exists
                    System.out.println("register failed");
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (authenticationViewModel.login(usernameText.getText().toString(),passwordText.getText().toString()) != null) {
                    //display registered and go to home page
                    ma.disableNav(false);
                    ma.setUser(usernameText.getText().toString());
                    NavHostFragment.findNavController(AuthenticationFragment.this).navigate(R.id.action_AuthenticationFragment_to_HomeFragment);
                    System.out.println("Logged in");
                } else {
                    //display login failed, account already exists
                    System.out.println("password wrong");
                }
            }
        });
        return root;
    }
}
