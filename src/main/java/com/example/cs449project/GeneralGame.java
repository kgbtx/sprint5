package com.example.cs449project;

public class GeneralGame extends Game {
    public GeneralGame() {
        super();
    }

    /**
     * Checks if the game is over. A general game is over if all cells have been filled.
     * @return true if all cells have been filled, false otherwise
     */
    public boolean gameOver() {
        if (allCellsFilled()) {
            return true;
        }
        if (!checkSOS()) {
            changeCurrentPlayer();
        }
        return computerPlay();
    }
}
