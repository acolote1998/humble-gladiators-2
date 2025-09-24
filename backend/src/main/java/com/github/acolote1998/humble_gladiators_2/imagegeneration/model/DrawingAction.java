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

    public DrawingAction(int drawingMethod, int initialX, int initialY, int red, int green, int blue, int alpha, int size, int width, int height, int radius) {
        this.drawingMethod = drawingMethod;
        this.initialX = initialX;
        this.initialY = initialY;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.size = size;
        this.width = width;
        this.height = height;
        this.radius = radius;
    }


    //DRAWING METHOD 0
    public static List<Pixel> drawSquare(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        for (int dx = 0; dx < action.size; dx++) {
            for (int dy = 0; dy < action.size; dy++) {
                pixelsToDraw.add(new Pixel(action.initialX + dx, action.initialY + dy, action.red, action.green, action.blue, action.alpha));
            }
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD 1
    public static List<Pixel> drawRectangle(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int width = action.getWidth();
        int height = action.getHeight();
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                pixelsToDraw.add(new Pixel(
                        action.getInitialX() + dx,
                        action.getInitialY() + dy,
                        action.getRed(),
                        action.getGreen(),
                        action.getBlue(),
                        action.getAlpha()
                ));
            }
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD 2
    public static List<Pixel> drawHorizontalLine(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int length = action.getWidth(); // width field used as line length
        for (int dx = 0; dx < length; dx++) {
            pixelsToDraw.add(new Pixel(
                    action.getInitialX() + dx,
                    action.getInitialY(),
                    action.getRed(),
                    action.getGreen(),
                    action.getBlue(),
                    action.getAlpha()
            ));
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD 3
    public static List<Pixel> drawVerticalLine(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int length = action.getHeight(); // use height as line length

        for (int dy = 0; dy < length; dy++) {
            pixelsToDraw.add(new Pixel(
                    action.getInitialX(),
                    action.getInitialY() + dy,
                    action.getRed(),
                    action.getGreen(),
                    action.getBlue(),
                    action.getAlpha()
            ));
        }

        return pixelsToDraw;
    }

    //DRAWING METHOD 4
    public static List<Pixel> drawCircle(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int radius = action.getRadius();
        int cx = action.getInitialX();
        int cy = action.getInitialY();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx * dx + dy * dy <= radius * radius) {
                    pixelsToDraw.add(new Pixel(
                            cx + dx,
                            cy + dy,
                            action.getRed(),
                            action.getGreen(),
                            action.getBlue(),
                            action.getAlpha()
                    ));
                }
            }
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD 5
    public static List<Pixel> drawHollowSquare(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int size = action.getSize();
        for (int i = 0; i < size; i++) {
            // Top and bottom edges
            pixelsToDraw.add(new Pixel(action.getInitialX() + i, action.getInitialY(), action.getRed(), action.getGreen(), action.getBlue(), action.getAlpha()));
            pixelsToDraw.add(new Pixel(action.getInitialX() + i, action.getInitialY() + size - 1, action.getRed(), action.getGreen(), action.getBlue(), action.getAlpha()));
            // Left and right edges
            pixelsToDraw.add(new Pixel(action.getInitialX(), action.getInitialY() + i, action.getRed(), action.getGreen(), action.getBlue(), action.getAlpha()));
            pixelsToDraw.add(new Pixel(action.getInitialX() + size - 1, action.getInitialY() + i, action.getRed(), action.getGreen(), action.getBlue(), action.getAlpha()));
        }
        return pixelsToDraw;
    }
}
