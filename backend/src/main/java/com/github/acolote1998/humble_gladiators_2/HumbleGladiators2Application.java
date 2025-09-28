package com.github.acolote1998.humble_gladiators_2;

import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.core.service.GameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HumbleGladiators2Application {

    public static void main(String[] args) throws InterruptedException {


        ConfigurableApplicationContext context = SpringApplication.run(HumbleGladiators2Application.class, args);

        Theme theme = new Theme();
        List<String> wantedThemes = new ArrayList<>();
        wantedThemes.add("the office");
        wantedThemes.add("marvel");
        List<String> unwantedThemes = new ArrayList<>();
        unwantedThemes.add("Unicorns");
        theme.setWantedThemes(wantedThemes);
        theme.setUnwantedThemes(unwantedThemes);

        //Try game logic
        context.getBean(GameService.class).startGame(theme);

    }
}
