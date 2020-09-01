package com.example.gameclub.ui.Network;
import java.net.*;
import java.io.*;
public class ServerNetwork {
        public  static void main (String[] args) {
            try {
                ServerSocket sock = new ServerSocket(6013);
                /*Listen for connections */
                while(true) {
                    Socket client = sock.accept();
                    PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
                    /*write the data to the socket*/
                    pout.println(new java.util.Date().toString());

                    /*close the socket and resume */
                    /*listening for connections*/
                    client.close();
                }
            } catch (IOException e) {
                System.err.println(e);
            }


        }
}
