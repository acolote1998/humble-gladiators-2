package com.github.acolote1998.humble_gladiators_2;

import com.github.acolote1998.humble_gladiators_2.core.service.RunwareService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HumbleGladiators2Application {

    public static void main(String[] args) throws InterruptedException {


        ConfigurableApplicationContext context = SpringApplication.run(HumbleGladiators2Application.class, args);

        context.getBean(RunwareService.class).getBase64GeneratedImage();
    }
}
