package com.github.acolote1998.humble_gladiators_2.imagegeneration.service;

import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.DrawingAction;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.Pixel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ImageGeneratorService {

    public static boolean isValidColorValue(int value) {
        return (value >= 0 && value <= 255);
    }

    public static boolean isValidCoordinate(int valueX, int valueY, int maxX, int maxY) {
        return (valueX >= 0 && valueX < maxX && valueY >= 0 && valueY < maxY);
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

        } catch (Exception e) {
            log.error("Error creating image: " + e.getMessage());
        }
    }

    public static List<Pixel> generatePixelsForImage(List<DrawingAction> actions) {
        List<Pixel> resultPixels = new ArrayList<>();
        actions.forEach(drawingAction -> {
            switch (drawingAction.getDrawingMethod()) {
                case 0: {
                    resultPixels.add(new Pixel(10, 10, 255, 0, 0, 255));
                }
                case 1: {
                    resultPixels.add(new Pixel(12, 10, 0, 255, 0, 255));

                }
                default: {
                    System.out.println("default");
                }
            }
        });
        return resultPixels;
    }
}
