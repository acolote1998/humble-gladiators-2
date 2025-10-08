package com.github.acolote1998.humble_gladiators_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HumbleGladiators2Application {

    public static void main(String[] args) throws InterruptedException {
        
        ConfigurableApplicationContext context = SpringApplication.run(HumbleGladiators2Application.class, args);
    }
}
