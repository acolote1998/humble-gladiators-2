package com.github.acolote1998.humble_gladiators_2;

import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.DrawingAction;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.ImageGenerator;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.service.ImageGeneratorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HumbleGladiators2Application {

    public static void main(String[] args) {
        List<DrawingAction> actions = new ArrayList<>();
        actions.add(new DrawingAction(0, 0, 0));
        actions.add(new DrawingAction(1, 0, 0));

        ImageGenerator.GenerateImage(ImageGeneratorService.generatePixelsForImage(actions), "test.png", 32, 32);
        SpringApplication.run(HumbleGladiators2Application.class, args);

    }
}
