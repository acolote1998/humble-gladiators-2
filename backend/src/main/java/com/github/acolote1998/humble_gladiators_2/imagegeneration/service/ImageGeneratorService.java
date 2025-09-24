package com.github.acolote1998.humble_gladiators_2.imagegeneration.service;

import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.DrawingAction;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.Pixel;

import java.util.ArrayList;
import java.util.List;

public class ImageGeneratorService {

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
