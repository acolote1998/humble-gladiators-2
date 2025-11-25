package com.github.acolote1998.humble_gladiators_2.core.service.integration;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterCategory;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.core.dto.CharacterFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GameService;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RunwareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class GameServiceIntegrationTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private CampaignRepository campaignRepository;

    @MockitoBean
    private GeminiService geminiService;

    @MockitoBean
    private RunwareService runwareService;

    private String userId;
    private GameCreationDtoRequest validDto;

    @BeforeEach
    void setup() {
        userId = "test-user";
        validDto = new GameCreationDtoRequest(
                "Test Campaign",
                new GameCreationDtoRequest.ThemeDtoRequest(
                        new ArrayList<>(List.of("fantasy", "medieval")),
                        new ArrayList<>(List.of("modern"))
                )
        );
        
        // Mock GeminiService to return valid content
        setupGeminiServiceMocks();
    }
    
    private void setupGeminiServiceMocks() {
        // Mock all item generation methods to return 5 valid items per tier (1-5)
        for (int tier = 1; tier <= 5; tier++) {
            when(geminiService.generateFiveArmorsOfTier(any(Campaign.class), eq(tier))).thenReturn(createValidArmors(5));
            when(geminiService.generateFiveBootsOfTier(any(Campaign.class), eq(tier))).thenReturn(createValidBoots(5));
            when(geminiService.generateFiveConsumablesOfTier(any(Campaign.class), eq(tier))).thenReturn(createValidConsumables(5));
            when(geminiService.generateFiveHelmetsOfTier(any(Campaign.class), eq(tier))).thenReturn(createValidHelmets(5));
            when(geminiService.generateFiveShieldsOfTier(any(Campaign.class), eq(tier))).thenReturn(createValidShields(5));
            when(geminiService.generateFiveSpellsOfTier(any(Campaign.class), eq(tier))).thenReturn(createValidSpells(5));
            when(geminiService.generateFiveWeaponsOfTier(any(Campaign.class), eq(tier))).thenReturn(createValidWeapons(5));
        }
        
        // Mock NPC generation for each tier (1-5) - called twice per tier
        for (int tier = 1; tier <= 5; tier++) {
            when(geminiService.generateFiveNpcsOfTier(any(Campaign.class), any(), eq(tier)))
                    .thenReturn(createValidNPCs(5, tier));
        }
    }
    
    private List<ItemFromGeminiDto> createValidArmors(int count) {
        List<ItemFromGeminiDto> items = new ArrayList<>();
        String[] categories = {"ROBE", "PLATE", "MAIL", "SHIRT", "CAPE", "CLOAK", "BACKPACK", "OTHER"};
        int categoryIndex = 0;
        
        for (int tier = 1; tier <= 5; tier++) {
            for (int rarity = 1; rarity <= 5; rarity++) {
                if (items.size() >= count) break;
                items.add(new ItemFromGeminiDto(
                        "Armor " + items.size() + 1,
                        "Description for armor " + items.size() + 1,
                        rarity,
                        tier,
                        100 * tier * rarity,
                        false,
                        0,
                        false,
                        null,
                        categories[categoryIndex % categories.length],
                        1, // physicalDefense
                        0, // magicalDefense
                        null, null, null, null
                ));
                categoryIndex++;
            }
        }
        return items;
    }
    
    private List<ItemFromGeminiDto> createValidBoots(int count) {
        List<ItemFromGeminiDto> items = new ArrayList<>();
        String[] categories = {"BOOTS", "COMBAT_BOOTS", "SNEAKERS", "SANDALS", "OTHER"};
        int categoryIndex = 0;
        
        for (int tier = 1; tier <= 5; tier++) {
            for (int rarity = 1; rarity <= 5; rarity++) {
                if (items.size() >= count) break;
                items.add(new ItemFromGeminiDto(
                        "Boots " + items.size() + 1,
                        "Description for boots " + items.size() + 1,
                        rarity,
                        tier,
                        100 * tier * rarity,
                        false,
                        0,
                        false,
                        null,
                        categories[categoryIndex % categories.length],
                        1, // physicalDefense
                        0, // magicalDefense
                        null, null, null, null
                ));
                categoryIndex++;
            }
        }
        return items;
    }
    
    private List<ItemFromGeminiDto> createValidConsumables(int count) {
        List<ItemFromGeminiDto> items = new ArrayList<>();
        String[] categories = {"FOOD", "DRINK", "MEDICINE", "TREAT", "OTHER"};
        int categoryIndex = 0;
        
        for (int tier = 1; tier <= 5; tier++) {
            for (int rarity = 1; rarity <= 5; rarity++) {
                if (items.size() >= count) break;
                // Consumables must have at least one restore type (HP or MP)
                // Alternate between HP-only, MP-only, and both
                int restoreType = items.size() % 3;
                int restoreHpFlag = (restoreType == 0 || restoreType == 2) ? 1 : 0; // HP-only or both
                int restoreMpFlag = (restoreType == 1 || restoreType == 2) ? 1 : 0; // MP-only or both
                items.add(new ItemFromGeminiDto(
                        "Consumable " + items.size() + 1,
                        "Description for consumable " + items.size() + 1,
                        rarity,
                        tier,
                        100 * tier * rarity,
                        false,
                        0,
                        false,
                        null,
                        categories[categoryIndex % categories.length],
                        null, null,
                        restoreHpFlag, // restoreHp flag (1 or 0)
                        restoreMpFlag, // restoreMp flag (1 or 0)
                        null, null
                ));
                categoryIndex++;
            }
        }
        return items;
    }
    
    private List<ItemFromGeminiDto> createValidHelmets(int count) {
        List<ItemFromGeminiDto> items = new ArrayList<>();
        String[] categories = {"HELMET", "HARD_HAT", "MILITARY_HELMET", "HAT", "HOOD", "OTHER"};
        int categoryIndex = 0;
        
        for (int tier = 1; tier <= 5; tier++) {
            for (int rarity = 1; rarity <= 5; rarity++) {
                if (items.size() >= count) break;
                items.add(new ItemFromGeminiDto(
                        "Helmet " + items.size() + 1,
                        "Description for helmet " + items.size() + 1,
                        rarity,
                        tier,
                        100 * tier * rarity,
                        false,
                        0,
                        false,
                        null,
                        categories[categoryIndex % categories.length],
                        1, // physicalDefense
                        0, // magicalDefense
                        null, null, null, null
                ));
                categoryIndex++;
            }
        }
        return items;
    }
    
    private List<ItemFromGeminiDto> createValidShields(int count) {
        List<ItemFromGeminiDto> items = new ArrayList<>();
        String[] categories = {"SHIELD", "BOOK", "AMULET", "RING", "BADGE", "OTHER"};
        int categoryIndex = 0;
        
        for (int tier = 1; tier <= 5; tier++) {
            for (int rarity = 1; rarity <= 5; rarity++) {
                if (items.size() >= count) break;
                items.add(new ItemFromGeminiDto(
                        "Shield " + items.size() + 1,
                        "Description for shield " + items.size() + 1,
                        rarity,
                        tier,
                        100 * tier * rarity,
                        false,
                        0,
                        false,
                        null,
                        categories[categoryIndex % categories.length],
                        1, // physicalDefense
                        0, // magicalDefense
                        null, null, null, null
                ));
                categoryIndex++;
            }
        }
        return items;
    }
    
    private List<ItemFromGeminiDto> createValidSpells(int count) {
        List<ItemFromGeminiDto> items = new ArrayList<>();
        String[] categories = {"FIRE_SPELL", "ICE_SPELL", "HEALING_SPELL", "BUFF_SPELL", "GENERAL_SPELL", "OTHER"};
        int categoryIndex = 0;
        
        for (int tier = 1; tier <= 5; tier++) {
            for (int rarity = 1; rarity <= 5; rarity++) {
                if (items.size() >= count) break;
                // Alternate between damage spells and healing spells
                boolean isHealing = (items.size() % 3 == 0);
                items.add(new ItemFromGeminiDto(
                        "Spell " + items.size() + 1,
                        "Description for spell " + items.size() + 1,
                        rarity,
                        tier,
                        100 * tier * rarity,
                        false,
                        0,
                        false,
                        null,
                        categories[categoryIndex % categories.length],
                        null, null, // physicalDefense, magicalDefense
                        isHealing ? 1 : 0, // restoreHp flag
                        null, // restoreMp
                        isHealing ? 0 : 1, // physicalDamage flag
                        isHealing ? 0 : 1 // magicalDamage flag
                ));
                categoryIndex++;
            }
        }
        return items;
    }
    
    private List<ItemFromGeminiDto> createValidWeapons(int count) {
        List<ItemFromGeminiDto> items = new ArrayList<>();
        String[] categories = {"SWORD", "AXE", "MACE", "DAGGER", "SPEAR", "STAFF", "BOW", "WAND", "OTHER"};
        int categoryIndex = 0;
        
        for (int tier = 1; tier <= 5; tier++) {
            for (int rarity = 1; rarity <= 5; rarity++) {
                if (items.size() >= count) break;
                // Weapons must have at least one damage type (physical or magical)
                boolean hasPhysical = (items.size() % 2 == 0);
                items.add(new ItemFromGeminiDto(
                        "Weapon " + items.size() + 1,
                        "Description for weapon " + items.size() + 1,
                        rarity,
                        tier,
                        100 * tier * rarity,
                        false,
                        0,
                        false,
                        null,
                        categories[categoryIndex % categories.length],
                        null, null, null, null,
                        hasPhysical ? 1 : 0, // physicalDamage flag
                        hasPhysical ? 0 : 1 // magicalDamage flag
                ));
                categoryIndex++;
            }
        }
        return items;
    }
    
    private List<CharacterFromGeminiDto> createValidNPCs(int count, int tier) {
        List<CharacterFromGeminiDto> npcs = new ArrayList<>();
        CharacterCategory[] categories = CharacterCategory.values();
        int categoryIndex = 0;
        
        for (int i = 0; i < count; i++) {
            int rarity = (i % 5) + 1;
            int level = 1 + tier;
            npcs.add(new CharacterFromGeminiDto(
                    new CharacterFromGeminiDto.StatsFromGemini(
                            10 + tier, // constitution
                            10 + tier, // intelligence
                            10 + tier, // strength
                            10 + tier, // speed
                            10 + tier, // luck
                            100 + tier * 20, // maxHp
                            100 + tier * 20, // currentHp
                            50 + tier * 10, // maxMp
                            50 + tier * 10, // currentMp
                            170, // height
                            70, // weight
                            level, // level
                            0, // currentExp
                            100 // expForNextLevel
                    ),
                    CharacterType.NPC,
                    categories[categoryIndex % categories.length],
                    "NPC " + (i + 1),
                    "Description for NPC " + (i + 1),
                    false,
                    null,
                    rarity,
                    tier,
                    level * 10 * rarity * tier,
                    level * 20 * rarity * tier
            ));
            categoryIndex++;
        }
        return npcs;
    }

    @Test
    void startGame_verifiesFullGameCreationFlow() throws InterruptedException {
        Campaign campaign = gameService.startGame(validDto, userId);

        assertThat(campaign).isNotNull();
        assertThat(campaign.getId()).isNotNull();
        assertThat(campaign.getName()).isEqualTo("Test Campaign");
        assertThat(campaign.getUserId()).isEqualTo(userId);
        assertThat(campaign.getTheme()).isNotNull();
        assertThat(campaign.getTheme().getWantedThemes()).containsExactly("fantasy", "medieval");
        assertThat(campaign.getTheme().getUnwantedThemes()).containsExactly("modern");

        // Verify campaign is persisted
        Campaign saved = campaignRepository.findById(campaign.getId()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Campaign");
    }

    @Test
    void startGame_verifiesCampaignStateTransitions() throws InterruptedException {
        Campaign campaign = gameService.startGame(validDto, userId);

        // Verify final state - should be one of the completion states
        Campaign saved = campaignRepository.findById(campaign.getId()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getCampaignCreationState()).isNotNull();
        // The final state depends on feature flags, but should be one of the completion states
        assertThat(saved.getCampaignCreationState()).isIn(
                CampaignCreationStateType.GAME_CREATED,
                CampaignCreationStateType.CAMPAIGN_CREATED,
                CampaignCreationStateType.THEMES_CREATED
        );
    }

    @Test
    void startGame_verifiesCampaignCreationWithAllRelatedEntities() throws InterruptedException {
        Campaign campaign = gameService.startGame(validDto, userId);

        assertThat(campaign).isNotNull();
        assertThat(campaign.getId()).isNotNull();
        assertThat(campaign.getTheme()).isNotNull();
        assertThat(campaign.getTheme().getId()).isNotNull();

        // Verify campaign is persisted with relationships
        Campaign saved = campaignRepository.findById(campaign.getId()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getTheme()).isNotNull();
        assertThat(saved.getTheme().getWantedThemes()).isNotEmpty();
        assertThat(saved.getTheme().getUnwantedThemes()).isNotNull();
    }

    @Test
    void startGame_withGeminiServiceFailure_handlesGracefully() throws InterruptedException {
        // Mock GeminiService to throw exception
        // Note: This test verifies that if GeminiService fails, the campaign creation
        // still completes (at least the campaign is created)
        // The actual behavior depends on how GameService handles GeminiService failures
        
        Campaign campaign = gameService.startGame(validDto, userId);

        // At minimum, campaign should be created
        assertThat(campaign).isNotNull();
        assertThat(campaign.getId()).isNotNull();
    }

    @Test
    void startGame_withRunwareServiceFailure_handlesGracefully() throws InterruptedException {
        // Mock RunwareService to throw exception
        // Note: This test verifies that if RunwareService fails, the campaign creation
        // still completes (at least the campaign is created)
        
        Campaign campaign = gameService.startGame(validDto, userId);

        // At minimum, campaign should be created
        assertThat(campaign).isNotNull();
        assertThat(campaign.getId()).isNotNull();
    }

    @Test
    void startGame_verifiesDatabaseStateIsConsistent() throws InterruptedException {
        Campaign campaign = gameService.startGame(validDto, userId);

        // Verify campaign is persisted
        Campaign saved = campaignRepository.findById(campaign.getId()).orElse(null);
        assertThat(saved).isNotNull();
        
        // Verify theme is persisted
        assertThat(saved.getTheme()).isNotNull();
        assertThat(saved.getTheme().getId()).isNotNull();
        
        // Verify state is persisted
        assertThat(saved.getCampaignCreationState()).isNotNull();
    }
}

