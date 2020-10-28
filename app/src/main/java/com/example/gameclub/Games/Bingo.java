package com.example.gameclub.Games;
import android.os.health.SystemHealthManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

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
    private Integer winner = -1;
    private String started = "false";
    private Integer playerNum = 0;
    private boolean add = true;
    private boolean finish = false;

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
                            Log.d("ishost", String.valueOf(isHost));
                        }
                        if (add) {
                            playerNum = Integer.parseInt(snapshot.child("Bingo").child("Hosting").child("Num").getValue().toString());
                            playerNum = playerNum + 1;
                            mDatabase.child("Games").child("Bingo").child("Hosting").child("Num").setValue(playerNum);
                            Log.d("playerNum", String.valueOf(playerNum));
                            add = false;
                        }
                    } catch (Exception e) {
                        Log.d("Exception", String.valueOf(e));
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("ishost", String.valueOf(isHost));
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        try {
                            if (!isHost) {
                                if (snapshot.child("Bingo").child("Hosting").child("Ball").getValue().toString().equals("empty")) {
                                    System.out.println("Ball is empty");
                                }
                                ballNum = Integer.parseInt(snapshot.child("Bingo").child("Hosting").child("Ball").getValue().toString());
                                BingoFragment.displayBall(ballNum);
                                checkWin(ballNum);
                                Log.d("BNDB ", String.valueOf(ballNum));
                            }

                            playerNum = Integer.parseInt(snapshot.child("Bingo").child("Hosting").child("Num").getValue().toString());
                            Log.d("playerNum is host", String.valueOf(playerNum));

                            if (isHost && playerNum.equals(2) && started.equals("false")) {
                                System.out.println("start game\n");
                                started = "true";
                                mDatabase.child("Games").child("Bingo").child("Hosting").child("started").setValue("true");
                                startGame();
                            }
                            winner = Integer.parseInt(snapshot.child("Bingo").child("Hosting").child("winner").getValue().toString());
                            if (winner != -1 && !finish) {
                                finish = true;
                                finishGame();
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
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!finish) {
                    int newBall = getBall();
                    BingoFragment.displayBall(newBall);
                    checkWin(newBall);
                }
            }
        }, 5000, 10000);
    }

    public void finishGame() {
        if (MainActivity.currentUser.getId().equals(winner.toString())) {
            System.out.println("YOU WON");
        } else {
            System.out.println("The winner is " + winner);
        }
    }

    public int getBall() {
        int number = 0;
        if (isHost && started.equals("true")) {
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

        mDatabase.child("Games").child("Bingo").child("Hosting").child("id").setValue("empty");
        mDatabase.child("Games").child("Bingo").child("Hosting").child("Ball").setValue("empty");
        mDatabase.child("Games").child("Bingo").child("Hosting").child("Num").setValue(0);
        mDatabase.child("Games").child("Bingo").child("Hosting").child("started").setValue("false");
        mDatabase.child("Games").child("Bingo").child("Hosting").child("winner").setValue(-1);

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

        if (number > 80) {
            win = true;
        }

        if (win && winner.equals(-1)) {
            mDatabase.child("Games").child("Bingo").child("Hosting").child("winner").setValue(MainActivity.currentUser.getId());
        }
//        for (int i = 0; i < 25; ++i) {
//            Integer send = bingoBoard.get(i);
//            Boolean state = checker.get(i);
//            System.out.println(i + ":number:" + send + " state:" + state);
//        }
    }
}
