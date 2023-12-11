package com.example.cs449project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player1;
    private Player player2;

    @BeforeEach
    public void beforeEach() {
         player1 = new Player(Player.PlayerTeamColor.Blue, Player.PlayerType.Human);
         player2 = new Player(Player.PlayerTeamColor.Red, Player.PlayerType.Human);
    }

    @Test
    void getName() {
        assertEquals(player1.getName(), "Blue player");
        assertEquals(player2.getName(), "Red player");
    }

    @Test
    void getCurrentSymbol() {
        assertEquals(player1.getCurrentSymbol(), Player.Symbol.S);
        assertEquals(player2.getCurrentSymbol(), Player.Symbol.S);
    }

    @Test
    void setCurrentSymbol() {
        player1.setCurrentSymbol(Player.Symbol.O);
        assertEquals(player1.getCurrentSymbol(), Player.Symbol.O);
    }
}