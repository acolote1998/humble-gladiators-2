package com.github.acolote1998.humble_gladiators_2.imagegeneration.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DrawingAction {
    private int drawingMethod;
    private int initialX;
    private int initialY;
    private int red;
    private int green;
    private int blue;
    private int alpha;

    // Optional parameters for shapes
    private int size;       // For square or hollow square
    private int width;      // For rectangle
    private int height;     // For rectangle
    private int radius;     // For circle

    public DrawingAction(int drawingMethod, int initialX, int initialY, int red, int green, int blue, int alpha) {
        this.drawingMethod = drawingMethod;
        this.initialX = initialX;
        this.initialY = initialY;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public static List<Pixel> drawSquare(int squareSize, DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        for (int dx = 0; dx < squareSize; dx++) {
            for (int dy = 0; dy < squareSize; dy++) {
                pixelsToDraw.add(new Pixel(action.initialX + dx, action.initialY + dy, action.red, action.green, action.blue, action.alpha));
            }
        }
        return pixelsToDraw;
    }

}
