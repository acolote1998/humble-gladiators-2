package com.github.acolote1998.humble_gladiators_2.item.controller;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.item.service.HelmetService;
import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;
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

@WebMvcTest(HelmetTemplateController.class)
class HelmetTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HelmetService helmetService;

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
    void getAllHelmetTemplatesForACampaign_ShouldReturnListOfHelmets() throws Exception {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        
        HelmetTemplate helmetTemplate = new HelmetTemplate();
        helmetTemplate.setId(100L);
        helmetTemplate.setName("Test Helmet");
        helmetTemplate.setCampaign(campaign);
        List<HelmetTemplate> helmets = List.of(helmetTemplate);
        
        when(helmetService.getAllHelmetTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID)).thenReturn(helmets);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/helmet-templates", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void getAllHelmetTemplatesForACampaign_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(helmetService.getAllHelmetTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/helmet-templates", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}

