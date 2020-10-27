package com.example.gameclub.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientNetwork implements Runnable{

    public void run() {

        try{
            /*make connection to server socket */
            Socket sock = new Socket("10.0.2.2", 6013,null,2222);

            System.out.println(sock.getPort());
            InputStream in = sock.getInputStream();
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));

            /*read the data from the socket*/
            String line;
            while ((line = bin.readLine()) != null)
                System.out.println(line);
            /*close the socket connection*/
            sock.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}