package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.exception.BannedUser;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.item.service.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@Getter
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

    @Value("${GENERATE_ALL}")
    private boolean GENERATE_ALL;

    @Value("${GENERATE_IMAGES}")
    private boolean GENERATE_IMAGES;

    @Value("${GENERATE_CAMPAIGN_COVER}")
    private boolean GENERATE_CAMPAIGN_COVER;

    @Value("${GENERATE_CAMPAIGN_CARD_BACK}")
    private boolean GENERATE_CAMPAIGN_CARD_BACK;

    @Value("${GENERATE_NPCS}")
    private boolean GENERATE_NPCS;
    @Value("${GENERATE_ARMORS}")
    private boolean GENERATE_ARMORS;
    @Value("${GENERATE_BOOTS}")
    private boolean GENERATE_BOOTS;
    @Value("${GENERATE_CONSUMABLES}")
    private boolean GENERATE_CONSUMABLES;
    @Value("${GENERATE_HELMETS}")
    private boolean GENERATE_HELMETS;
    @Value("${GENERATE_SHIELDS}")
    private boolean GENERATE_SHIELDS;
    @Value("${GENERATE_SPELLS}")
    private boolean GENERATE_SPELLS;
    @Value("${GENERATE_WEAPONS}")
    private boolean GENERATE_WEAPONS;
    @Value("${GAME_CREATION_STATE_INTERVAL}")
    private Integer GAME_CREATION_STATE_INTERVAL;

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


    public Campaign startGame(GameCreationDtoRequest gameCreationDtoRequest, String userId) throws InterruptedException {
        Campaign campaign = campaignService.createCampaign(gameCreationDtoRequest, userId);
        //THEMES
        updateCampaignCreationState(CampaignCreationStateType.CREATING_THEMES, campaign);
        Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        updateCampaignCreationState(CampaignCreationStateType.THEMES_CREATED, campaign);
        Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        //CAMPAIGN
        updateCampaignCreationState(CampaignCreationStateType.CREATING_CAMPAIGN, campaign);
        Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        updateCampaignCreationState(CampaignCreationStateType.CAMPAIGN_CREATED, campaign);
        Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        //ARMORS
        if (GENERATE_ALL || GENERATE_ARMORS) {
            updateCampaignCreationState(CampaignCreationStateType.CREATING_ARMORS, campaign);
            armorService.createTwentyFiveNewArmorTemplates(campaign);
            updateCampaignCreationState(CampaignCreationStateType.ARMORS_CREATED, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        }
        //BOOTS
        if (GENERATE_ALL || GENERATE_BOOTS) {
            updateCampaignCreationState(CampaignCreationStateType.CREATING_BOOTS, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
            bootsService.createTwentyFiveNewBootsTemplates(campaign);
            updateCampaignCreationState(CampaignCreationStateType.BOOTS_CREATED, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        }
        //CONSUMABLES
        if (GENERATE_ALL || GENERATE_CONSUMABLES) {
            updateCampaignCreationState(CampaignCreationStateType.CREATING_CONSUMABLES, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
            consumableService.createTwentyFiveNewConsumableTemplates(campaign);
            updateCampaignCreationState(CampaignCreationStateType.CONSUMABLES_CREATED, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        }
        //HELMETS
        if (GENERATE_ALL || GENERATE_HELMETS) {
            updateCampaignCreationState(CampaignCreationStateType.CREATING_HELMETS, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
            helmetService.createTwentyFiveNewHelmetsTemplates(campaign);
            updateCampaignCreationState(CampaignCreationStateType.HELMETS_CREATED, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        }
        //SHIELDS
        if (GENERATE_ALL || GENERATE_SHIELDS) {
            updateCampaignCreationState(CampaignCreationStateType.CREATING_SHIELDS, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
            shieldService.createTwentyFiveNewShieldTemplates(campaign);
            updateCampaignCreationState(CampaignCreationStateType.SHIELDS_CREATED, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        }
        //SPELLS
        if (GENERATE_ALL || GENERATE_SPELLS) {
            updateCampaignCreationState(CampaignCreationStateType.CREATING_SPELLS, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
            spellService.createTwentyFiveNewSpellTemplates(campaign);
            updateCampaignCreationState(CampaignCreationStateType.SPELLS_CREATED, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        }
        //WEAPONS
        if (GENERATE_ALL || GENERATE_WEAPONS) {
            updateCampaignCreationState(CampaignCreationStateType.CREATING_WEAPONS, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
            weaponService.createTwentyFiveNewWeaponTemplates(campaign);
            updateCampaignCreationState(CampaignCreationStateType.WEAPONS_CREATED, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        }
        //NPCs (Characters)
        if (GENERATE_ALL || GENERATE_NPCS) {
            updateCampaignCreationState(CampaignCreationStateType.CREATING_NPCS_PHASE_ONE, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
            //Tier 1 NPCs
            characterService.createTenNPCsOfDesiredTier(campaign, 1);
            updateCampaignCreationState(CampaignCreationStateType.CREATING_NPCS_PHASE_TWO, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
            //Tier 2 NPCs
            characterService.createTenNPCsOfDesiredTier(campaign, 2);
            updateCampaignCreationState(CampaignCreationStateType.CREATING_NPCS_PHASE_THREE, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
            //Tier 3 NPCs
            characterService.createTenNPCsOfDesiredTier(campaign, 3);
            updateCampaignCreationState(CampaignCreationStateType.CREATING_NPCS_PHASE_FOUR, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
            //Tier 4 NPCs
            characterService.createTenNPCsOfDesiredTier(campaign, 4);
            updateCampaignCreationState(CampaignCreationStateType.CREATING_NPCS_PHASE_FIVE, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
            //Tier 5 NPCs
            characterService.createTenNPCsOfDesiredTier(campaign, 5);
            updateCampaignCreationState(CampaignCreationStateType.NPCS_CREATED, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        }

        // CAMPAIGN COVER IMAGE
        if ((GENERATE_ALL && GENERATE_IMAGES) || (GENERATE_IMAGES && GENERATE_CAMPAIGN_COVER)) {
            updateCampaignCreationState(CampaignCreationStateType.CREATING_CAMPAIGN_COVER_IMAGE, campaign);
            campaignService.generateImageCoverForCampaign(
                    campaign,
                    characterService.getTier5NpcsContextForCampaignCover(campaign).toString(),
                    armorService.getTier5ArmorsContextForCampaignCover(campaign).toString(),
                    bootsService.getTier5BootsContextForCampaignCover(campaign).toString(),
                    helmetService.getTier5HelmetsContextForCampaignCover(campaign).toString(),
                    shieldService.getTier5ShieldsContextForCampaignCover(campaign).toString(),
                    weaponService.getTier5WeaponsContextForCampaignCover(campaign).toString(),
                    spellService.getTier5SpellsContextForCampaignCover(campaign).toString(),
                    consumableService.getTier5ConsumablesContextForCampaignCover(campaign).toString());
            updateCampaignCreationState(CampaignCreationStateType.CAMPAIGN_COVER_IMAGE_CREATED, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        }

        // CARD BACK FOR CAMPAIGN IMAGE
        if ((GENERATE_ALL && GENERATE_IMAGES) || (GENERATE_IMAGES && GENERATE_CAMPAIGN_CARD_BACK)) {
            updateCampaignCreationState(CampaignCreationStateType.CREATING_CAMPAIGN_CARD_BACK_IMAGE, campaign);
            campaignService.generateCardBackImageForCampaign(
                    campaign,
                    characterService.getTier5NpcsContextForCampaignCover(campaign).toString(),
                    armorService.getTier5ArmorsContextForCampaignCover(campaign).toString(),
                    bootsService.getTier5BootsContextForCampaignCover(campaign).toString(),
                    helmetService.getTier5HelmetsContextForCampaignCover(campaign).toString(),
                    shieldService.getTier5ShieldsContextForCampaignCover(campaign).toString(),
                    weaponService.getTier5WeaponsContextForCampaignCover(campaign).toString(),
                    spellService.getTier5SpellsContextForCampaignCover(campaign).toString(),
                    consumableService.getTier5ConsumablesContextForCampaignCover(campaign).toString());
            updateCampaignCreationState(CampaignCreationStateType.CAMPAIGN_CARD_BACK_IMAGE_CREATED, campaign);
            Thread.sleep(GAME_CREATION_STATE_INTERVAL);
        }
        updateCampaignCreationState(CampaignCreationStateType.GAME_CREATED, campaign);
        return campaign;
    }

    public void updateCampaignCreationState(CampaignCreationStateType status, Campaign campaign) {
        log.info("Campaign ID: {} - {}", campaign.getId(), status);
        campaign.setCampaignCreationState(status);
        campaignService.save(campaign);
    }
}

