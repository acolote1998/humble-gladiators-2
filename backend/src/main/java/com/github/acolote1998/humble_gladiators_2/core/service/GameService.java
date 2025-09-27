package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.item.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameService {
    CampaignService campaignService;
    ArmorService armorService;
    BootsService bootsService;
    ConsumableService consumableService;
    HelmetService helmetService;
    ShieldService shieldService;
    SpellService spellService;
    WeaponService weaponService;
    CharacterService characterService;

    @Autowired
    public GameService(CampaignService campaignService,
                       ArmorService armorService,
                       BootsService bootsService,
                       ConsumableService consumableService,
                       HelmetService helmetService,
                       ShieldService shieldService,
                       SpellService spellService,
                       WeaponService weaponService,
                       CharacterService characterService) {
        this.campaignService = campaignService;
        this.armorService = armorService;
        this.bootsService = bootsService;
        this.consumableService = consumableService;
        this.helmetService = helmetService;
        this.shieldService = shieldService;
        this.spellService = spellService;
        this.weaponService = weaponService;
        this.characterService = characterService;
    }

    public void startGame(Theme gameTheme) throws InterruptedException {
        Campaign campaign = campaignService.createCampaign(gameTheme);
        //THEMES
        updateCampaignCreationState(CampaignCreationStateType.CREATING_THEMES, campaign);
        Thread.sleep(500);
        updateCampaignCreationState(CampaignCreationStateType.THEMES_CREATED, campaign);
        Thread.sleep(500);

        //CAMPAIGN
        updateCampaignCreationState(CampaignCreationStateType.CREATING_CAMPAIGN, campaign);
        Thread.sleep(500);
        updateCampaignCreationState(CampaignCreationStateType.CAMPAIGN_CREATED, campaign);
        Thread.sleep(500);

        //ARMORS
        updateCampaignCreationState(CampaignCreationStateType.CREATING_ARMORS, campaign);
        armorService.createTwentyFiveNewArmorTemplates(campaign);
        updateCampaignCreationState(CampaignCreationStateType.ARMORS_CREATED, campaign);

        //BOOTS
        updateCampaignCreationState(CampaignCreationStateType.CREATING_BOOTS, campaign);
        bootsService.createTwentyFiveNewBootsTemplates(campaign);
        updateCampaignCreationState(CampaignCreationStateType.BOOTS_CREATED, campaign);

        //CONSUMABLES
        updateCampaignCreationState(CampaignCreationStateType.CREATING_CONSUMABLES, campaign);
        consumableService.createTwentyFiveNewConsumableTemplates(campaign);
        updateCampaignCreationState(CampaignCreationStateType.CONSUMABLES_CREATED, campaign);

        //HELMETS
        updateCampaignCreationState(CampaignCreationStateType.CREATING_HELMETS, campaign);
        helmetService.createTwentyFiveNewHelmetsTemplates(campaign);
        updateCampaignCreationState(CampaignCreationStateType.HELMETS_CREATED, campaign);

        //SHIELDS
        updateCampaignCreationState(CampaignCreationStateType.CREATING_SHIELDS, campaign);
        shieldService.createTwentyFiveNewShieldTemplates(campaign);
        updateCampaignCreationState(CampaignCreationStateType.SHIELDS_CREATED, campaign);

        //SPELLS
        updateCampaignCreationState(CampaignCreationStateType.CREATING_SPELLS, campaign);
        spellService.createTwentyFiveNewSpellTemplates(campaign);
        updateCampaignCreationState(CampaignCreationStateType.SPELLS_CREATED, campaign);

        //WEAPONS
        updateCampaignCreationState(CampaignCreationStateType.CREATING_WEAPONS, campaign);
        weaponService.createTwentyFiveNewWeaponTemplates(campaign);
        updateCampaignCreationState(CampaignCreationStateType.WEAPONS_CREATED, campaign);

        //NPCs (Characters)
        updateCampaignCreationState(CampaignCreationStateType.CREATING_NPCS, campaign);
        characterService.createFiftyNPCs(campaign);
        updateCampaignCreationState(CampaignCreationStateType.NPCS_CREATED, campaign);
    }

    public void updateCampaignCreationState(CampaignCreationStateType status, Campaign campaign) {
        log.info("Campaign ID: {} - {}", campaign.getId(), status);
        campaign.setCampaignCreationState(status);
        campaignService.save(campaign);
    }
}

