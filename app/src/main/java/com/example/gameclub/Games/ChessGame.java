package com.example.gameclub.Games;

import android.view.View;


import java.util.ArrayList;

/**
 * Chess game is the state information of the chess game being played. It only sends out the
 * user selected piece and it's new destination. Meaning the selected piece are to move to the
 * new destination.
 */
public class ChessGame implements Game {

    private ChessViewModel chessViewModel;
    private ArrayList<Integer> array;
    private View root;
    private Boolean onWhite = true;

    /**
     * creates a chess game
     * @param chessModel The chessViewModel
     * @param rootSave Root of the fragment.
     */
    public ChessGame(ChessViewModel chessModel,  View rootSave) {
        chessViewModel = chessModel;
        root = rootSave;
        array = new ArrayList<Integer>();
    }

    /**
     * sends game game board information using arrays;
     * @return game board state
     */
    public ArrayList<Integer> gameBoard(){
        ArrayList<Integer> newArray = new ArrayList<Integer>();
        newArray.add(0,array.get(0));
        newArray.add(1,array.get(1));
        newArray.add(2,array.get(2));
        newArray.add(3,array.get(3));

        return newArray;
    }


    /**
     * sets game information
     * @param arrayList game information
     */
    public void setGame(ArrayList<Integer> arrayList){
        array.add(0,arrayList.get(0));
        array.add(1,arrayList.get(1));
        array.add(2,arrayList.get(2));
        array.add(3,arrayList.get(3));
    }

    /**
     * return the chessViewModel
     * @return return the chessViewModel
     */
    public ChessViewModel getChessViewModel() {

        return chessViewModel;
    }

    /**
     * returns the type of game being played
     * @return string of game being played
     */
    public String typeof(){

        return "chess";
    }

}
