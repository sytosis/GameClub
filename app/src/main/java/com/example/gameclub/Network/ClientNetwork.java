package com.example.gameclub.Network;

import android.widget.TextView;

import com.example.gameclub.Games.ChessGame;
import com.example.gameclub.Games.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class opens up a client socket to listen to.
 * @author John Roby John
 */
public class ClientNetwork implements Runnable {
    private TextView text;
    private Socket client;
    private ChessGame chessGame;
    private int turn;
    /**
     * sets up the Game class.
     * @param newChessGame
     */
    public ClientNetwork(ChessGame newChessGame) {
        turn = 0;
        chessGame = newChessGame;
    }

    /**
     * run when parent calls start on this class.
     * When run it opens a socket and waits for a client to accept.
     * After which the game loop will start
     */
    public void run() {

        try {
            /*make connection to server socket */
            Socket sock = new Socket("10.0.2.2", 6013, null, 2222);

            InputStream in = sock.getInputStream();
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));


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
                    ArrayList<Integer> array = chessGame.gameBoard();
                    if (array.get(0) == -1 && array.get(1) == -1 && array.get(2) == -1
                            && array.get(3) == -1) {
                        break;
                    }

                    sendMessage(chessGame);
                    turn = 0;
                } else {
                    /*runs if servers turn*/
                    /* reads from socket */

                    line = bin.readLine();

                    String[] arrOfStr = line.split(":", 4);
                    List<Integer> list = new ArrayList<>();
                    list.add(0, Integer.parseInt(arrOfStr[0]));
                    list.add(1, Integer.parseInt(arrOfStr[1]));
                    list.add(2, Integer.parseInt(arrOfStr[2]));
                    list.add(3, Integer.parseInt(arrOfStr[3]));
                    if (list.get(0) == -1 && list.get(0) == -1 && list.get(0) == -1
                        && list.get(0) == -1) {
                        break;
                    }
                    chessGame.getChessViewModel().getMove().postValue(list);
                    turn = 1;

                }
            }
            closeClient();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Sends the game moves to client
     * @param game the game information
     * @throws IOException
     */
    private void sendMessage(Game game) throws IOException {
        PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
        ArrayList<Integer> array = game.gameBoard();
        /*write the data to the socket*/
        pout.println(array.get(0)+":"+array.get(1)+":"+array.get(2)+":"+array.get(3));
    }

    /**
     * Closes client
     * @throws IOException
     */
    public void closeClient() throws IOException {
        client.close();
    }

}
