package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.item.service.ArmorService;
import com.github.acolote1998.humble_gladiators_2.item.service.BootsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameService {
    CampaignService campaignService;
    ArmorService armorService;
    BootsService bootsService;

    @Autowired
    public GameService(CampaignService campaignService, ArmorService armorService, BootsService bootsService) {
        this.campaignService = campaignService;
        this.armorService = armorService;
        this.bootsService = bootsService;
    }

    public void startGame(Theme gameTheme) throws InterruptedException {
        Campaign campaign = campaignService.createCampaign(gameTheme);
        updateCampaignCreationState(CampaignCreationStateType.CREATING_THEMES, campaign);
        Thread.sleep(500);
        updateCampaignCreationState(CampaignCreationStateType.THEMES_CREATED, campaign);
        Thread.sleep(500);
        updateCampaignCreationState(CampaignCreationStateType.CREATING_CAMPAIGN, campaign);
        Thread.sleep(500);
        updateCampaignCreationState(CampaignCreationStateType.CAMPAIGN_CREATED, campaign);
        Thread.sleep(500);
        updateCampaignCreationState(CampaignCreationStateType.CREATING_ARMORS, campaign);
        armorService.createTwentyFiveNewArmorTemplates(campaign);
        updateCampaignCreationState(CampaignCreationStateType.ARMORS_CREATED, campaign);
        updateCampaignCreationState(CampaignCreationStateType.CREATING_BOOTS, campaign);
        bootsService.createTwentyFiveNewBootsTemplates(campaign);
        updateCampaignCreationState(CampaignCreationStateType.BOOTS_CREATED, campaign);
    }

    public void updateCampaignCreationState(CampaignCreationStateType status, Campaign campaign) {
        log.info("Campaign ID: {} - {}", campaign.getId(), status);
        campaign.setCampaignCreationState(status);
        campaignService.save(campaign);
    }
}

