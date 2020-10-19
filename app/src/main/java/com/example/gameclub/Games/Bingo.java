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

public class Bingo extends ViewModel {
    public List<Integer> bingoBoard = new ArrayList<>(25);
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    List<Integer> usedNumbers = new ArrayList<>();
    Random randomGenerator = new Random();
    private String id;
    private boolean isHost = false;
    private Integer ballNum;

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
                            Log.d("BallNum ", String.valueOf(ballNum));
                        } catch (Exception e) {
                            Log.d("Exception: ", String.valueOf(e));
                        }
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
            }
        }
    }

    public int getBall() {
        int number = 0;
        if (isHost) {
            while (!usedNumbers.contains(number = randomGenerator.nextInt(100) + 1)) {
                mDatabase.child("Games").child("Bingo").child("Hosting").child("Ball").setValue(number);
                return number;
            }
        }
        else {
            number = ballNum;
        }
        return number;
    }

    public void reset() {
        mDatabase.child("Games").child("Bingo").child("Hosting").child("id").setValue("empty");
        mDatabase.child("Games").child("Bingo").child("Hosting").child("Ball").setValue("empty");
        bingoBoard.clear();
        usedNumbers.clear();

    }

    public void playerTurn(int ball) {

    }

    public boolean checkWin() {


        return false;
    }
}
