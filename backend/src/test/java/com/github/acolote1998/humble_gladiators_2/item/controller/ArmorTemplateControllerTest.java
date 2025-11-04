package com.github.acolote1998.humble_gladiators_2.item.controller;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.item.service.ArmorService;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(ArmorTemplateController.class)
class ArmorTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArmorService armorService;

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
    void getAllArmorTemplatesForACampaign_ShouldReturnListOfArmors() throws Exception {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        
        ArmorTemplate armorTemplate = new ArmorTemplate();
        armorTemplate.setId(100L);
        armorTemplate.setName("Test Armor");
        armorTemplate.setCampaign(campaign);
        List<ArmorTemplate> armors = List.of(armorTemplate);
        
        when(armorService.getAllArmorTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID)).thenReturn(armors);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/armor-templates", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void getAllArmorTemplatesForACampaign_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(armorService.getAllArmorTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/armor-templates", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}

