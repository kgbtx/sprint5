package com.example.cs449project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class SimpleGameTest {
    private Game game;

    @BeforeEach
    public void setUp() {
        game = new SimpleGame();
    }

    @Test
    public void testSetCellState() {
        game.setGameMode(Game.GameMode.Simple);
        Player player1 = game.getPlayers()[0];
        Player player2 = game.getPlayers()[1];
        game.startGame();
        game.setCellState(0, 0);

        // After setting a cell, the current player should change
        Assertions.assertNotNull(game.getCellState(0,0).toString());
    }
    @Test
    public void testGameOver() {
        game.setGameMode(Game.GameMode.Simple);

        // Test when the game is not over
        Assertions.assertFalse(game.gameOver());

        // Test when the game is over as a draw
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                game.setCellState(i, j);
            }
        }
        Assertions.assertTrue(game.gameOver());

        // Test when the game is over as a win
        Player player1 = game.getPlayers()[0];
        player1.incrementNoOfSOS();
        Assertions.assertTrue(game.gameOver());
    }
}
