package com.github.acolote1998.humble_gladiators_2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.DrawingAction;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.service.ImageGeneratorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class HumbleGladiators2Application {

    public static void main(String[] args) throws JsonProcessingException {


        ConfigurableApplicationContext context = SpringApplication.run(HumbleGladiators2Application.class, args);

        ImageGeneratorService service = context.getBean(ImageGeneratorService.class);

        service.generateImage("a blue dragon", 640, 480);

    }
}
