package com.example.gameclub.Network;

import android.view.View;


import com.example.gameclub.Ui.Gallery.ChessViewModel;

import java.util.ArrayList;

public class ChessGame implements Game{

    private ChessViewModel chessViewModel;
    ArrayList<Integer> array;
    View root;
    Boolean onWhite = true;
    public ChessGame(ChessViewModel chessModel,  View rootSave) {
        chessViewModel = chessModel;
        root = rootSave;
        array = new ArrayList<Integer>();
    }

    public ArrayList<Integer> gameBoard(){
        ArrayList<Integer> newArray = new ArrayList<Integer>();
        newArray.add(0,array.get(0));
        newArray.add(1,array.get(1));
        newArray.add(2,array.get(2));
        newArray.add(3,array.get(3));

        return newArray;
    }

    public void chat(Chat newMessage){

    }

    public void setGame(ArrayList<Integer> arrayList){
        array.add(0,arrayList.get(0));
        array.add(1,arrayList.get(1));
        array.add(2,arrayList.get(2));
        array.add(3,arrayList.get(3));
    }

    public ChessViewModel getChessViewModel() {

        return chessViewModel;
    }
    public String typeof(){

        return "chess";
    }

}
