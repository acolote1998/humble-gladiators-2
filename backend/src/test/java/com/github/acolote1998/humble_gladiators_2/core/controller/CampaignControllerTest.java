package com.github.acolote1998.humble_gladiators_2.core.controller;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Theme;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
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

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CampaignController.class, excludeAutoConfiguration = OAuth2ResourceServerAutoConfiguration.class)
@Import(TestSecurityConfig.class)
class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CampaignService campaignService;

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
    void getAllCampaignsForAUser_ShouldReturnListOfCampaigns() throws Exception {
        // Arrange
        Campaign campaign = createTestCampaign();
        List<Campaign> campaigns = List.of(campaign);
        when(campaignService.getAllCampainsForAUser(USER_ID)).thenReturn(campaigns);

        // Act & Assert
        mockMvc.perform(get("/api/campaign")
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void getAllCampaignsForAUser_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(campaignService.getAllCampainsForAUser(USER_ID)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/campaign")
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser
    void getCampaignByUserAndId_ShouldReturnCampaign() throws Exception {
        // Arrange
        Campaign campaign = createTestCampaign();
        when(campaignService.getCampaignByIdAndUserId(USER_ID, CAMPAIGN_ID)).thenReturn(campaign);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void getCardBackForCampaignByUserAndId_ShouldReturnCardBack() throws Exception {
        // Arrange
        byte[] cardBackImg = new byte[]{1, 2, 3};
        when(campaignService.getBackCardImgForCampaignAndUser(USER_ID, CAMPAIGN_ID)).thenReturn(cardBackImg);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/card-back", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    private Campaign createTestCampaign() {
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        campaign.setUserId(USER_ID);
        campaign.setName("Test Campaign");
        
        Theme theme = new Theme();
        theme.setWantedThemes(List.of("fantasy", "medieval"));
        theme.setUnwantedThemes(List.of("sci-fi"));
        theme.setCampaign(campaign);
        campaign.setTheme(theme);
        
        return campaign;
    }
}

