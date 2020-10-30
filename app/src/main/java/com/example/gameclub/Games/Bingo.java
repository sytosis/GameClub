package com.example.gameclub.Games;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.example.gameclub.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *  This class holds the functions that run the back end of the bingo game
 *  @author John Roby John
 *  @author Kevin Chua
 *  @author Jordan Ng
 *  @author Robert Francis
 */
public class Bingo extends ViewModel {
    // Numbers on bingo board
    public List<Integer> bingoBoard = new ArrayList<>(25);
    // List of boolean, false if number hasn't been called
    public List<Boolean> checker = new ArrayList<>(25);
    // Database
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    // Numbers that have been used
    private List<Integer> usedNumbers = new ArrayList<>();
    // Random number generator for calling numbers
    private Random randomGenerator = new Random();
    // Id of user
    private String id;
    // Is user host
    private boolean isHost = false;
    // New ball called
    private Integer ballNum = 0;
    // Winner ID
    private Integer winner = -1;
    // Has game started
    private String started = "false";
    // Number of players in game
    private Integer playerNum = 0;
    // Has user added to database player number
    private boolean add = true;
    // Has game finished
    private boolean finish = false;
    // Has user won
    private Boolean win = false;
    // Has user done finish game elements
    private Boolean done = false;
    // Winner name
    private String winName = "";

    public Bingo() {
        // Set the board up
        setBoard();
    }

    public void setBoard() {
        // Listen to the database
        mDatabase.addChildEventListener(new ChildEventListener() {
            // What currently is in the database.
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    try {
                        // Get the user id of the bingo game
                        id = snapshot.child("Bingo").child("Hosting").child("id").getValue().toString();
                        // If no user id
                        if (id.equals("empty")) {
                            // Make game id this user's id
                            mDatabase.child("Games").child("Bingo").child("Hosting").child("id").setValue(MainActivity.currentUser.getId());
                            // User becomes host
                            isHost = true;
                        }
                        // Has user added player number
                        if (add) {
                            // Get number of players in game
                            playerNum = Integer.parseInt(snapshot.child("Bingo").child("Hosting").child("Num").getValue().toString());
                            playerNum = playerNum + 1;
                            // Set number of players in game
                            mDatabase.child("Games").child("Bingo").child("Hosting").child("Num").setValue(playerNum);
                            // Has added
                            add = false;
                        }
                    } catch (Exception e) {
                        Log.d("Exception", String.valueOf(e));
                    }
                }
            }

            // When database gets changes
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    try {
                        // If not host
                        if (!isHost) {
                            // Get ball number
                            if (snapshot.child("Bingo").child("Hosting").child("Ball").getValue().toString().equals("empty")) {
                                System.out.println("Ball is empty");
                            }
                            ballNum = Integer.parseInt(snapshot.child("Bingo").child("Hosting").child("Ball").getValue().toString());
                            // Display rolled ball
                            BingoFragment.displayBall(ballNum);
                            // Check if won game
                            checkWin(ballNum);
                            Log.d("BNDB ", String.valueOf(ballNum));
                        }
                        // Get player number
                        playerNum = Integer.parseInt(snapshot.child("Bingo").child("Hosting").child("Num").getValue().toString());

                        // If user is host, hasn't started the game and the player number is 2
                        if (isHost && playerNum.equals(2) && started.equals("false")) {
                            System.out.println("start game\n");
                            // Start game is true
                            started = "true";
                            // Tell other players game has started
                            mDatabase.child("Games").child("Bingo").child("Hosting").child("started").setValue("true");
                            // Start game
                            startGame();
                        }
                        // Get winner id
                        winner = Integer.parseInt(snapshot.child("Bingo").child("Hosting").child("winner").getValue().toString());

                        // If no winner and game isn't finished and user has bingo
                        if (winner != -1 && !finish && win) {
                            // Finish game
                            finish = true;
                            // Get winner name
                            winName = snapshot.child("Bingo").child("Hosting").child("name").getValue().toString();
                        }
                    } catch (Exception e) {
                        Log.d("Exception ", String.valueOf(e));
                    }
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

        int number;
        // Set 25 numbers
        while (bingoBoard.size() < 25) {
            // Get random number
            number = randomGenerator.nextInt(100) + 1;
            // Check number doesn't already exist
            if (!bingoBoard.contains(number)) {
                // Add number
                bingoBoard.add(number);
                // Add state
                checker.add(false);
            }
        }
    }

    /**
     *  Start the bingo game
     */
    public void startGame() {
        Timer timer = new Timer();
        // Do run function after 5 seconds every 10 seconds
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Game is finished
                if (finish) {
                    finishGame();
                }
                // If game isn't finished
                if (!finish) {
                    // Get new ball
                    int newBall = getBall();
                    // Display new ball
                    BingoFragment.displayBall(newBall);
                    // Check if win condition met
                    checkWin(newBall);
                }
            }
        }, 5000, 10000);
    }

    /**
     * Finish the game
     */
    public void finishGame() {
        // If has done finish
        if (!done) {
            done = true;
            // If user won
            if (MainActivity.currentUser.getId().equals(winner.toString())) {
                System.out.println("YOU WON");
            } else {
                System.out.println("The winner is " + winner);
            }
            // Reset database values
            reset();
            // Game over
            BingoFragment.gameOver(winner, winName);
        }
    }

    /**
     *  Called to get a new ball by host
     *  Returns a number between 1-100 that hasn't already been called
     */
    public int getBall() {
        int number = 0;
        // If user is host and has started
        if (isHost && started.equals("true")) {
            // Loop
            while (true) {
                // get non used number
                if (!usedNumbers.contains(number = randomGenerator.nextInt(100) + 1)) {
                    // Set ball number in database
                    mDatabase.child("Games").child("Bingo").child("Hosting").child("Ball").setValue(number);
                    return number;
                }
            }
        } else {
            // Old ball number
            number = ballNum;
        }
        return number;
    }

    /**
     *  This function resets the database values, bingo board, used number and the states
     */
    public void reset() {

        mDatabase.child("Games").child("Bingo").child("Hosting").child("id").setValue("empty");
        mDatabase.child("Games").child("Bingo").child("Hosting").child("Ball").setValue("empty");
        mDatabase.child("Games").child("Bingo").child("Hosting").child("Num").setValue(0);
        mDatabase.child("Games").child("Bingo").child("Hosting").child("started").setValue("false");
        mDatabase.child("Games").child("Bingo").child("Hosting").child("winner").setValue(-1);
        mDatabase.child("Games").child("Bingo").child("Hosting").child("Chat").setValue("");
        mDatabase.child("Games").child("Bingo").child("Hosting").child("name").setValue("");
        bingoBoard.clear();
        usedNumbers.clear();
        checker.clear();
    }

    /**
     *  Checks if the user has won through the given Integer
     */
    public void checkWin(int number) {
        Integer key = null;
        // If user has ball changes ball state to true
        if (bingoBoard.contains(number)) {
            key = bingoBoard.indexOf(number);
            checker.set(key, true);
        }

        // Check if user has won vertically
        for (int i = 0; i < 5; ++i) {
            if (checker.get(i) && checker.get(i + 5) && checker.get(i + 10) && checker.get(i + 15) && checker.get(i + 20)) {
                win = true;
                System.out.println("vertical");
            }
        }

        // Check if user has won horizontally
        for (int i = 0; i < 21; i = i + 5) {
            if (checker.get(i) && checker.get(i + 1) && checker.get(i + 2) && checker.get(i + 3) && checker.get(i + 4)) {
                win = true;
                System.out.println("horizontal");
            }
        }

        // Check if user has won diagonally
        if (checker.get(0) && checker.get(6) && checker.get(12) && checker.get(18) && checker.get(24)) {
            win = true;
            System.out.println("diagonal1");
        }

        // Check if user has won diagonally
        if (checker.get(4) && checker.get(8) && checker.get(12) && checker.get(16) && checker.get(20)) {
            win = true;
            System.out.println("diagonal2");
        }

        // If user has won
        if (win) {
            // Change database values to user id and name
            mDatabase.child("Games").child("Bingo").child("Hosting").child("winner").setValue(MainActivity.currentUser.getId());
            mDatabase.child("Games").child("Bingo").child("Hosting").child("name").setValue(MainActivity.currentUser.getFirstName());
        }
    }
}
