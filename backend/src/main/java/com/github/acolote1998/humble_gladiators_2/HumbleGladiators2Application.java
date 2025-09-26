package com.github.acolote1998.humble_gladiators_2;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HumbleGladiators2Application {

    public static void main(String[] args) {


        ConfigurableApplicationContext context = SpringApplication.run(HumbleGladiators2Application.class, args);
        Theme theme = new Theme();
        List<String> wantedThemes = new ArrayList<>();
        wantedThemes.add("Harry Potter");
        List<String> unwantedThemes = new ArrayList<>();
        unwantedThemes.add("Unicorns");
        theme.setWantedThemes(wantedThemes);
        theme.setUnwantedThemes(unwantedThemes);
        Campaign newCampaign = context.getBean(CampaignService.class).createCampaign(theme);

    }
}
