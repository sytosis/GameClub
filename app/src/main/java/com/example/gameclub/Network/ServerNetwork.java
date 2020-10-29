package com.example.gameclub.Network;

import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.example.gameclub.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerNetwork implements Runnable {
    private TextView text;
    private Socket client;
    private ChessGame chessGame;
    private int turn = 1;

    public ServerNetwork( ChessGame newChessGame) {

        chessGame = newChessGame;
    }

    public void run() {

        try {
            System.out.println("here");
            ServerSocket sock = new ServerSocket(6013);
            client = sock.accept();
            InputStream in = client.getInputStream();
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            PrintWriter pout = new PrintWriter(client.getOutputStream(),true);

            /*read the data from the socket*/
            String line;
            /*Listen for connections */
            while (true) {
                if(turn == 1) {
                    synchronized (chessGame) {
                        try {

                            chessGame.wait();
                        } catch (InterruptedException ne) {
                            System.err.println(ne);
                        }
                    }
                    System.out.println("I got up to here");
                    ArrayList<Integer> array = chessGame.gameBoard();
                    if (array.get(0) == -1 && array.get(1) == -1 && array.get(2) == -1
                            && array.get(3) == -1) {
                        break;
                    }
                    System.out.println(chessGame.gameBoard().toString());
                    sendMessage(chessGame);
                    turn = 0;
                } else {
                    line = bin.readLine();
                    String[] arrOfStr = line.split(":", 4);
                    Integer x;
                    Integer y;
                    Integer newx;
                    Integer newy;
                    x = Integer.parseInt(arrOfStr[0]);
                    y = Integer.parseInt(arrOfStr[1]);
                    newx = Integer.parseInt(arrOfStr[2]);
                    newy = Integer.parseInt(arrOfStr[3]);
                    List<Integer> list = new ArrayList<>();
                    list.add(0, x);
                    list.add(1, y);
                    list.add(2, newx);
                    list.add(3, newy);
                    if (list.get(0) == -1 && list.get(0) == -1 && list.get(0) == -1
                            && list.get(0) == -1) {
                        break;
                    }
                    chessGame.getChessViewModel().getMove().postValue(list);
                    turn = 1;


                }


            }
            client.close();
        } catch(IOException e) {
            System.err.println(e);
        }
    }


    public void sendMessage(Game game) throws IOException {
        PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
        /*write the data to the socket*/
        pout.println(game.gameBoard().toString());
    }

    public void closeClient() throws IOException {
        client.close();
    }
}