package com.example.gameclub.Network;

import android.widget.TextView;

import com.example.gameclub.Games.ChessGame;
import com.example.gameclub.Games.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class opens up a server socket to listen to.
 * @author John Roby John
 */
public class ServerNetwork implements Runnable {
    private TextView text;
    private Socket client;
    private ChessGame chessGame;
    private int turn = 1;

    /**
     * sets up the Game class.
     * @param newChessGame
     */
    public ServerNetwork( ChessGame newChessGame) {

        chessGame = newChessGame;
    }

    /**
     * run when parent calls start on this class.
     * When run it opens a socket and waits for a client to accept.
     * After which the game loop will start
     */
    public void run() {

        try {
            /* opens server socket */
            ServerSocket sock = new ServerSocket(6013);
            /* Listens for connections */
            client = sock.accept();
            /* read in from client */
            InputStream in = client.getInputStream();
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            /*prints out from server */
            PrintWriter pout = new PrintWriter(client.getOutputStream(),true);


            String line;

            while (true) {
                if(turn == 1) {
                    /* if it is the users turn serverNetwork synchronises with chessGame in parent
                     to send game state to server */
                    synchronized (chessGame) {
                        try {

                            chessGame.wait();
                        } catch (InterruptedException ne) {
                            System.err.println(ne);
                        }
                    }
                    /* checks if game is over */
                    ArrayList<Integer> array = chessGame.gameBoard();
                    if (array.get(0) == -1 && array.get(1) == -1 && array.get(2) == -1
                            && array.get(3) == -1) {
                        break;
                    }

                    sendMessage(chessGame);
                    turn = 0;
                } else {
                    /* runs if clients turn */
                    /* read string from socket */
                    line = bin.readLine();
                    String[] arrOfStr = line.split(":", 4);

                    List<Integer> list = new ArrayList<>();
                    list.add(0, Integer.parseInt(arrOfStr[0]));
                    list.add(1, Integer.parseInt(arrOfStr[1]));
                    list.add(2, Integer.parseInt(arrOfStr[2]));
                    list.add(3, Integer.parseInt(arrOfStr[3]));
                    /* checks if game is over */
                    if (list.get(0) == -1 && list.get(0) == -1 && list.get(0) == -1
                            && list.get(0) == -1) {
                        break;
                    }
                    chessGame.getChessViewModel().getMove().postValue(list);
                    turn = 1;
                }
            }
            closeClient();
        } catch(IOException e) {
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
     * closes client
     * @throws IOException
     */
    private void closeClient() throws IOException {
        client.close();
    }
}