package com.github.acolote1998.humble_gladiators_2.core.controller;

import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.enums.CampaignCreationStateType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import com.github.acolote1998.humble_gladiators_2.core.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.acolote1998.humble_gladiators_2.config.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

@WebMvcTest(controllers = GameController.class, excludeAutoConfiguration = OAuth2ResourceServerAutoConfiguration.class)
@Import(TestSecurityConfig.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

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
    void createNewCampaign_ShouldReturnCreatedResponse() throws Exception {
        // Arrange
        GameCreationDtoRequest.ThemeDtoRequest theme = new GameCreationDtoRequest.ThemeDtoRequest(
                List.of("fantasy", "medieval"),
                Collections.emptyList()
        );
        GameCreationDtoRequest request = new GameCreationDtoRequest("Test Campaign", theme);
        Campaign createdCampaign = new Campaign();
        createdCampaign.setId(CAMPAIGN_ID);
        createdCampaign.setName("Test Campaign");
        createdCampaign.setUserId(USER_ID);

        when(gameService.startGame(any(GameCreationDtoRequest.class), eq(USER_ID))).thenReturn(createdCampaign);
        when(gameService.getCampaignService()).thenReturn(campaignService);

        // Act & Assert
        mockMvc.perform(post("/api/game/campaign")
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/campaign/" + CAMPAIGN_ID));
    }

    @Test
    @WithMockUser
    void getGameCreationState_WhenCampaignExists_ShouldReturnState() throws Exception {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setCampaignCreationState(CampaignCreationStateType.STARTING_NEW_CAMPAIGN);
        when(gameService.getCampaignService()).thenReturn(campaignService);
        when(campaignService.getCampaignBeingCreatedByUserId(USER_ID)).thenReturn(campaign);

        // Act & Assert
        mockMvc.perform(get("/api/game/state")
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("STARTING_NEW_CAMPAIGN"));
    }

    @Test
    @WithMockUser
    void getGameCreationState_WhenCampaignDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(gameService.getCampaignService()).thenReturn(campaignService);
        when(campaignService.getCampaignBeingCreatedByUserId(USER_ID)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/game/state")
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("CAMPAIGN_NOT_FOUND"));
    }
}

