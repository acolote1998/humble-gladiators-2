package com.github.acolote1998.humble_gladiators_2.core.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterCategory;
import com.github.acolote1998.humble_gladiators_2.core.dto.CharacterFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RunwareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeminiService geminiService;

    @MockitoBean
    private RunwareService runwareService;

    private GameCreationDtoRequest validDto;

    @BeforeEach
    void setup() {
        validDto = new GameCreationDtoRequest(
                "Test Campaign",
                new GameCreationDtoRequest.ThemeDtoRequest(
                        new ArrayList<>(List.of("fantasy", "medieval")),
                        new ArrayList<>(List.of("modern"))
                )
        );
        
        // Mock GeminiService to return valid content (same as GameServiceIntegrationTest)
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
                        "Armor " + (items.size() + 1),
                        "Description for armor " + (items.size() + 1),
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
                        "Boots " + (items.size() + 1),
                        "Description for boots " + (items.size() + 1),
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
                int restoreType = items.size() % 3;
                int restoreHpFlag = (restoreType == 0 || restoreType == 2) ? 1 : 0;
                int restoreMpFlag = (restoreType == 1 || restoreType == 2) ? 1 : 0;
                items.add(new ItemFromGeminiDto(
                        "Consumable " + (items.size() + 1),
                        "Description for consumable " + (items.size() + 1),
                        rarity,
                        tier,
                        100 * tier * rarity,
                        false,
                        0,
                        false,
                        null,
                        categories[categoryIndex % categories.length],
                        null, null,
                        restoreHpFlag,
                        restoreMpFlag,
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
                        "Helmet " + (items.size() + 1),
                        "Description for helmet " + (items.size() + 1),
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
                        "Shield " + (items.size() + 1),
                        "Description for shield " + (items.size() + 1),
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
                boolean isHealing = (items.size() % 3 == 0);
                items.add(new ItemFromGeminiDto(
                        "Spell " + (items.size() + 1),
                        "Description for spell " + (items.size() + 1),
                        rarity,
                        tier,
                        100 * tier * rarity,
                        false,
                        0,
                        false,
                        null,
                        categories[categoryIndex % categories.length],
                        null, null,
                        isHealing ? 1 : 0,
                        null,
                        isHealing ? 0 : 1,
                        isHealing ? 0 : 1
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
                boolean hasPhysical = (items.size() % 2 == 0);
                items.add(new ItemFromGeminiDto(
                        "Weapon " + (items.size() + 1),
                        "Description for weapon " + (items.size() + 1),
                        rarity,
                        tier,
                        100 * tier * rarity,
                        false,
                        0,
                        false,
                        null,
                        categories[categoryIndex % categories.length],
                        null, null, null, null,
                        hasPhysical ? 1 : 0,
                        hasPhysical ? 0 : 1
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
            // goldReward = level * 10 * rarity * tier (from CharacterService.createFiveNPCsOfTier)
            int goldReward = level * 10 * rarity * tier;
            // expReward = level * 20 * rarity * tier (from CharacterService.createFiveNPCsOfTier)
            int expReward = level * 20 * rarity * tier;
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
                    com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType.NPC,
                    categories[categoryIndex % categories.length],
                    "NPC " + (i + 1),
                    "Description for NPC " + (i + 1),
                    false,
                    null,
                    rarity,
                    tier,
                    goldReward,
                    expReward
            ));
            categoryIndex++;
        }
        return npcs;
    }

    @Test
    void createNewCampaign_fullGameCreationFlow() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/game/campaign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(validDto))
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/campaign/")));
    }

    @Test
    void getGameCreationState_returnsCampaignCreationState() throws Exception {
        // Create a campaign first by calling the create endpoint
        ObjectMapper mapper = new ObjectMapper();
        
        // Note: This is async, so we might need to wait or check the state
        mockMvc.perform(post("/api/game/campaign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(validDto))
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isCreated());

        // Check the state
        mockMvc.perform(get("/api/game/state")
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void getGameCreationState_returnsNotFoundWhenNoCampaignBeingCreated() throws Exception {
        // Use a unique user ID that definitely has no campaigns
        // Due to @Transactional, each test should be isolated, but to be safe use a unique user
        String uniqueUserId = "user-no-campaigns-" + System.currentTimeMillis();
        mockMvc.perform(get("/api/game/state")
                        .with(jwt().jwt(j -> j.subject(uniqueUserId))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(CampaignCreationStateType.CAMPAIGN_NOT_FOUND.name()));
    }
}

