package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.item.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public void getShortReportOfAIGeneratedContent(Campaign campaign) {
        Map<String, Object> report = new HashMap<>();

        // Add campaign metadata
        Map<String, Object> campaignInfo = new HashMap<>();
        campaignInfo.put("campaignId", campaign.getId());
        campaignInfo.put("theme", campaign.getTheme());
        campaignInfo.put("generationTimestamp", System.currentTimeMillis());
        report.put("campaignInfo", campaignInfo);

        // Collect reports from all services
        report.putAll(armorService.getShortAIGeneratedReport());
        report.putAll(bootsService.getShortAIGeneratedReport());
        report.putAll(consumableService.getShortAIGeneratedReport());
        report.putAll(helmetService.getShortAIGeneratedReport());
        report.putAll(shieldService.getShortAIGeneratedReport());
        report.putAll(spellService.getShortAIGeneratedReport());
        report.putAll(weaponService.getShortAIGeneratedReport());
        report.putAll(characterService.getShortAIGeneratedReport());

        // Save report as JSON file
        saveReportAsJson(report, campaign.getId());
    }

    private void saveReportAsJson(Map<String, Object> report, Long campaignId) {
        try {
            // Ensure the directory exists
            Path reportsDir = Paths.get("src/main/resources/generation_reports");
            if (!Files.exists(reportsDir)) {
                Files.createDirectories(reportsDir);
            }

            // Create filename
            String filename = "campaign" + campaignId + "_generated_content.json";
            Path filePath = reportsDir.resolve(filename);

            // Convert to JSON and save
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), report);

            log.info("Generated content report saved to: {}", filePath.toString());
        } catch (IOException e) {
            log.error("Failed to save generated content report for campaign {}: {}", campaignId, e.getMessage());
        }
    }

    public void startGame(GameCreationDtoRequest gameCreationDtoRequest) throws InterruptedException {
        Campaign campaign = campaignService.createCampaign(gameCreationDtoRequest);
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
        //Tier 1 NPCs
        characterService.createTenNPCsOfDesiredTier(campaign, 1);
        //Tier 2 NPCs
        characterService.createTenNPCsOfDesiredTier(campaign, 2);
        //Tier 3 NPCs
        characterService.createTenNPCsOfDesiredTier(campaign, 3);
        //Tier 4 NPCs
        characterService.createTenNPCsOfDesiredTier(campaign, 4);
        //Tier 5 NPCs
        characterService.createTenNPCsOfDesiredTier(campaign, 5);
        updateCampaignCreationState(CampaignCreationStateType.NPCS_CREATED, campaign);

        getShortReportOfAIGeneratedContent(campaign);
        log.info("Creating report of generated content");

        updateCampaignCreationState(CampaignCreationStateType.GAME_CREATED, campaign);
    }

    public void updateCampaignCreationState(CampaignCreationStateType status, Campaign campaign) {
        log.info("Campaign ID: {} - {}", campaign.getId(), status);
        campaign.setCampaignCreationState(status);
        campaignService.save(campaign);
    }
}

