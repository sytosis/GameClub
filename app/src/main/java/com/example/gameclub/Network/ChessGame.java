package com.example.gameclub.Network;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.gameclub.R;
import com.example.gameclub.ui.gallery.ChessViewModel;

public class ChessGame implements Game{

    private ChessViewModel chessViewModel;

    View root;
    Boolean onWhite = true;
    public ChessGame(ChessViewModel chessModel,  View rootSave) {
        chessViewModel = chessModel;
        root = rootSave;
    }

    public String gameBoard(){
        return " ";
    }

    public void chat(Chat newMessage){

    }

    public void setGame(String string){

    }

    public ChessViewModel getChessViewModel() {

        return chessViewModel;
    }
    public String typeof(){

        return "chess";
    }

}
