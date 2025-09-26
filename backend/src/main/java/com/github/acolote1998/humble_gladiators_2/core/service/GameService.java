package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.item.service.ArmorService;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    CampaignService campaignService;
    ArmorService armorService;

    public void startGame(Theme gameTheme) {
        Campaign campaign = campaignService.createCampaign(gameTheme);
        armorService.createTwentyFiveNewArmorTemplates(campaign);
    }
}

