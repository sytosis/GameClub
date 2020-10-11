package com.example.gameclub.Network;
import java.net.*;
import java.io.*;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.gameclub.Network.ServerNetwork;
import com.example.gameclub.Network.ClientNetwork;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.gameclub.R;
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