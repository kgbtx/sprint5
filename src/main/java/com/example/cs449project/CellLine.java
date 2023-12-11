/**
 * CellLine class is a custom class representing a line cutting through a cell
 * It has color (blue or red) and direction (vertical, horizontal, diagonal)
 */
package com.example.cs449project;

import javafx.scene.paint.Color;

public class CellLine {

    private Color color;
    private CustomTextField.Direction direction;

    public CellLine(Color color, CustomTextField.Direction direction) {
        this.color = color;
        this.direction = direction;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public CustomTextField.Direction getDirection() {
        return direction;
    }

    public void setDirection(CustomTextField.Direction direction) {
        this.direction = direction;
    }
}
