package com.example.gameclub.Games;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gameclub.MainActivity;
import com.example.gameclub.R;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controls the bingo game UI and some functionality
 */
public class BingoFragment extends Fragment {

    private static TextView winText;
    public Bingo bingoGame;
    private static View root;
    private List<ImageView> bingoBoardImages = new ArrayList<>();
    private List<TextView> bingoBoardText = new ArrayList<>();
    private Button chatOpenButton;
    private Button chatCloseButton;
    private LinearLayout wholeChatBox;
    private Button homeButton;
    private Button sendChat;
    private Button toggleHelpButton;
    private LinearLayout chatChessBox;
    private ScrollView scrollViewChat;
    private EditText text;
    private ImageView RollBall;
    private TextView RollText;
    private TextView homeHelpText;
    private TextView chatHelpText;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String receive;
    private String[] record;
    private static Button replayButton;
    private static Button quitButton;
    private ConstraintLayout gameOverScreen;
    private Integer winner;
    private static Boolean serverMessage = false;
    private static Random randomGenerator = new Random();
    private static Integer recordlen = 0;
    private static Integer receivelen = 0;

    public BingoFragment() {

    }

    /**
     *  Will set the server message state
     */
    public static void setServerMessage(Boolean state) {
        serverMessage = state;
    }

    /**
     *  Will return the server message state
     */
    public static Boolean getServerMessage() {
        return serverMessage;
    }

    /**
     *  Add bingo text
     */
    public void fillBingoBoardTextList() {
        bingoBoardText.add((TextView) root.findViewById(R.id.Text1));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text2));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text3));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text4));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text5));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text6));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text7));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text8));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text9));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text10));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text11));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text12));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text13));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text14));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text15));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text16));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text17));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text18));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text19));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text20));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text21));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text22));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text23));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text24));
        bingoBoardText.add((TextView) root.findViewById(R.id.Text25));
    }

    /**
     *  Add bingo ball images
     */
    public void fillBingoBoardImageList() {
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball1));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball2));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball3));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball4));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball5));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball6));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball7));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball8));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball9));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball10));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball11));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball12));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball13));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball14));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball15));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball16));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball17));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball18));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball19));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball20));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball21));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball22));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball23));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball24));
        bingoBoardImages.add((ImageView) root.findViewById(R.id.Ball25));
    }

    /**
     * Sets the ball image based on the ball number
     */
    public static void setBallColour(ImageView ball, int number) {
        if (number < 16) {
            ball.setImageResource(R.drawable.pink_ball);
        }
        else if (number < 31) {
            ball.setImageResource(R.drawable.blue_ball);
        }
        else if (number < 45) {
            ball.setImageResource(R.drawable.yellow_ball);
        }
        else if (number < 61) {
            ball.setImageResource(R.drawable.green_ball);
        }
        else {
            ball.setImageResource(R.drawable.purple_ball);
        }
    }

    /**
     * If game is over display popup with winner name or if user has won
     */
    public static void gameOver(int winner, String winName) {
        // If database id is user id
        if (Integer.parseInt(MainActivity.currentUser.getId()) == winner) {
            System.out.println("YOU WON");
        } else {
            System.out.println("The winner is " + winName);
            winText = root.findViewById(R.id.winText);
            String win = "The winner is " + winName;
            winText.setText(win);
        }
    }

    /**
     *  Display the new ball number
     */
    public static void displayBall(int number) {
        setBallColour((ImageView) root.findViewById(R.id.RollBall), number);
        ((TextView) root.findViewById(R.id.RollText)).setText(String.valueOf(number));
    }

    /**
     *  Set the text for the board
     */
    public void setBoardText() {
        for (int i = 0; i < 25; ++i) {
            bingoBoardText.get(i).setText(String.valueOf(bingoGame.bingoBoard.get(i)));
        }
    }

    /**
     * Set the color for the balls on board
     */
    public void setBoardColours() {
        for (int i = 0; i < 25; ++i) {
            setBallColour(bingoBoardImages.get(i), bingoGame.bingoBoard.get(i));
            if (bingoGame.checker.get(i)) {
                bingoBoardImages.get(i).setImageResource(R.drawable.grey_ball);
            }
        }
    }

    /**
     *  If ball has been called and is on the user's board stamp it
     */
    public void stampBall() {
        // Checks all board ball states
        for (int i = 0; i < 25; ++i) {
            if (bingoGame.checker.get(i)) {
                // Changes ball image to grey/stamped
                bingoBoardImages.get(i).setImageResource(R.drawable.grey_ball);
            }
        }
    }

    /**
     *  Print the chat using the given string
     */
    public void printChat(String chat) {
        serverMessage = true;
        TextView tv = new TextView(getContext());
        tv.setText(chat);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        chatChessBox.addView(tv);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        bingoGame = ViewModelProviders.of(this).get(Bingo.class);
        root = inflater.inflate(R.layout.fragment_bingo, container, false);
        chatCloseButton = root.findViewById(R.id.close_chat_button);
        chatOpenButton = root.findViewById(R.id.message_button);
        homeButton = root.findViewById(R.id.Bingo_Home_Button);
        sendChat = root.findViewById(R.id.send_chat_button);
        chatChessBox = root.findViewById(R.id.chess_chat_box);
        scrollViewChat = root.findViewById(R.id.scrollview_chat);
        text = root.findViewById(R.id.chess_chat_id);
        wholeChatBox = root.findViewById(R.id.whole_chat_box);
        RollBall = root.findViewById(R.id.RollBall);
        RollText = root.findViewById(R.id.RollText);
        replayButton = root.findViewById(R.id.replay);
        quitButton = root.findViewById(R.id.quit);
        gameOverScreen = root.findViewById(R.id.game_end_screen);
        homeHelpText = root.findViewById(R.id.home_help_text);
        chatHelpText = root.findViewById(R.id.message_help_text);
        toggleHelpButton = root.findViewById(R.id.toggle_help_text_button);

        // Database listener
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        // Get database chat values
                        receive = snapshot.child("Bingo").child("Hosting").child("Chat").getValue().toString();
                        // Split string as different messages are separated by '/'
                        String[] messages = receive.split("/");
                        record = receive.split("/");
                        // Print all database chat messages
                        for (int i = 0; i < messages.length - 1; ++i) {
                            printChat(messages[i]);
                        }
                    } catch (Exception e) {
                        Log.d("Exception", String.valueOf(e));
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    // Get the winner id
                    winner = Integer.parseInt(snapshot.child("Bingo").child("Hosting").child("winner").getValue().toString());
                    // Get database chat
                    receive = snapshot.child("Bingo").child("Hosting").child("Chat").getValue().toString();
                    String[] messages = receive.split("/");
                    // User chat record length
                    recordlen = record.length;
                    // database chat record length
                    receivelen = messages.length;
                    // Stamp the ball
                    stampBall();
                    // If the user chat is shorter than database chat
                    if (recordlen < receivelen) {
                        // Get number of chats user needs
                        int difference = receivelen - recordlen;
                        // Print the database chats user doesn't have
                        for (int i = receivelen - difference; i < receivelen; ++i) {
                            printChat(messages[i]);
                        }
                        // Update record to database record
                        record = messages;
                    }
                    //  If the winner is decided
                    if (winner != -1) {
                        // Make game over screen visible
                        gameOverScreen.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.d("Exception ", String.valueOf(e));
                }
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

        // Set and display board ball numbers
        fillBingoBoardTextList();
        setBoardText();

        // Set and display board ball images
        fillBingoBoardImageList();
        setBoardColours();

        // If home button is pressed
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Take user to home page
                NavHostFragment.findNavController(BingoFragment.this).navigate((R.id.action_nav_bingo_to_home));
            }
        });

        //toggles the help text which shows what home and chat button does with a popup explanation
        toggleHelpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (homeHelpText.getVisibility() == View.INVISIBLE) {
                    homeHelpText.setVisibility(View.VISIBLE);
                    chatHelpText.setVisibility(View.VISIBLE);
                } else {
                    homeHelpText.setVisibility(View.INVISIBLE);
                    chatHelpText.setVisibility(View.INVISIBLE);
                }
            }
        });
        // Set button text to Play Again
        String replay = "Play Again";
        replayButton.setText(replay);

        // If replay button is clicked
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Take to user back to current page
                NavHostFragment.findNavController(BingoFragment.this).navigate((R.id.action_nav_gallery_self));
            }
        });

        // Set button text to Quit
        String quit = "Quit";
        quitButton.setText(quit);

        // If quit button is clicked
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Take user to home page
                NavHostFragment.findNavController(BingoFragment.this).navigate((R.id.action_nav_bingo_to_home));
            }
        });

        // If send chat button is clicked
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendChat = (" " + MainActivity.currentUser.getFirstName() + ": " + text.getText());
                text.getText().clear();
                // Add chat to database
                mDatabase.child("Games").child("Bingo").child("Hosting").child("Chat").setValue(receive+"/"+sendChat);
                scrollViewChat.fullScroll(View.FOCUS_DOWN);
            }
        });

        // Open chat button is clicked
        chatOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make bingo invisible and chat visible
                wholeChatBox.setVisibility(View.VISIBLE);
                toggleHelpButton.setVisibility(View.INVISIBLE);
                ImageView ball1 = root.findViewById(R.id.Ball1);
                ball1.setVisibility(View.INVISIBLE);
                ImageView ball2 = root.findViewById(R.id.Ball2);
                ball2.setVisibility(View.INVISIBLE);
                ImageView ball3 = root.findViewById(R.id.Ball3);
                ball3.setVisibility(View.INVISIBLE);
                ImageView ball4 = root.findViewById(R.id.Ball4);
                ball4.setVisibility(View.INVISIBLE);
                ImageView ball5 = root.findViewById(R.id.Ball5);
                ball5.setVisibility(View.INVISIBLE);
                ImageView ball6 = root.findViewById(R.id.Ball6);
                ball6.setVisibility(View.INVISIBLE);
                ImageView ball7 = root.findViewById(R.id.Ball7);
                ball7.setVisibility(View.INVISIBLE);
                ImageView ball8 = root.findViewById(R.id.Ball8);
                ball8.setVisibility(View.INVISIBLE);
                ImageView ball9 = root.findViewById(R.id.Ball9);
                ball9.setVisibility(View.INVISIBLE);
                ImageView ball10 = root.findViewById(R.id.Ball10);
                ball10.setVisibility(View.INVISIBLE);
                ImageView ball11 = root.findViewById(R.id.Ball11);
                ball11.setVisibility(View.INVISIBLE);
                ImageView ball12 = root.findViewById(R.id.Ball12);
                ball12.setVisibility(View.INVISIBLE);
                ImageView ball13 = root.findViewById(R.id.Ball13);
                ball13.setVisibility(View.INVISIBLE);
                ImageView ball14 = root.findViewById(R.id.Ball14);
                ball14.setVisibility(View.INVISIBLE);
                ImageView ball15 = root.findViewById(R.id.Ball15);
                ball15.setVisibility(View.INVISIBLE);
                ImageView ball16 = root.findViewById(R.id.Ball16);
                ball16.setVisibility(View.INVISIBLE);
                ImageView ball17 = root.findViewById(R.id.Ball17);
                ball17.setVisibility(View.INVISIBLE);
                ImageView ball18 = root.findViewById(R.id.Ball18);
                ball18.setVisibility(View.INVISIBLE);
                ImageView ball19 = root.findViewById(R.id.Ball19);
                ball19.setVisibility(View.INVISIBLE);
                ImageView ball20 = root.findViewById(R.id.Ball20);
                ball20.setVisibility(View.INVISIBLE);
                ImageView ball21 = root.findViewById(R.id.Ball21);
                ball21.setVisibility(View.INVISIBLE);
                ImageView ball22 = root.findViewById(R.id.Ball22);
                ball22.setVisibility(View.INVISIBLE);
                ImageView ball23 = root.findViewById(R.id.Ball23);
                ball23.setVisibility(View.INVISIBLE);
                ImageView ball24 = root.findViewById(R.id.Ball24);
                ball24.setVisibility(View.INVISIBLE);
                ImageView ball25 = root.findViewById(R.id.Ball25);
                ball25.setVisibility(View.INVISIBLE);
                homeButton.setVisibility(View.INVISIBLE);
                RollBall.setVisibility(View.INVISIBLE);
                RollText.setVisibility(View.INVISIBLE);
                TextView text1 = root.findViewById(R.id.Text1);
                text1.setVisibility(View.INVISIBLE);
                TextView text2 = root.findViewById(R.id.Text2);
                text2.setVisibility(View.INVISIBLE);
                TextView text3 = root.findViewById(R.id.Text3);
                text3.setVisibility(View.INVISIBLE);
                TextView text4 = root.findViewById(R.id.Text4);
                text4.setVisibility(View.INVISIBLE);
                TextView text5 = root.findViewById(R.id.Text5);
                text5.setVisibility(View.INVISIBLE);
                TextView text6 = root.findViewById(R.id.Text6);
                text6.setVisibility(View.INVISIBLE);
                TextView text7 = root.findViewById(R.id.Text7);
                text7.setVisibility(View.INVISIBLE);
                TextView text8 = root.findViewById(R.id.Text8);
                text8.setVisibility(View.INVISIBLE);
                TextView text9 = root.findViewById(R.id.Text9);
                text9.setVisibility(View.INVISIBLE);
                TextView text10 = root.findViewById(R.id.Text10);
                text10.setVisibility(View.INVISIBLE);
                TextView text11 = root.findViewById(R.id.Text11);
                text11.setVisibility(View.INVISIBLE);
                TextView text12 = root.findViewById(R.id.Text12);
                text12.setVisibility(View.INVISIBLE);
                TextView text13 = root.findViewById(R.id.Text13);
                text13.setVisibility(View.INVISIBLE);
                TextView text14 = root.findViewById(R.id.Text14);
                text14.setVisibility(View.INVISIBLE);
                TextView text15 = root.findViewById(R.id.Text15);
                text15.setVisibility(View.INVISIBLE);
                TextView text16 = root.findViewById(R.id.Text16);
                text16.setVisibility(View.INVISIBLE);
                TextView text17 = root.findViewById(R.id.Text17);
                text17.setVisibility(View.INVISIBLE);
                TextView text18 = root.findViewById(R.id.Text18);
                text18.setVisibility(View.INVISIBLE);
                TextView text19 = root.findViewById(R.id.Text19);
                text19.setVisibility(View.INVISIBLE);
                TextView text20 = root.findViewById(R.id.Text20);
                text20.setVisibility(View.INVISIBLE);
                TextView text21 = root.findViewById(R.id.Text21);
                text21.setVisibility(View.INVISIBLE);
                TextView text22 = root.findViewById(R.id.Text22);
                text22.setVisibility(View.INVISIBLE);
                TextView text23 = root.findViewById(R.id.Text23);
                text23.setVisibility(View.INVISIBLE);
                TextView text24 = root.findViewById(R.id.Text24);
                text24.setVisibility(View.INVISIBLE);
                TextView text25 = root.findViewById(R.id.Text25);
                text25.setVisibility(View.INVISIBLE);
                chatOpenButton.setVisibility(View.INVISIBLE);

            }
        });

        // If chat close button is clicked
        chatCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make bingo visible and chat invisible
                wholeChatBox.setVisibility(View.INVISIBLE);
                toggleHelpButton.setVisibility(View.VISIBLE);
                ImageView ball1 = root.findViewById(R.id.Ball1);
                ball1.setVisibility(View.VISIBLE);
                ImageView ball2 = root.findViewById(R.id.Ball2);
                ball2.setVisibility(View.VISIBLE);
                ImageView ball3 = root.findViewById(R.id.Ball3);
                ball3.setVisibility(View.VISIBLE);
                ImageView ball4 = root.findViewById(R.id.Ball4);
                ball4.setVisibility(View.VISIBLE);
                ImageView ball5 = root.findViewById(R.id.Ball5);
                ball5.setVisibility(View.VISIBLE);
                ImageView ball6 = root.findViewById(R.id.Ball6);
                ball6.setVisibility(View.VISIBLE);
                ImageView ball7 = root.findViewById(R.id.Ball7);
                ball7.setVisibility(View.VISIBLE);
                ImageView ball8 = root.findViewById(R.id.Ball8);
                ball8.setVisibility(View.VISIBLE);
                ImageView ball9 = root.findViewById(R.id.Ball9);
                ball9.setVisibility(View.VISIBLE);
                ImageView ball10 = root.findViewById(R.id.Ball10);
                ball10.setVisibility(View.VISIBLE);
                ImageView ball11 = root.findViewById(R.id.Ball11);
                ball11.setVisibility(View.VISIBLE);
                ImageView ball12 = root.findViewById(R.id.Ball12);
                ball12.setVisibility(View.VISIBLE);
                ImageView ball13 = root.findViewById(R.id.Ball13);
                ball13.setVisibility(View.VISIBLE);
                ImageView ball14 = root.findViewById(R.id.Ball14);
                ball14.setVisibility(View.VISIBLE);
                ImageView ball15 = root.findViewById(R.id.Ball15);
                ball15.setVisibility(View.VISIBLE);
                ImageView ball16 = root.findViewById(R.id.Ball16);
                ball16.setVisibility(View.VISIBLE);
                ImageView ball17 = root.findViewById(R.id.Ball17);
                ball17.setVisibility(View.VISIBLE);
                ImageView ball18 = root.findViewById(R.id.Ball18);
                ball18.setVisibility(View.VISIBLE);
                ImageView ball19 = root.findViewById(R.id.Ball19);
                ball19.setVisibility(View.VISIBLE);
                ImageView ball20 = root.findViewById(R.id.Ball20);
                ball20.setVisibility(View.VISIBLE);
                ImageView ball21 = root.findViewById(R.id.Ball21);
                ball21.setVisibility(View.VISIBLE);
                ImageView ball22 = root.findViewById(R.id.Ball22);
                ball22.setVisibility(View.VISIBLE);
                ImageView ball23 = root.findViewById(R.id.Ball23);
                ball23.setVisibility(View.VISIBLE);
                ImageView ball24 = root.findViewById(R.id.Ball24);
                ball24.setVisibility(View.VISIBLE);
                ImageView ball25 = root.findViewById(R.id.Ball25);
                ball25.setVisibility(View.VISIBLE);
                homeButton.setVisibility(View.VISIBLE);
                RollBall.setVisibility(View.VISIBLE);
                RollText.setVisibility(View.VISIBLE);
                TextView text1 = root.findViewById(R.id.Text1);
                text1.setVisibility(View.VISIBLE);
                TextView text2 = root.findViewById(R.id.Text2);
                text2.setVisibility(View.VISIBLE);
                TextView text3 = root.findViewById(R.id.Text3);
                text3.setVisibility(View.VISIBLE);
                TextView text4 = root.findViewById(R.id.Text4);
                text4.setVisibility(View.VISIBLE);
                TextView text5 = root.findViewById(R.id.Text5);
                text5.setVisibility(View.VISIBLE);
                TextView text6 = root.findViewById(R.id.Text6);
                text6.setVisibility(View.VISIBLE);
                TextView text7 = root.findViewById(R.id.Text7);
                text7.setVisibility(View.VISIBLE);
                TextView text8 = root.findViewById(R.id.Text8);
                text8.setVisibility(View.VISIBLE);
                TextView text9 = root.findViewById(R.id.Text9);
                text9.setVisibility(View.VISIBLE);
                TextView text10 = root.findViewById(R.id.Text10);
                text10.setVisibility(View.VISIBLE);
                TextView text11 = root.findViewById(R.id.Text11);
                text11.setVisibility(View.VISIBLE);
                TextView text12 = root.findViewById(R.id.Text12);
                text12.setVisibility(View.VISIBLE);
                TextView text13 = root.findViewById(R.id.Text13);
                text13.setVisibility(View.VISIBLE);
                TextView text14 = root.findViewById(R.id.Text14);
                text14.setVisibility(View.VISIBLE);
                TextView text15 = root.findViewById(R.id.Text15);
                text15.setVisibility(View.VISIBLE);
                TextView text16 = root.findViewById(R.id.Text16);
                text16.setVisibility(View.VISIBLE);
                TextView text17 = root.findViewById(R.id.Text17);
                text17.setVisibility(View.VISIBLE);
                TextView text18 = root.findViewById(R.id.Text18);
                text18.setVisibility(View.VISIBLE);
                TextView text19 = root.findViewById(R.id.Text19);
                text19.setVisibility(View.VISIBLE);
                TextView text20 = root.findViewById(R.id.Text20);
                text20.setVisibility(View.VISIBLE);
                TextView text21 = root.findViewById(R.id.Text21);
                text21.setVisibility(View.VISIBLE);
                TextView text22 = root.findViewById(R.id.Text22);
                text22.setVisibility(View.VISIBLE);
                TextView text23 = root.findViewById(R.id.Text23);
                text23.setVisibility(View.VISIBLE);
                TextView text24 = root.findViewById(R.id.Text24);
                text24.setVisibility(View.VISIBLE);
                TextView text25 = root.findViewById(R.id.Text25);
                text25.setVisibility(View.VISIBLE);
                chatOpenButton.setVisibility(View.VISIBLE);
            }
        });
        return root;
    }

}
