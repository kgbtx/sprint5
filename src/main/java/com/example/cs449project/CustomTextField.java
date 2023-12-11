/**
 * CustomTextField is a text field that has lines that can be
 * activated to indicate the formation of an SOS
 */
package com.example.cs449project;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class CustomTextField extends TextField {
    public enum Direction {
        VERTICAL,
        HORIZONTAL,
        TOPDOWNDIAGONAL,
        BOTTOMUPDIAGONAL
    }
    private Pane linesPane;
    private Line horizontalLine;
    private Line verticalLine;
    private Line topDownDiagonalLine;
    private Line bottomUpDiagonalLine;
    public CustomTextField() {
        // Create a Pane to hold the lines
        linesPane = new Pane();
        getChildren().add(linesPane);

        // Initialize the lines
        horizontalLine = new Line();
        verticalLine = new Line();
        topDownDiagonalLine = new Line();
        bottomUpDiagonalLine = new Line();

        // Initially hide the lines
        horizontalLine.setVisible(false);
        verticalLine.setVisible(false);
        topDownDiagonalLine.setVisible(false);
        bottomUpDiagonalLine.setVisible(false);

        // Set line properties
        horizontalLine.setStroke(Color.RED);
        verticalLine.setStroke(Color.BLUE);
        topDownDiagonalLine.setStroke(Color.GREEN);
        bottomUpDiagonalLine.setStroke(Color.BLUEVIOLET);

        // Add the lines to the linesPane
        linesPane.getChildren().addAll(horizontalLine, verticalLine, topDownDiagonalLine, bottomUpDiagonalLine);


        layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.getWidth();
            double height = newValue.getHeight();

            // Adjust the position of lines to center them within the content area
            double textPaddingLeft = getPadding().getLeft();
            double textPaddingTop = getPadding().getTop();

            // Adjust horizontal line position
            updateLinePositions(horizontalLine, 0 - textPaddingLeft, newValue.getHeight() / 2 - textPaddingTop, newValue.getWidth() - textPaddingLeft, newValue.getHeight() / 2 - textPaddingTop);

            // Adjust vertical line position
            updateLinePositions(verticalLine, newValue.getWidth() / 2 - textPaddingLeft, 0 - textPaddingTop, newValue.getWidth() / 2 - textPaddingLeft, newValue.getHeight() - textPaddingTop);

            // Adjust top-down diagonal line position
            updateLinePositions(topDownDiagonalLine, 0-textPaddingLeft, 0-textPaddingTop, width-textPaddingLeft, height-textPaddingTop);

            // Adjust bottom-up diagonal line position
            updateLinePositions(bottomUpDiagonalLine, 0-textPaddingLeft, height - textPaddingTop, width - textPaddingLeft, 0-textPaddingTop);
        });
    }

    public void activateLines(boolean horizontal, boolean vertical, boolean tddiagonal, boolean budiagonal) {
        linesPane.setVisible(horizontal || vertical || tddiagonal || budiagonal);
        horizontalLine.setVisible(horizontal);
        verticalLine.setVisible(vertical);
        topDownDiagonalLine.setVisible(tddiagonal);
        bottomUpDiagonalLine.setVisible(budiagonal);
    }

    public void activateLine(Direction direction, Color color) {
        linesPane.setVisible(true);
        if (direction == Direction.VERTICAL) {
            verticalLine.setStroke(color);
            verticalLine.setVisible(true);
        } else if (direction == Direction.HORIZONTAL) {
            horizontalLine.setStroke(color);
            horizontalLine.setVisible(true);
        } if (direction == Direction.TOPDOWNDIAGONAL) {
            topDownDiagonalLine.setStroke(color);
            topDownDiagonalLine.setVisible(true);
        } if (direction == Direction.BOTTOMUPDIAGONAL) {
            bottomUpDiagonalLine.setStroke(color);
            bottomUpDiagonalLine.setVisible(true);
        }
    }

    private void updateLinePositions(Line line, double x1, double y1, double x2, double y2) {
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);
    }
}
