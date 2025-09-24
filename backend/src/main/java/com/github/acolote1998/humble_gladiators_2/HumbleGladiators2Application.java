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
//        List<DrawingAction> actions = new ArrayList<>();
//        actions.add(new DrawingAction(5, 12, 12, 255, 0, 0, 255, 4, 4, 6, 4));
//
//        ImageGeneratorService.GenerateImage(ImageGeneratorService.generatePixelsForImage(actions), "stest.png", 32, 32);

        ConfigurableApplicationContext context = SpringApplication.run(HumbleGladiators2Application.class, args);

        ImageGeneratorService service = context.getBean(ImageGeneratorService.class);

        service.getActionsForDrawing();

    }
}
