package com.example.cs449project;

public class SimpleGame extends Game {
    public SimpleGame() {
        super();
    }

    /**
     * Checks if the game is over.
     * A simple game is over if an SOS has been formed or all cells have been filled.
     *
     * @return true if game is over, false otherwise
     */
    @Override
    public boolean gameOver() {
        checkSOS();
        boolean over = false;
        if (!sosCombinations.isEmpty() || allCellsFilled()) {
            over = true;
        } else if (sosCombinations.isEmpty() && !allCellsFilled()) {
            changeCurrentPlayer();
            over = computerPlay();
        }

        return over;
    }
}
