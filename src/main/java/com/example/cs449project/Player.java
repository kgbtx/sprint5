package com.example.cs449project;

import javafx.scene.paint.Color;

public class Player {

    public enum PlayerType {
        Human,
        Computer
    }

    public enum PlayerTeamColor {
        Blue,
        Red
    }

    public enum Symbol {
        S,
        O
    }

    private PlayerTeamColor playerTeamColor;
    private PlayerType playerType;
    private Symbol currentSymbol;
    private int noOfSOS;

    public Player() {
        this(PlayerTeamColor.Blue, PlayerType.Human);
    }
    public Player(PlayerTeamColor playerTeamColor, PlayerType playerType) {
        this.playerTeamColor = playerTeamColor;
        this.playerType = playerType;
        this.currentSymbol = Symbol.S;
        this.noOfSOS = 0;
    }

    public String getName() {
        return this.playerTeamColor.name() + " player";
    }

    public String getFullName() {
        return getPlayerType() + " - " + getName();
    }

    public Symbol getCurrentSymbol() {
        return this.currentSymbol;
    }

    public void setCurrentSymbol(Symbol symbol) {
        this.currentSymbol = symbol;
    }

    public Color getPlayerColor() {
        if (playerTeamColor == PlayerTeamColor.Red) {
            return Color.RED;
        } else if (playerTeamColor == PlayerTeamColor.Blue) {
            return Color.BLUE;
        }
        return Color.TRANSPARENT;
    }

    public int getNoOfSOS() {
        return noOfSOS;
    }

    public void setNoOfSOS(int noOfSOS) {
        this.noOfSOS = noOfSOS;
    }

    public void incrementNoOfSOS() {
        this.noOfSOS++;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public PlayerTeamColor getPlayerTeamColor() {
        return playerTeamColor;
    }

    public void setPlayerTeamColor(PlayerTeamColor playerTeamColor) {
        this.playerTeamColor = playerTeamColor;
    }


    public Symbol selectCurrentSymbol() {
        return null;
    }

    public int selectRow() {
        return -1;
    }

    public int selectCol() {
        return -1;
    }
}
