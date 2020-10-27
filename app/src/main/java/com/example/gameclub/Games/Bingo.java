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

public class Bingo extends ViewModel {
    public List<Integer> bingoBoard = new ArrayList<>(25);
    public List<Boolean> checker = new ArrayList<>(25);
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    List<Integer> usedNumbers = new ArrayList<>();
    Random randomGenerator = new Random();
    private String id;
    private boolean isHost = false;
    private Integer ballNum = 0;
    private Integer playerNum = 0;
    private Boolean started = false;
    private Boolean added = false;
    private Integer winner = -1;

    public Bingo() {
        setBoard();
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    try {
                        id = snapshot.child("Bingo").child("Hosting").child("id").getValue().toString();
                        if (id.equals("empty")) {
                            mDatabase.child("Games").child("Bingo").child("Hosting").child("id").setValue(MainActivity.currentUser.getId());
                            isHost = true;
                        }
                        playerNum = Integer.parseInt(snapshot.child("Games").child("Bingo").child("Hosting").child("Num").getValue().toString());
                        playerNum = playerNum + 1;
                        Log.d("PlayerNum", String.valueOf(playerNum));
                        mDatabase.child("Games").child("Bingo").child("Hosting").child("Num").setValue(playerNum);
                    } catch (Exception e) {
                        Log.d("Exception: ", String.valueOf(e));
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (!isHost) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        try {
                            if (snapshot.child("Bingo").child("Hosting").child("Ball").getValue().toString().equals("empty")) {
                                System.out.println("Ball is empty");
                            }
                            ballNum = Integer.parseInt(snapshot.child("Bingo").child("Hosting").child("Ball").getValue().toString());
                            Log.d("BNDB ", String.valueOf(ballNum));
                        } catch (Exception e) {
                            Log.d("Exception: ", String.valueOf(e));
                        }
                    }
                }
                if (isHost && !started) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        try {
                            if (snapshot.child("Bingo").child("Hosting").child("Num").getValue().toString().equals("0")) {
                                System.out.println("Player num is empty");
                            }
                            playerNum = (Integer) snapshot.child("Bingo").child("Hosting").child("Num").getValue();
                            if (playerNum == 2) {
                                startGame();
                            }
                            Log.d("PlayerNum ", String.valueOf(playerNum));
                        } catch (Exception e) {
                            Log.d("Exception: ", String.valueOf(e));
                        }
                    }
                }
                winner = (Integer) snapshot.child("Bingo").child("Hosting").child("winner").getValue();
                if (winner != -1) {
                    System.out.println("The winner is " + winner);
                    reset();
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
    }

    public void setBoard() {
        int number;

        while (bingoBoard.size() < 25) {
            number = randomGenerator.nextInt(100) + 1;
            if (!bingoBoard.contains(number)) {
                bingoBoard.add(number);
                checker.add(false);
            }
        }
    }

    public void startGame() {
        started = true;
        mDatabase.child("Games").child("Bingo").child("Hosting").child("started").setValue("true");
        playGame();
    }

    public void playGame() {
        while(true) {
            checkWin(getBall());
        }
    }

    public int getBall() {
        int number = 0;
        if (isHost) {
            while (!usedNumbers.contains(number = randomGenerator.nextInt(100) + 1)) {
                mDatabase.child("Games").child("Bingo").child("Hosting").child("Ball").setValue(number);
                return number;
            }
        } else {
            number = ballNum;
        }
        return number;
    }

    public void reset() {
        if (isHost) {
            mDatabase.child("Games").child("Bingo").child("Hosting").child("id").setValue("empty");
            mDatabase.child("Games").child("Bingo").child("Hosting").child("Ball").setValue("empty");
            mDatabase.child("Games").child("Bingo").child("Hosting").child("winner").setValue(-1);
            mDatabase.child("Games").child("Bingo").child("Hosting").child("started").setValue("false");
            mDatabase.child("Games").child("Bingo").child("Hosting").child("Num").setValue(0);
        }
        bingoBoard.clear();
        usedNumbers.clear();

    }

    public void checkWin(Integer number) {
        Boolean win = false;
        Integer key = null;
        if (bingoBoard.contains(number)) {
            key = bingoBoard.indexOf(number);
            checker.set(key, true);
        }

        if (bingoBoard.contains(55) && number == 55) {
            win = true;
        }

        if (win && winner == -1) {
            mDatabase.child("Games").child("Bingo").child("Hosting").child("winner").setValue(MainActivity.currentUser.getId());
        }
        for (int i = 0; i < 25; ++i) {
            Integer send = bingoBoard.get(i);
            Boolean state = checker.get(i);
            System.out.println(i + ":number:" + send + " state:" + state);
        }
    }
}
