package com.github.acolote1998.humble_gladiators_2;

import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.ImageGenerator;
import com.github.acolote1998.humble_gladiators_2.imagegeneration.model.Pixel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HumbleGladiators2Application {

    public static void main(String[] args) {
        List<Pixel> testList = new ArrayList<>();
        testList.add(new Pixel(23, 23, 2555, 23, 23, 255));
        ImageGenerator.GenerateImage(testList, "test.png", 32, 32);
        SpringApplication.run(HumbleGladiators2Application.class, args);

    }
}
