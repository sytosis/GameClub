package com.example.gameclub.Network;

import java.util.ArrayList;

/**
 * game interface allows server and client to send over game information.
 * In future applications servers would implement serialization so game states can be sent more easily.
 * however, our current games did not require them.
 * @author John Roby John
 */
public interface Game {

    /**
     * sends game game board information using arrays;
     * @return game board state
     */
    public ArrayList<Integer> gameBoard();

    /**
     * sets game information
     * @param arrayList game information
     */
    public void setGame(ArrayList<Integer> arrayList);

    /**
     * returns the type of game being played
     * @return string of game being played
     */
    public String typeof();

}
