package com.example.gameclub.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gameclub.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChessViewModel extends ViewModel {
    private int[] selectedPiece= new int[2];
    private boolean isWhite;
    private String[][] chessBoard = new String[8][8];

    public ChessViewModel() {

    }


    public void setChessBoard() {
        chessBoard[0][0] = "wr";
        chessBoard[1][0] = "wn";
        chessBoard[2][0] = "wi";
        chessBoard[3][0] = "wq";
        chessBoard[4][0] = "wk";
        chessBoard[5][0] = "wi";
        chessBoard[6][0] = "wn";
        chessBoard[7][0] = "wr";
        chessBoard[0][1] = "wp";
        chessBoard[1][1] = "wp";
        chessBoard[2][1] = "wp";
        chessBoard[3][1] = "wp";
        chessBoard[4][1] = "wp";
        chessBoard[5][1] = "wp";
        chessBoard[6][1] = "wp";
        chessBoard[7][1] = "wp";

        chessBoard[0][7] = "br";
        chessBoard[1][7] = "bn";
        chessBoard[2][7] = "bi";
        chessBoard[3][7] = "bq";
        chessBoard[4][7] = "bk";
        chessBoard[5][7] = "bi";
        chessBoard[6][7] = "bn";
        chessBoard[7][7] = "br";
        chessBoard[0][6] = "bp";
        chessBoard[1][6] = "bp";
        chessBoard[2][6] = "bp";
        chessBoard[3][6] = "bp";
        chessBoard[4][6] = "bp";
        chessBoard[5][6] = "bp";
        chessBoard[6][6] = "bp";
        chessBoard[7][6] = "bp";

        for (int i = 2; i < 6; i++) {
            for (int z = 0; z < 8; z++) {
                chessBoard[z][i] = null;
            }
        }

        selectedPiece[0] = -1;
        selectedPiece[1] = -1;
    }

    public boolean selectChessPiece(int x, int y) {
        if (selectedPiece[0] == x && selectedPiece[1] == y) {
            selectedPiece[0] = -1;
            selectedPiece[1] = -1;
            return false;
        }
        if (chessBoard[x][y] != null) {
            selectedPiece[0] = x;
            selectedPiece[1] = y;
            return true;
        } else {
            return false;
        }
    }

    public String[][] getChessBoard() {
        return chessBoard;
    }
    public void moveSelectedPiece(int x, int y) {
        String piece = chessBoard[selectedPiece[0]][selectedPiece[1]];
        chessBoard[selectedPiece[0]][selectedPiece[1]] = null;
        chessBoard[x][y] = piece;
    }
    public int[] getSelectedPiece() {
        return selectedPiece;
    }
}