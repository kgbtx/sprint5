/**
 * ComputerPlayer class represents the special player of type computer
 * who does not play by interacting with the UI
 */
package com.example.cs449project;

import java.util.Random;

public class ComputerPlayer extends Player {
    public ComputerPlayer(PlayerTeamColor teamColor) {
        super();
        this.setPlayerType(PlayerType.Computer);
        this.setPlayerTeamColor(teamColor);
    }

    public Symbol selectCurrentSymbol() {
        Random random = new Random();
        int rand = random.nextInt(2);
        if (rand == 0) {
            return Symbol.S;
        } else {
            return Symbol.O;
        }
    }

    public int selectRow(int boardSize) {
        Random random = new Random();
        return random.nextInt(boardSize);
    }

    public int selectCol(int boardSize) {
        Random random = new Random();
        return random.nextInt(boardSize);
    }
}
