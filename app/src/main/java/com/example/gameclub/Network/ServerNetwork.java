package com.example.gameclub.Network;

import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.gameclub.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerNetwork implements Runnable {
    TextView text;
    Socket client;
    ChessGame chessGame;
    public ServerNetwork(TextView view, ChessGame newChessGame) {
        text = view;
        chessGame = newChessGame;
    }

    public void run() {

        try {
            System.out.println("here");
            ServerSocket sock = new ServerSocket(6013);

            /*Listen for connections */
            while (true) {
                client = sock.accept();
                InputStream in = client.getInputStream();
                BufferedReader bin = new BufferedReader(new InputStreamReader(in));

                /*read the data from the socket*/
                String line;
                while ((line = bin.readLine()) != null) {
                    String[] arrOfStr = line.split(":",1);
                    Integer x;
                    Integer y;
                    x = Integer.parseInt(arrOfStr[0]);
                    y = Integer.parseInt(arrOfStr[1]);
                    chessGame.getChessViewModel().getMove().setValue(Pair.create(x,y));
                }

                //tem.out.println("out here");
                //PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
                /*write the data to the socket*/
                //pout.println();
                //text.setText("Server Connected");
                //System.out.println(client.getPort());
                //System.out.println(client.getLocalPort());
                //pout.println(new java.util.Date().toString());
                /*close the socket and resume */
                /*listening for connections*/
                client.close();
            }
        } catch(IOException e) {
            System.err.println(e);
        }
    }


    public void sendMessage(Game game) throws IOException {
        PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
        /*write the data to the socket*/
        pout.println();
        text.setText("Server Connected");
        /*close the socket and resume */
        /*listening for connections*/
    }

    public void closeClient() throws IOException {
        client.close();
    }
}