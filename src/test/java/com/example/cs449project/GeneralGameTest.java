package com.example.cs449project;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GeneralGameTest {
    private Game game;

    @Before
    public void setUp() {
        game = new GeneralGame();
    }

    @Test
    public void testSetCellState() {
        game.setGameMode(Game.GameMode.General);
        Player player1 = game.getPlayers()[0];
        Player player2 = game.getPlayers()[1];
        game.setGameStarted(true);
        game.setCellState(0, 0);

        // After setting a cell
        assertNotNull(game.getCellState(0,0).toString());
    }

    @Test
    public void testGameOver() {
        game.setGameMode(Game.GameMode.General);

        // Test when the game is not over
        assertFalse(game.gameOver());

        // Test when the game is over
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                game.setCellState(i, j);
            }
        }
        assertTrue(game.gameOver());
    }
}
