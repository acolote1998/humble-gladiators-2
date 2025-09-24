package com.github.acolote1998.humble_gladiators_2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.DrawingAction;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.service.ImageGeneratorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.naming.Context;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HumbleGladiators2Application {

    public static void main(String[] args) throws JsonProcessingException {

        List<DrawingAction> actions = new ArrayList<>();
        actions.add(new DrawingAction(4, 50, 50, 160, 82, 45, 255, 0, 0, 0, 20));
        actions.add(new DrawingAction(4, 35, 40, 139, 69, 19, 255, 0, 0, 0, 12));
        actions.add(new DrawingAction(4, 65, 40, 139, 69, 19, 255, 0, 0, 0, 12));
        actions.add(new DrawingAction(1, 30, 60, 139, 69, 19, 255, 0, 15, 10, 0));
        actions.add(new DrawingAction(1, 70, 60, 139, 69, 19, 255, 0, 15, 10, 0));
        actions.add(new DrawingAction(0, 45, 70, 139, 69, 19, 255, 10, 0, 0, 0));
        actions.add(new DrawingAction(0, 60, 70, 139, 69, 19, 255, 10, 0, 0, 0));
        actions.add(new DrawingAction(4, 50, 30, 101, 67, 33, 255, 0, 0, 0, 8));
        actions.add(new DrawingAction(6, 45, 25, 0, 0, 0, 255, 0, 0, 0, 0));
        actions.add(new DrawingAction(6, 55, 25, 0, 0, 0, 255, 0, 0, 0, 0));
        actions.add(new DrawingAction(1, 40, 50, 160, 82, 45, 255, 0, 30, 20, 0));
        actions.add(new DrawingAction(1, 55, 50, 160, 82, 45, 255, 0, 30, 20, 0));
        actions.add(new DrawingAction(4, 50, 70, 139, 69, 19, 255, 0, 0, 0, 25));
        actions.add(new DrawingAction(1, 48, 75, 101, 67, 33, 255, 0, 4, 15, 0));
        actions.add(new DrawingAction(1, 53, 75, 101, 67, 33, 255, 0, 4, 15, 0));
        actions.add(new DrawingAction(4, 50, 50, 160, 82, 45, 200, 0, 0, 0, 22));
        actions.add(new DrawingAction(7, 50, 60, 139, 69, 19, 255, 8, 0, 0, 0));
        actions.add(new DrawingAction(8, 50, 85, 139, 69, 19, 255, 7, 0, 0, 0));
        actions.add(new DrawingAction(4, 35, 40, 160, 82, 45, 200, 0, 0, 0, 11));
        actions.add(new DrawingAction(4, 65, 40, 160, 82, 45, 200, 0, 0, 0, 11));
        actions.add(new DrawingAction(1, 30, 60, 160, 82, 45, 200, 0, 14, 9, 0));
        actions.add(new DrawingAction(1, 70, 60, 160, 82, 45, 200, 0, 14, 9, 0));
        actions.add(new DrawingAction(0, 45, 70, 160, 82, 45, 200, 9, 0, 0, 0));
        actions.add(new DrawingAction(0, 60, 70, 160, 82, 45, 200, 9, 0, 0, 0));
        actions.add(new DrawingAction(4, 50, 30, 139, 69, 19, 200, 0, 0, 0, 7));
        actions.add(new DrawingAction(1, 40, 50, 160, 82, 45, 200, 0, 28, 18, 0));
        actions.add(new DrawingAction(1, 55, 50, 160, 82, 45, 200, 0, 28, 18, 0));
        actions.add(new DrawingAction(4, 50, 70, 160, 82, 45, 200, 0, 0, 0, 23));
        actions.add(new DrawingAction(1, 48, 75, 139, 69, 19, 200, 0, 3, 13, 0));
        actions.add(new DrawingAction(1, 53, 75, 139, 69, 19, 200, 0, 3, 13, 0));
        actions.add(new DrawingAction(4, 50, 50, 139, 69, 19, 150, 0, 0, 0, 21));
        actions.add(new DrawingAction(4, 50, 30, 101, 67, 33, 200, 0, 0, 0, 7));
        actions.add(new DrawingAction(7, 50, 60, 160, 82, 45, 200, 7, 0, 0, 0));
        actions.add(new DrawingAction(8, 50, 85, 160, 82, 45, 200, 6, 0, 0, 0));
        actions.add(new DrawingAction(1, 38, 50, 101, 67, 33, 255, 0, 24, 16, 0));
        actions.add(new DrawingAction(1, 57, 50, 101, 67, 33, 255, 0, 24, 16, 0));

        ImageGeneratorService.GenerateImage(ImageGeneratorService.generatePixelsForImage(actions), "stest.png", 100, 100);

        ConfigurableApplicationContext context = SpringApplication.run(HumbleGladiators2Application.class, args);

        ImageGeneratorService service = context.getBean(ImageGeneratorService.class);

        service.getActionsForDrawing("a dog");

    }
}
