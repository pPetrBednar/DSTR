package io.github.ppetrbednar.dstr.logic;

import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class RailwayVisualizer {
    private AnchorPane pane;
    private final int cellCountWidth = 60;
    private final int cellCountHeight = 30;
    private double cellSizeWidth;
    private double cellSizeHeight;

    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }

    public void recalculate() {
        cellSizeWidth = pane.getWidth() / cellCountWidth;
        cellSizeHeight = pane.getHeight() / cellCountHeight;
    }

    public void printGrid() {
        for (int i = 0; i < cellCountHeight; i++) {
            for (int j = 0; j < cellCountWidth; j++) {
                Circle c = new Circle();
                c.setFill(Color.BLACK);
                c.setRadius(2);
                c.setLayoutX((j + 0.5) * cellSizeWidth);
                c.setLayoutY((i + 0.5) * cellSizeHeight);

                int finalI = i;
                int finalJ = j;
                c.setOnMousePressed(mouseEvent -> {
                    System.out.println(finalI + " - " + finalJ);
                });
                c.setCursor(Cursor.HAND);

                pane.getChildren().add(c);
            }
        }
    }
}
