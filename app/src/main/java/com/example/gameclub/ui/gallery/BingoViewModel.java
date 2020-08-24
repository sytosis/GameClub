package com.example.gameclub.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BingoViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    Random randomGenerator = new Random();
    private List<List<Integer>> bingoBoard = new ArrayList<>();
    List<Integer> usedNumbers = new ArrayList<>();
    Integer[] bingoBackgroundNumbers = new Integer[100];
    int counter = 0;

    public BingoViewModel() {
        setBingoBoard();
        resetBingoBackground();
        mText = new MutableLiveData<>();
        mText.setValue(bingoBoard.toString());
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setBingoBoard() {
        bingoBoard = new ArrayList<>();
        usedNumbers = new ArrayList<>();    
        for (int i = 0; i < 5; i++) {
            List<Integer> line = new ArrayList<>();
            bingoBoard.add(line);
            while (bingoBoard.get(i).size() < 5) {
                int number = randomGenerator.nextInt(100) + 1;
                if (!usedNumbers.contains(number)) {
                    line.add(number);
                    usedNumbers.add(number);
                }
            }
        }
    }

    public int callBall() {
        while (counter != 99) {
            int number = bingoBackgroundNumbers[counter];
            counter = counter + 1;
            return number;
        }
        return 0;
    }

    public void resetBingoBackground() {
        counter = 0;
        bingoBackgroundNumbers = new Integer[100];
        for (int i = 0; i < bingoBackgroundNumbers.length; i++) {
            bingoBackgroundNumbers[i] = i + 1;
        }
        Collections.shuffle(Arrays.asList(bingoBackgroundNumbers));
        setBingoBoard();
    }

    public boolean checkBall(int number) {
        if (usedNumbers.contains(number)) {
            for (int i = 0; i < 5; i++) {
                if (bingoBoard.get(i).indexOf(number) != -1) {
                    bingoBoard.get(i).set(bingoBoard.get(i).indexOf(number),null);
                    return true;
                }
            }
        }
        return false;
    }

    public List<List<Integer>> getBingoBoard() {
        return bingoBoard;
    }
}