/**
 * Cell class represents a cell in the game board
 * It has a symbol and an arraylist of lines activated when a cell forms part of an SOS
 */
package com.example.cs449project;

import java.util.ArrayList;

public class Cell {
    private Player.Symbol symbol;
    private ArrayList<CellLine> lines;
    public Cell() {
        lines = new ArrayList<>();
    }

    public Player.Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Player.Symbol symbol) {
        this.symbol = symbol;
    }

    public ArrayList<CellLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<CellLine> lines) {
        this.lines = lines;
    }

    public void addLine(CellLine line) {
        this.lines.add(line);
    }
}
