package com.example.gameclub.Games.Bingo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.lifecycle.ViewModel;

import com.example.gameclub.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bingo extends ViewModel {
    public List<Integer> bingoBoard = new ArrayList<>(25);

    List<Integer> usedNumbers = new ArrayList<>();
    Random randomGenerator = new Random();

    public Bingo() {
        setBoard();
    }

    public void setBoard() {
        int number;
        for (int i = 0; i < 25; ++i) {

            while(!bingoBoard.contains(number = randomGenerator.nextInt(100) + 1)) {
                bingoBoard.add(number);
                break;
            }
        }
    }

    public int getBall() {
        int number;
        while(!bingoBoard.contains(number = randomGenerator.nextInt(100) + 1)) {
            return number;
        }
        return 0;
    }

    public void takeTurn() {
        int ball = getBall();

//        if (bingoBoard.contains(ball)) {
//
//        }
    }

    public void reset() {
        bingoBoard.clear();
        usedNumbers.clear();

    }

    public boolean checkWin() {
        return false;
    }
}
