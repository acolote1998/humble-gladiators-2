package com.github.acolote1998.humble_gladiators_2.characters.controller;

import com.github.acolote1998.humble_gladiators_2.characters.dto.CreateHeroRequestDto;
import com.github.acolote1998.humble_gladiators_2.characters.exception.HeroAlreadyCreated;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CharacterController.class)
class CharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CharacterService characterService;

    @MockitoBean
    private CampaignService campaignService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;

    private Jwt jwt;

    @BeforeEach
    void setUp() {
        jwt = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .claim("sub", USER_ID)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
    }

    @Test
    @WithMockUser
    void getAllFullCharactersForACampaign_ShouldReturnListOfCharacters() throws Exception {
        // Arrange
        CharacterInstance character = createTestCharacter();
        List<CharacterInstance> characters = List.of(character);

        when(characterService.getAllCharacterInstancesForACampaignAndUser(USER_ID, CAMPAIGN_ID))
                .thenReturn(characters);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/character-instances", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void getAllFullCharactersForACampaign_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(characterService.getAllCharacterInstancesForACampaignAndUser(USER_ID, CAMPAIGN_ID))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/character-instances", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser
    void createHeroForACampaign_ShouldReturnCreatedResponse() throws Exception {
        // Arrange
        CreateHeroRequestDto requestDto = new CreateHeroRequestDto("Hero Name");
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        campaign.setUserId(USER_ID);

        CharacterInstance hero = createTestCharacter();
        hero.setName("Hero Name");
        hero.setCampaign(campaign);

        when(campaignService.getCampaignByIdAndUserId(USER_ID, CAMPAIGN_ID)).thenReturn(campaign);
        when(characterService.createHero(any(Campaign.class), eq(USER_ID), any(CreateHeroRequestDto.class)))
                .thenReturn(hero);

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/character-instances/hero", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/campaign/character-instances/hero"))
                .andExpect(jsonPath("$.campaignId").value(CAMPAIGN_ID));
    }

    @Test
    @WithMockUser
    void createHeroForACampaign_WhenHeroAlreadyExists_ShouldReturnConflict() throws Exception {
        // Arrange
        CreateHeroRequestDto requestDto = new CreateHeroRequestDto("Hero Name");
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);

        when(campaignService.getCampaignByIdAndUserId(USER_ID, CAMPAIGN_ID)).thenReturn(campaign);
        when(characterService.createHero(any(Campaign.class), eq(USER_ID), any(CreateHeroRequestDto.class)))
                .thenThrow(new HeroAlreadyCreated("Hero already created"));

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/character-instances/hero", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Hero already created"));
    }

    @Test
    @WithMockUser
    void getHeroForACampaign_ShouldReturnHero() throws Exception {
        // Arrange
        CharacterInstance hero = createTestCharacter();

        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/character-instances/hero", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void getDailyEnemyForACampaign_ShouldReturnDailyEnemy() throws Exception {
        // Arrange
        CharacterInstance dailyEnemy = createTestCharacter();

        when(characterService.getDailyEnemy(CAMPAIGN_ID, USER_ID)).thenReturn(dailyEnemy);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/character-instances/daily-enemy", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void doesHeroExistsForACampaign_WhenHeroExists_ShouldReturnTrue() throws Exception {
        // Arrange
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/character-instances/hero/check-if-exists", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser
    void doesHeroExistsForACampaign_WhenHeroDoesNotExist_ShouldReturnFalse() throws Exception {
        // Arrange
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/character-instances/hero/check-if-exists", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @WithMockUser
    void handleHeroAlreadyCreated_ShouldReturnConflict() throws Exception {
        // Arrange
        CreateHeroRequestDto requestDto = new CreateHeroRequestDto("Hero Name");
        Campaign campaign = new Campaign();

        when(campaignService.getCampaignByIdAndUserId(USER_ID, CAMPAIGN_ID)).thenReturn(campaign);
        when(characterService.createHero(any(Campaign.class), eq(USER_ID), any(CreateHeroRequestDto.class)))
                .thenThrow(new HeroAlreadyCreated("Hero already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/character-instances/hero", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Hero already exists"));
    }

    private CharacterInstance createTestCharacter() {
        CharacterInstance character = new CharacterInstance();
        character.setName("Test Character");
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        character.setCampaign(campaign);
        character.setStats(new Stats());
        Inventory inventory = createBlankInventory();
        character.setInventory(inventory);
        return character;
    }

    private Inventory createBlankInventory() {
        Inventory inventory = new Inventory();
        inventory.setArmors(new java.util.ArrayList<>());
        inventory.setBoots(new java.util.ArrayList<>());
        inventory.setConsumables(new java.util.ArrayList<>());
        inventory.setHelmets(new java.util.ArrayList<>());
        inventory.setShields(new java.util.ArrayList<>());
        inventory.setSpells(new java.util.ArrayList<>());
        inventory.setWeapons(new java.util.ArrayList<>());
        inventory.setGold(0);
        return inventory;
    }
}

