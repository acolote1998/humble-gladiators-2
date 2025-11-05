package com.github.acolote1998.humble_gladiators_2.item.controller;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.item.service.WeaponService;
import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;
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

@WebMvcTest(controllers = WeaponTemplateController.class, excludeAutoConfiguration = OAuth2ResourceServerAutoConfiguration.class)
@Import(TestSecurityConfig.class)
class WeaponTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeaponService weaponService;

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
    void getAllWeaponTemplatesForACampaign_ShouldReturnListOfWeapons() throws Exception {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        
        WeaponTemplate weaponTemplate = new WeaponTemplate();
        weaponTemplate.setId(100L);
        weaponTemplate.setName("Test Weapon");
        weaponTemplate.setCampaign(campaign);
        List<WeaponTemplate> weapons = List.of(weaponTemplate);
        
        when(weaponService.getAllWeaponTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID)).thenReturn(weapons);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/weapon-templates", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void getAllWeaponTemplatesForACampaign_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(weaponService.getAllWeaponTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/weapon-templates", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}

