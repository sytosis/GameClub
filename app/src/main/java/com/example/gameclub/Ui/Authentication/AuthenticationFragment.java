package com.example.gameclub.Ui.Authentication;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.MainActivity;
import com.example.gameclub.R;
import com.example.gameclub.Users.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AuthenticationFragment extends Fragment {
    private AuthenticationViewModel authenticationViewModel;
    private Integer pageNumber = 1;
    private SharedPreferences sharedPreferences;
    private Button registerButton;
    private Button loginButton;
    private Button nextButton;
    private Button backButton;
    private Button continueButton;
    private TextView topText;
    private TextView bottomText;
    private TextView orText;
    private EditText topEditText;
    private EditText bottomEditText;
    private String registerEmail;
    private String registerPassword;
    private String registerFirstName;
    private String registerLastName;
    private String registerCountry;
    private String registerInterests;
    private View view;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Long userNum; //Number of users
    private List<String> existingId = new ArrayList<String>();
    private List<String> existingEmails = new ArrayList<String>();
    private List<String> existingPasswords = new ArrayList<String>();
    private List<User> existingUsers = new ArrayList<User>();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference() ;
    private StorageReference imagesRef = storageRef.child("images");
    private StorageReference spaceRef = storageRef.child("images/space.jpg");
    private ImageView imageView;

    public AuthenticationFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Firebase stuff
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(snapshot.getKey(), snapshot.getChildrenCount() + "");
                userNum = snapshot.getChildrenCount();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    try {
                        String id = postSnapshot.getKey();
                        String email = postSnapshot.child("email").getValue().toString();
                        String pass = postSnapshot.child("password").getValue().toString();
                        String first = postSnapshot.child("firstName").getValue().toString();
                        String last = postSnapshot.child("lastName").getValue().toString();
                        String country = postSnapshot.child("country").getValue().toString();
                        String interests = postSnapshot.child("interest").getValue().toString();
                        String friends = postSnapshot.child("friends").getValue().toString();
                        existingEmails.add(email);
                        existingId.add(id);
                        existingPasswords.add(pass);
                        User user = new User(id, email, pass, first, last, country, interests, friends);
                        existingUsers.add(user);
                    } catch (NullPointerException n) {
                        System.out.println("Null Pointer Caught" + n);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        authenticationViewModel =
                ViewModelProviders.of(this).get(AuthenticationViewModel.class);
        sharedPreferences = requireActivity().getSharedPreferences("accounts", MODE_PRIVATE);
        //getContext().getSharedPreferences("accounts",0).edit().clear().commit();
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
        final AuthenticationFragment fragment = this;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topText.setVisibility(View.INVISIBLE);
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
                topText.setVisibility(View.INVISIBLE);
                if (pageNumber == 2) {
                    registerFirstName = topEditText.getText().toString();
                    if (registerFirstName.equals("")) {
                        pageNumber = pageNumber - 1;
                        topText.setText("Empty!");
                        topText.setVisibility(View.VISIBLE);
                        topText.setTextColor(Color.RED);
                    }
                } else if (pageNumber == 3) {
                    registerLastName = topEditText.getText().toString();
                    if (registerLastName.equals("")) {
                        pageNumber = pageNumber - 1;
                        topText.setText("Empty!");
                        topText.setVisibility(View.VISIBLE);
                        topText.setTextColor(Color.RED);
                    }
                } else if (pageNumber == 4) {
                    registerCountry = topEditText.getText().toString();
                    if (registerCountry.equals("")) {
                        pageNumber = pageNumber - 1;
                        topText.setText("Empty!");
                        topText.setVisibility(View.VISIBLE);
                        topText.setTextColor(Color.RED);
                    }
                } else if (pageNumber == 5) {
                    registerInterests = topEditText.getText().toString();
                    if (registerInterests.equals("")) {
                        pageNumber = pageNumber - 1;
                        topText.setText("Empty!");
                        topText.setVisibility(View.VISIBLE);
                        topText.setTextColor(Color.RED);
                    }
                } else if (pageNumber == 6) {
                    registerEmail = topEditText.getText().toString();
                    //Check user does not already exist
                    if (checkNewUser(registerEmail)) {
                        Log.d("EMAIL: ", "email already exists");
                        topEditText.setHint("Already existing email");
                        topEditText.setText("");
                        pageNumber = pageNumber - 1;
                        topText.setText("Email already exists");
                        topText.setVisibility(View.VISIBLE);
                        topText.setTextColor(Color.RED);
                    }
                    if (registerEmail.equals("")) {
                        pageNumber = pageNumber - 1;
                        topText.setText("Empty!");
                        topText.setVisibility(View.VISIBLE);
                        topText.setTextColor(Color.RED);
                    }
                } else if (pageNumber == 7) {
                    registerPassword = topEditText.getText().toString();
                    if (registerPassword.equals("")) {
                        pageNumber = pageNumber - 1;
                        topText.setText("Empty!");
                        topText.setVisibility(View.VISIBLE);
                        topText.setTextColor(Color.RED);
                    }
                } else if (pageNumber == 9) {
                    if (existingEmails.contains(topEditText.getText().toString())) {
                        int index = existingEmails.indexOf(topEditText.getText().toString());
                        String correctPass = existingPasswords.get(index);
                        if (correctPass.equals(bottomEditText.getText().toString())) {
                            MainActivity.currentUser = existingUsers.get(index);
                            NavHostFragment.findNavController(AuthenticationFragment.this).navigate(R.id.action_AuthenticationFragment_to_HomeFragment);
                            System.out.println("Logged in");
                        } else {
                            bottomEditText.setText("");
                            topText.setText("Incorrect password");
                            topText.setVisibility(View.VISIBLE);
                            topText.setTextColor(Color.RED);
                        }
                    } else {
                        topEditText.setText("");
                        topText.setText("Email doesn't exist");
                        topText.setVisibility(View.VISIBLE);
                        topText.setTextColor(Color.RED);
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
                    System.out.println(userNum.toString());
                    //Add new user to database
                    writeNewUser(userNum.toString(), registerEmail, registerPassword, registerFirstName, registerLastName, registerCountry, registerInterests);
                    MainActivity.currentUser = new User(userNum.toString(), registerEmail, registerPassword, registerFirstName, registerLastName, registerCountry, registerInterests, "");
                    //display registered and go to home page
                    NavHostFragment.findNavController(AuthenticationFragment.this).navigate(R.id.action_AuthenticationFragment_to_HomeFragment);
                    System.out.println("registered");
                    //display failed to register, username exists
                    System.out.println("register failed");
            }
        });
        refreshPageLayout();
        return root;
    }
    /*
     *  Returns true if the new user's email already exists in the database.
     *  Returns false otherwise.
     */
    public boolean checkNewUser(String email) {
        return existingEmails.contains(email);
    }

    /*
     *  Creates new user in firebase database
     */
    public void writeNewUser(String userId, String e, String pass, String first, String last, String c, String i) {
        mDatabase.child("users").child(userId).child("email").setValue(e);
        mDatabase.child("users").child(userId).child("password").setValue(pass);
        mDatabase.child("users").child(userId).child("firstName").setValue(first);
        mDatabase.child("users").child(userId).child("lastName").setValue(last);
        mDatabase.child("users").child(userId).child("country").setValue(c);
        mDatabase.child("users").child(userId).child("interest").setValue(i);
        mDatabase.child("users").child(userId).child("friends").setValue("");
        Log.d("writeNewUser", userId);
    }

    public List<User> getUsers() {
        return existingUsers;
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
