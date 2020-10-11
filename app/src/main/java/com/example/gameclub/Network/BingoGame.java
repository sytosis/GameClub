package com.example.gameclub.Network;

import java.util.List;

public class BingoGame implements Game {

    private int number;
    private List<Chat> chat;
    public BingoGame() {

    }

    public String gameBoard() {

        return "";
    }

    public void chat(Chat newMessage) {

        chat.add(newMessage);

    }

    public void setGame(String string) {
        number = Integer.parseInt(string);
    }

    public String typeof() {

        return "Bingo";
    }

    public void decodeMessage(String string) {
        Chat newChat = new Chat();
        String[] array  = string.split(":",0);

        newChat.setSender(array[1]);
        newChat.setReceiver(array[2]);
        newChat.setMessage(array[3]);
        chat.add(newChat);
    }

}