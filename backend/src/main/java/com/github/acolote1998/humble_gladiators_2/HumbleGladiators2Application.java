package com.github.acolote1998.humble_gladiators_2;

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

    public static void main(String[] args) {

        List<DrawingAction> actions = new ArrayList<>();
        actions.add(new DrawingAction(4, 50, 50, 139, 69, 19, 255, 0, 0, 0, 25)); // Dragon body (brown circle)
        actions.add(new DrawingAction(7, 50, 25, 139, 69, 19, 255, 8, 0, 0, 0)); // Dragon head (triangle up)
        actions.add(new DrawingAction(6, 45, 30, 255, 0, 0, 255, 0, 0, 0, 0)); // Left eye (red dot)
        actions.add(new DrawingAction(6, 55, 30, 255, 0, 0, 255, 0, 0, 0, 0)); // Right eye (red dot)
        actions.add(new DrawingAction(11, 30, 40, 139, 69, 19, 255, 6, 0, 0, 0)); // Dragon scale (diamond)
        actions.add(new DrawingAction(11, 70, 40, 139, 69, 19, 255, 6, 0, 0, 0)); // Dragon scale (diamond)
        actions.add(new DrawingAction(8, 50, 75, 139, 69, 19, 255, 6, 0, 0, 0)); // Dragon tail (triangle down)

        ImageGeneratorService.GenerateImage(ImageGeneratorService.generatePixelsForImage(actions), "stest.png", 100, 100);

        ConfigurableApplicationContext context = SpringApplication.run(HumbleGladiators2Application.class, args);

        ImageGeneratorService service = context.getBean(ImageGeneratorService.class);

//        service.getActionsForDrawing();

    }
}
