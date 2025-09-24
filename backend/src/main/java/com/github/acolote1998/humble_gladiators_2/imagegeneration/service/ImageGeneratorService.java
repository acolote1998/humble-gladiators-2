package com.github.acolote1998.humble_gladiators_2.imagegeneration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.DrawingAction;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.Pixel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImageGeneratorService {

    GeminiService geminiservice;

    @Autowired
    public ImageGeneratorService(GeminiService service) {
        this.geminiservice = service;
    }

    public static boolean isValidColorValue(int value) {
        return (value >= 0 && value <= 255);
    }

    public static boolean isValidCoordinate(int valueX, int valueY, int maxX, int maxY) {
        return (valueX >= 0 && valueX < maxX && valueY >= 0 && valueY < maxY);
    }

    //    public List<DrawingAction> getActionsForDrawing() {
    public List<DrawingAction> getActionsForDrawing(String imageToGenerate) throws JsonProcessingException {
        return geminiservice.generateDrawingActionsTest(imageToGenerate);
    }

    public static void GenerateImage(List<Pixel> pixels, String outputPath, Integer width, Integer height) {
        try {

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // Set pixels from the list
            for (Pixel p : pixels) {
                if (isValidCoordinate(p.getX(), p.getY(), width, height)) {
                    if (isValidColorValue(p.getAlpha()) &&
                            isValidColorValue(p.getRed()) &&
                            isValidColorValue(p.getGreen()) &&
                            isValidColorValue(p.getBlue())) {
                        int pixelValue = (p.getAlpha() << 24) | (p.getRed() << 16) | (p.getGreen() << 8) | p.getBlue();
                        image.setRGB(p.getX(), p.getY(), pixelValue);
                    } else {
                        log.warn("Ignoring pixel " + p.toString() + ", corrupted color code.");
                    }
                } else {
                    log.warn("Ignoring pixel " + p.toString() + ", coordinate out of canvas");
                }
            }

            // Save image
            ImageIO.write(image, "png", new File(outputPath));
            log.info("Image successfully created");

            // Create a new scaled image
            BufferedImage scaledImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            // Enable high-quality scaling
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawImage(image, 0, 0, 64, 64, null);
            g2d.dispose();
            // Save result
            ImageIO.write(scaledImage, "png", new File("scaledDown.png"));

        } catch (Exception e) {
            log.error("Error creating image: " + e.getMessage());
        }
    }

    public static List<Pixel> generatePixelsForImage(List<DrawingAction> actions) {
        List<Pixel> resultPixels = new ArrayList<>();
        actions.forEach(drawingAction -> {
            switch (drawingAction.getDrawingMethod()) {
                //SQUARE
                case 0: {
                    resultPixels.addAll(DrawingAction.drawSquare(drawingAction));
                    break;
                }
                //RECTANGLE
                case 1: {
                    resultPixels.addAll(DrawingAction.drawRectangle(drawingAction));
                    break;
                }
                //HORIZONTAL LINE
                case 2: {
                    resultPixels.addAll(DrawingAction.drawHorizontalLine(drawingAction));
                    break;
                }
                //VERTICAL LINE
                case 3: {
                    resultPixels.addAll(DrawingAction.drawVerticalLine(drawingAction));
                    break;
                }
                //CIRCLE
                case 4: {
                    resultPixels.addAll(DrawingAction.drawCircle(drawingAction));
                    break;
                }
                //HOLLOW SQUARE
                case 5: {
                    resultPixels.addAll(DrawingAction.drawHollowSquare(drawingAction));
                    break;
                }
                //DOT
                case 6: {
                    resultPixels.addAll(DrawingAction.drawDot(drawingAction));
                    break;
                }
                //TRIANGLE UP
                case 7: {
                    resultPixels.addAll(DrawingAction.drawTriangleUp(drawingAction));
                    break;
                }
                //TRIANGLE DOWN
                case 8: {
                    resultPixels.addAll(DrawingAction.drawTriangleDown(drawingAction));
                    break;
                }
                //TRIANGLE LEFT
                case 9: {
                    resultPixels.addAll(DrawingAction.drawTriangleLeft(drawingAction));
                    break;
                }
                //TRIANGLE RIGHT
                case 10: {
                    resultPixels.addAll(DrawingAction.drawTriangleRight(drawingAction));
                    break;
                }
                //DIAMOND
                case 11: {
                    resultPixels.addAll(DrawingAction.drawDiamond(drawingAction));
                    break;
                }
                //ELLIPSE
                case 12: {
                    resultPixels.addAll(DrawingAction.drawEllipse(drawingAction));
                    break;
                }
                //ARC
                case 13: {
                    resultPixels.addAll(DrawingAction.drawArc(drawingAction));
                    break;
                }
                //CURVED LINE
                case 14: {
                    resultPixels.addAll(DrawingAction.drawCurvedLine(drawingAction));
                    break;
                }
                //STAR
                case 15: {
                    resultPixels.addAll(DrawingAction.drawStar(drawingAction));
                    break;
                }
                //GRADIENT SQUARE
                case 16: {
                    resultPixels.addAll(DrawingAction.drawGradientSquare(drawingAction));
                    break;
                }
                default: {
                    log.error("Invalid Drawing Method: " + drawingAction.getDrawingMethod());
                    break;
                }
            }
        });
        return resultPixels;
    }
}
