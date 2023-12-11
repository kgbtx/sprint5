package com.example.cs449project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void setGameModeTest() {
        game.setGameMode(Game.GameMode.General);
        assertEquals(game.getGameMode(), Game.GameMode.General);
        game.setGameMode(Game.GameMode.Simple);
        assertEquals(game.getGameMode(), Game.GameMode.Simple);
    }

    @Test
    void getGameModeTest() {
        assertEquals(game.getGameMode(), Game.GameMode.Simple);
    }

    @Test
    void getPlayersTest() {
        Player[] players = game.getPlayers();
        assertEquals(players.length, 2);
        assertEquals(players[0].getName(), "Blue player");
        assertEquals(players[1].getName(), "Red player");
    }

    @Test
    void getCurrentPlayerTest() {
        assertEquals(game.getCurrentPlayer().getName(), "Blue player");
    }

    @Test
    void getBoardSizeTest() {
        assertEquals(game.getBoardSize(), Game.INITIAL_BOARD_SIZE);
    }

    @Test
    void setBoardSizeTest() {
        game.setBoardSize(5);
        assertEquals(game.getBoardSize(), 5);
        game.setBoardSize(2);
        assertEquals(game.getBoardSize(), 5);
        game.setBoardSize(13);
        assertEquals(game.getBoardSize(), 5);
    }

    @Test
    void makeMoveTest() {
        assert(game.isValidMove(0,0));
        game.setCellState(0,0);
        assertFalse(game.isValidMove(0,0));
    }

    @Test
    void changeCurrentPlayerTest() {
        assertEquals(game.getCurrentPlayer().getName(), "Blue player");
        game.changeCurrentPlayer();
        assertEquals(game.getCurrentPlayer().getName(), "Red player");
    }

    @Test
    void isGameStartedTest() {
        assertFalse(game.isGameStarted());
    }

    @Test
    void setGameStartedTest() {
        assertFalse(game.isGameStarted());
        game.setGameStarted(true);
        assert(game.isGameStarted());
    }

    @Test
    void getCellStateTest() {
        assertNull(game.getCellState(0, 0));
    }

    @Test
    void setCellStateTest() {
        assertNull(game.getCellState(0, 0));
        game.setCellState(0,0);
        assertEquals(game.getCellState(0,0).getSymbol().toString(), "S");
    }

    @Test
    void computerPlayTest() {
        game.getCurrentPlayer().setPlayerType(Player.PlayerType.Computer);
        assertEquals(game.getCurrentPlayer().getPlayerType(), Player.PlayerType.Computer);
        game.startGame();
    }
}