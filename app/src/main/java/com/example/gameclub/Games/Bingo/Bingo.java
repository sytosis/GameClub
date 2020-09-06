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

        while (bingoBoard.size() < 25) {
            number = randomGenerator.nextInt(100) + 1;
            if (!bingoBoard.contains(number)) {
                bingoBoard.add(number);
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

    public void reset() {
        bingoBoard.clear();
        usedNumbers.clear();

    }

    public void playerTurn(int ball) {

    }

    public boolean checkWin() {



        return false;
    }
}
