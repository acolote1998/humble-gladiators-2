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
        actions.add(new DrawingAction(1, 30, 20, 0, 100, 0, 255, 0, 40, 50, 0)); // Body
        actions.add(new DrawingAction(0, 20, 20, 0, 100, 0, 255, 10, 0, 0, 0)); // Head
        actions.add(new DrawingAction(3, 20, 10, 0, 50, 0, 255, 0, 0, 10, 0)); // Horn 1
        actions.add(new DrawingAction(3, 30, 10, 0, 50, 0, 255, 0, 0, 10, 0)); // Horn 2
        actions.add(new DrawingAction(2, 70, 40, 100, 0, 0, 255, 0, 15, 0, 0)); // Tail
        actions.add(new DrawingAction(0, 65, 35, 100, 0, 0, 255, 5, 0, 0, 0)); // Tail tip
        actions.add(new DrawingAction(1, 40, 70, 100, 0, 0, 255, 0, 10, 5, 0)); // Feet 1
        actions.add(new DrawingAction(1, 60, 70, 100, 0, 0, 255, 0, 10, 5, 0)); // Feet 2
        actions.add(new DrawingAction(3, 70, 25, 255, 0, 0, 255, 0, 0, 15, 0)); // Wing

        ImageGeneratorService.GenerateImage(ImageGeneratorService.generatePixelsForImage(actions), "stest.png", 100, 100);

        ConfigurableApplicationContext context = SpringApplication.run(HumbleGladiators2Application.class, args);

        ImageGeneratorService service = context.getBean(ImageGeneratorService.class);

//        service.getActionsForDrawing();

    }
}
