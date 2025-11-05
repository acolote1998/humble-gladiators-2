package com.github.acolote1998.humble_gladiators_2.booster.controller;

import com.github.acolote1998.humble_gladiators_2.booster.exception.DailyBoosterAlreadyOpened;
import com.github.acolote1998.humble_gladiators_2.booster.exception.InvalidBooster;
import com.github.acolote1998.humble_gladiators_2.booster.model.CharacterBooster;
import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import com.github.acolote1998.humble_gladiators_2.booster.service.BoosterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.acolote1998.humble_gladiators_2.config.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BoosterController.class, excludeAutoConfiguration = OAuth2ResourceServerAutoConfiguration.class)
@Import(TestSecurityConfig.class)
class BoosterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BoosterService boosterService;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;
    private static final Long BOOSTER_ID = 100L;

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
    void openNewItemBooster_ShouldReturnCreatedResponse() throws Exception {
        // Arrange
        ItemsBooster itemsBooster = new ItemsBooster();
        itemsBooster.setId(BOOSTER_ID);
        itemsBooster.setArmors(Collections.emptyList());
        itemsBooster.setBoots(Collections.emptyList());
        itemsBooster.setConsumables(Collections.emptyList());
        itemsBooster.setHelmets(Collections.emptyList());
        itemsBooster.setShields(Collections.emptyList());
        itemsBooster.setSpells(Collections.emptyList());
        itemsBooster.setWeapons(Collections.emptyList());

        when(boosterService.getNewItemsBooster(CAMPAIGN_ID, USER_ID)).thenReturn(itemsBooster);
        doNothing().when(boosterService).discoverContentOfItemBooster(any(ItemsBooster.class));

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/items-booster", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/campaign/" + CAMPAIGN_ID + "/items-booster/" + BOOSTER_ID))
                .andExpect(jsonPath("$.armors").isArray())
                .andExpect(jsonPath("$.boots").isArray())
                .andExpect(jsonPath("$.consumables").isArray())
                .andExpect(jsonPath("$.helmets").isArray())
                .andExpect(jsonPath("$.shields").isArray())
                .andExpect(jsonPath("$.spells").isArray())
                .andExpect(jsonPath("$.weapons").isArray());
    }

    @Test
    @WithMockUser
    void openNewItemBooster_WhenServiceThrowsInvalidBooster_ShouldReturnConflict() throws Exception {
        // Arrange
        when(boosterService.getNewItemsBooster(CAMPAIGN_ID, USER_ID))
                .thenThrow(new InvalidBooster("The attempt to open an item booster is not valid"));

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/items-booster", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("The attempt to open an item booster is not valid"));
    }

    @Test
    @WithMockUser
    void openNewCharacterBooster_ShouldReturnCreatedResponse() throws Exception {
        // Arrange
        CharacterBooster characterBooster = new CharacterBooster();
        characterBooster.setId(BOOSTER_ID);
        characterBooster.setCharacters(Collections.emptyList());

        when(boosterService.getNewCharacterBooster(CAMPAIGN_ID, USER_ID)).thenReturn(characterBooster);
        doNothing().when(boosterService).discoverContentOfCharacterBooster(any(CharacterBooster.class));

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/character-booster", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/campaign/" + CAMPAIGN_ID + "/character-booster/" + BOOSTER_ID))
                .andExpect(jsonPath("$.characters").isArray());
    }

    @Test
    @WithMockUser
    void openNewCharacterBooster_WhenServiceThrowsInvalidBooster_ShouldReturnConflict() throws Exception {
        // Arrange
        when(boosterService.getNewCharacterBooster(CAMPAIGN_ID, USER_ID))
                .thenThrow(new InvalidBooster("The attempt to open an character booster is not valid"));

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/character-booster", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("The attempt to open an character booster is not valid"));
    }

    @Test
    @WithMockUser
    void canPlayerOpenCharacterBooster_WhenAvailable_ShouldReturnTrue() throws Exception {
        // Arrange
        when(boosterService.canOpenAValidCharacterBooster(CAMPAIGN_ID, USER_ID)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/character-booster/check-if-available", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser
    void canPlayerOpenCharacterBooster_WhenNotAvailable_ShouldReturnFalse() throws Exception {
        // Arrange
        when(boosterService.canOpenAValidCharacterBooster(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/character-booster/check-if-available", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @WithMockUser
    void canPlayerOpenItemsBooster_WhenAvailable_ShouldReturnTrue() throws Exception {
        // Arrange
        when(boosterService.canOpenAValidItemBooster(CAMPAIGN_ID, USER_ID)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/items-booster/check-if-available", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser
    void canPlayerOpenItemsBooster_WhenNotAvailable_ShouldReturnFalse() throws Exception {
        // Arrange
        when(boosterService.canOpenAValidItemBooster(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/items-booster/check-if-available", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @WithMockUser
    void handleDailyBoosterAlreadyOpened_ShouldReturnConflict() throws Exception {
        // Arrange
        String errorMessage = "Daily booster already opened";
        when(boosterService.getNewItemsBooster(CAMPAIGN_ID, USER_ID))
                .thenThrow(new DailyBoosterAlreadyOpened(errorMessage));

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/items-booster", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string(errorMessage));
    }

    @Test
    @WithMockUser
    void handleInvalidBooster_ShouldReturnConflict() throws Exception {
        // Arrange
        String errorMessage = "Invalid booster";
        when(boosterService.getNewItemsBooster(CAMPAIGN_ID, USER_ID))
                .thenThrow(new InvalidBooster(errorMessage));

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/items-booster", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string(errorMessage));
    }
}

