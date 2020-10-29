package com.example.gameclub.Network;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientNetwork implements Runnable {
    private TextView text;
    private Socket client;
    private ChessGame chessGame;
    private int turn;

    public ClientNetwork(ChessGame newChessGame) {
        turn = 0;
        chessGame = newChessGame;
    }


    public void run() {

        try {
            /*make connection to server socket */
            Socket sock = new Socket("10.0.2.2", 6013, null, 2222);

            System.out.println(sock.getPort());
            InputStream in = sock.getInputStream();
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));

            /*read the data from the socket*/
            String line;
            while (true) {
                if (turn == 1) {
                    synchronized (chessGame) {
                        try {

                            chessGame.wait();
                        } catch (InterruptedException ne) {
                            System.err.println(ne);
                        }
                    }
                } else {
                    System.out.println("I got up to here");
                    ArrayList<Integer> array = chessGame.gameBoard();
                    if (array.get(0) == -1 && array.get(1) == -1 && array.get(2) == -1
                            && array.get(3) == -1) {
                        break;
                    }
                    System.out.println(chessGame.gameBoard().toString());
                    sendMessage(chessGame);
                    while ((line = bin.readLine()) != null) {
                        System.out.println(line);
                        /*close the socket connection*/
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
                        break;
                    }
                }
            }

        } catch (IOException e) {
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
