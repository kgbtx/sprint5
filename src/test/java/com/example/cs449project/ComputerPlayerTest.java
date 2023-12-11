package com.example.cs449project;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComputerPlayerTest {
    ComputerPlayer computerPlayer;
    @BeforeEach
    void setUp() {
        computerPlayer = new ComputerPlayer(Player.PlayerTeamColor.Blue);
    }


    @Test
    void selectCurrentSymbolTest() {
        // Set symbol to null
        computerPlayer.setCurrentSymbol(null);
        // Let player pick randomly
        Player.Symbol symbol = computerPlayer.selectCurrentSymbol();
        // Assert symbol either O or S
        assertTrue(symbol.equals(Player.Symbol.S) || symbol.equals(Player.Symbol.O));
    }

    @Test
    void selectRowTest() {
        assertTrue(computerPlayer.selectRow(4) >= 0 &&
                computerPlayer.selectRow(4) < 4);
    }

    @Test
    void selectColTest() {
        assertTrue(computerPlayer.selectCol(4) >= 0 &&
                computerPlayer.selectCol(4) < 4);
    }
}