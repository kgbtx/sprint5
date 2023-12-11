/**
 * This class represents a set of 3 cells that together make an SOS
 * These combinations are used to create hashsets of SOS formations
 * to make it easier to identify new SOS formations after a move
 */
package com.example.cs449project;

import java.util.Objects;

public class SOSCombination {
    private int x1, y1;
    private int x2, y2;
    private int x3, y3;

    public SOSCombination(int x1, int y1, int x2, int y2, int x3, int y3) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }

    // Getters for individual cell coordinates
    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }
    public int getX3() { return x3; }
    public int getY3() { return y3; }

    // Implement equals and hashCode methods to compare SOS combinations
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SOSCombination that = (SOSCombination) o;
        return (x1 == that.x1 && y1 == that.y1 && x2 == that.x2 && y2 == that.y2 && x3 == that.x3 && y3 == that.y3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, y1, x2, y2, x3, y3);
    }
}

