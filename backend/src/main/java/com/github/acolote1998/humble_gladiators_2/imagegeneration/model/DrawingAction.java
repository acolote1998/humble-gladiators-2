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


    //DRAWING METHOD "0" - SQUARE
    public static List<Pixel> drawSquare(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        for (int dx = 0; dx < action.size; dx++) {
            for (int dy = 0; dy < action.size; dy++) {
                pixelsToDraw.add(new Pixel(action.initialX + dx, action.initialY + dy, action.red, action.green, action.blue, action.alpha));
            }
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD "1" - RECTANGLE
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

    //DRAWING METHOD "2" - HORIZONTAL LINE
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

    //DRAWING METHOD "3" - VERTICAL LINE
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

    //DRAWING METHOD "4" - CIRCLE
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

    //DRAWING METHOD "5" - HOLLOW SQUARE
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

    //DRAWING METHOD "6" - DOT (single pixel)
    public static List<Pixel> drawDot(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        pixelsToDraw.add(new Pixel(
                action.getInitialX(),
                action.getInitialY(),
                action.getRed(),
                action.getGreen(),
                action.getBlue(),
                action.getAlpha()
        ));
        return pixelsToDraw;
    }

    //DRAWING METHOD "7" - TRIANGLE UP
    public static List<Pixel> drawTriangleUp(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int size = action.getSize();
        int centerX = action.getInitialX();
        int centerY = action.getInitialY();
        
        for (int dy = 0; dy < size; dy++) {
            int width = dy + 1;
            int startX = centerX - dy / 2;
            for (int dx = 0; dx < width; dx++) {
                pixelsToDraw.add(new Pixel(
                        startX + dx,
                        centerY + dy,
                        action.getRed(),
                        action.getGreen(),
                        action.getBlue(),
                        action.getAlpha()
                ));
            }
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD "8" - TRIANGLE DOWN
    public static List<Pixel> drawTriangleDown(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int size = action.getSize();
        int centerX = action.getInitialX();
        int centerY = action.getInitialY();
        
        for (int dy = 0; dy < size; dy++) {
            int width = size - dy;
            int startX = centerX - (size - dy - 1) / 2;
            for (int dx = 0; dx < width; dx++) {
                pixelsToDraw.add(new Pixel(
                        startX + dx,
                        centerY + dy,
                        action.getRed(),
                        action.getGreen(),
                        action.getBlue(),
                        action.getAlpha()
                ));
            }
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD "9" - TRIANGLE LEFT
    public static List<Pixel> drawTriangleLeft(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int size = action.getSize();
        int centerX = action.getInitialX();
        int centerY = action.getInitialY();
        
        for (int dx = 0; dx < size; dx++) {
            int height = dx + 1;
            int startY = centerY - dx / 2;
            for (int dy = 0; dy < height; dy++) {
                pixelsToDraw.add(new Pixel(
                        centerX + dx,
                        startY + dy,
                        action.getRed(),
                        action.getGreen(),
                        action.getBlue(),
                        action.getAlpha()
                ));
            }
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD "10" - TRIANGLE RIGHT
    public static List<Pixel> drawTriangleRight(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int size = action.getSize();
        int centerX = action.getInitialX();
        int centerY = action.getInitialY();
        
        for (int dx = 0; dx < size; dx++) {
            int height = size - dx;
            int startY = centerY - (size - dx - 1) / 2;
            for (int dy = 0; dy < height; dy++) {
                pixelsToDraw.add(new Pixel(
                        centerX + dx,
                        startY + dy,
                        action.getRed(),
                        action.getGreen(),
                        action.getBlue(),
                        action.getAlpha()
                ));
            }
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD "11" - DIAMOND
    public static List<Pixel> drawDiamond(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int size = action.getSize();
        int centerX = action.getInitialX();
        int centerY = action.getInitialY();
        
        // Draw diamond using two triangles (up and down)
        for (int dy = 0; dy < size; dy++) {
            int width = dy + 1;
            int startX = centerX - dy / 2;
            for (int dx = 0; dx < width; dx++) {
                pixelsToDraw.add(new Pixel(
                        startX + dx,
                        centerY - dy,
                        action.getRed(),
                        action.getGreen(),
                        action.getBlue(),
                        action.getAlpha()
                ));
            }
        }
        
        for (int dy = 1; dy < size; dy++) {
            int width = size - dy;
            int startX = centerX - (size - dy - 1) / 2;
            for (int dx = 0; dx < width; dx++) {
                pixelsToDraw.add(new Pixel(
                        startX + dx,
                        centerY + dy,
                        action.getRed(),
                        action.getGreen(),
                        action.getBlue(),
                        action.getAlpha()
                ));
            }
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD "12" - ELLIPSE
    public static List<Pixel> drawEllipse(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int width = action.getWidth();
        int height = action.getHeight();
        int centerX = action.getInitialX();
        int centerY = action.getInitialY();
        
        for (int dx = -width/2; dx <= width/2; dx++) {
            for (int dy = -height/2; dy <= height/2; dy++) {
                // Ellipse equation: (dx/a)^2 + (dy/b)^2 <= 1
                double a = width / 2.0;
                double b = height / 2.0;
                if ((dx * dx) / (a * a) + (dy * dy) / (b * b) <= 1.0) {
                    pixelsToDraw.add(new Pixel(
                            centerX + dx,
                            centerY + dy,
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

    //DRAWING METHOD "13" - ARC
    public static List<Pixel> drawArc(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int radius = action.getRadius();
        int centerX = action.getInitialX();
        int centerY = action.getInitialY();
        int startAngle = action.getSize(); // Use size as start angle (0-360)
        int endAngle = action.getWidth(); // Use width as end angle (0-360)
        
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx * dx + dy * dy <= radius * radius) {
                    // Calculate angle of this point
                    double angle = Math.toDegrees(Math.atan2(dy, dx));
                    if (angle < 0) angle += 360;
                    
                    // Check if angle is within arc range
                    if (startAngle <= endAngle) {
                        if (angle >= startAngle && angle <= endAngle) {
                            pixelsToDraw.add(new Pixel(
                                    centerX + dx,
                                    centerY + dy,
                                    action.getRed(),
                                    action.getGreen(),
                                    action.getBlue(),
                                    action.getAlpha()
                            ));
                        }
                    } else {
                        // Handle wrap-around case (e.g., 300 to 60 degrees)
                        if (angle >= startAngle || angle <= endAngle) {
                            pixelsToDraw.add(new Pixel(
                                    centerX + dx,
                                    centerY + dy,
                                    action.getRed(),
                                    action.getGreen(),
                                    action.getBlue(),
                                    action.getAlpha()
                            ));
                        }
                    }
                }
            }
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD "14" - CURVED LINE
    public static List<Pixel> drawCurvedLine(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int startX = action.getInitialX();
        int startY = action.getInitialY();
        int endX = action.getInitialX() + action.getWidth();
        int endY = action.getInitialY() + action.getHeight();
        int controlX = startX + (endX - startX) / 2;
        int controlY = startY - action.getSize(); // Use size as curve height
        
        // Simple quadratic Bezier curve approximation
        int steps = Math.max(Math.abs(endX - startX), Math.abs(endY - startY));
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            int x = (int) ((1-t)*(1-t)*startX + 2*(1-t)*t*controlX + t*t*endX);
            int y = (int) ((1-t)*(1-t)*startY + 2*(1-t)*t*controlY + t*t*endY);
            
            pixelsToDraw.add(new Pixel(
                    x,
                    y,
                    action.getRed(),
                    action.getGreen(),
                    action.getBlue(),
                    action.getAlpha()
            ));
        }
        return pixelsToDraw;
    }

    //DRAWING METHOD "15" - STAR
    public static List<Pixel> drawStar(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int radius = action.getRadius();
        int centerX = action.getInitialX();
        int centerY = action.getInitialY();
        int points = action.getSize(); // Use size as number of points (5-8)
        if (points < 3) points = 5; // Default to 5-point star
        
        // Calculate star points
        double outerRadius = radius;
        double innerRadius = radius * 0.4; // Inner radius for star shape
        
        for (int i = 0; i < points * 2; i++) {
            double angle = (i * Math.PI) / points;
            double r = (i % 2 == 0) ? outerRadius : innerRadius;
            int x = (int) (centerX + r * Math.cos(angle));
            int y = (int) (centerY + r * Math.sin(angle));
            
            // Draw lines between points to create star shape
            if (i > 0) {
                double prevAngle = ((i-1) * Math.PI) / points;
                double prevR = ((i-1) % 2 == 0) ? outerRadius : innerRadius;
                int prevX = (int) (centerX + prevR * Math.cos(prevAngle));
                int prevY = (int) (centerY + prevR * Math.sin(prevAngle));
                
                // Draw line between previous and current point
                int steps = Math.max(Math.abs(x - prevX), Math.abs(y - prevY));
                for (int j = 0; j <= steps; j++) {
                    double t = (double) j / steps;
                    int lineX = (int) (prevX + t * (x - prevX));
                    int lineY = (int) (prevY + t * (y - prevY));
                    
                    pixelsToDraw.add(new Pixel(
                            lineX,
                            lineY,
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

    //DRAWING METHOD "16" - GRADIENT SQUARE
    public static List<Pixel> drawGradientSquare(DrawingAction action) {
        List<Pixel> pixelsToDraw = new ArrayList<>();
        int size = action.getSize();
        int startX = action.getInitialX();
        int startY = action.getInitialY();
        
        // Use red/green as start color, blue/alpha as end color
        int startR = action.getRed();
        int startG = action.getGreen();
        int endR = action.getBlue();
        int endG = action.getAlpha();
        
        for (int dx = 0; dx < size; dx++) {
            for (int dy = 0; dy < size; dy++) {
                // Calculate gradient based on position
                double t = (double) (dx + dy) / (2 * size);
                int r = (int) (startR + t * (endR - startR));
                int g = (int) (startG + t * (endG - startG));
                int b = 128; // Fixed blue component
                
                pixelsToDraw.add(new Pixel(
                        startX + dx,
                        startY + dy,
                        Math.max(0, Math.min(255, r)),
                        Math.max(0, Math.min(255, g)),
                        Math.max(0, Math.min(255, b)),
                        255
                ));
            }
        }
        return pixelsToDraw;
    }
}
