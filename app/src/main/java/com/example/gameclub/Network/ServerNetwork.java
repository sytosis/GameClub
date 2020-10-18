package com.example.gameclub.Network;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gameclub.R;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetwork implements Runnable {
    TextView text;
    Socket client;
    Game bingoGame = new BingoGame();
    public ServerNetwork(TextView view) {
        text = view;

    }

    public void run() {

        try {

            ServerSocket sock = new ServerSocket(6013);
            client = sock.accept();
            /*Listen for connections */
            while (true) {

                Button button = (Button) text.findViewById(R.id.roll_ball_button);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Do something in response to button click
                    }
                });

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