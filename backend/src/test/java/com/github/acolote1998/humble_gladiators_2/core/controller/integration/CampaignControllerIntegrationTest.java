package com.github.acolote1998.humble_gladiators_2.core.controller.integration;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
class CampaignControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    private Campaign campaignUserA;
    private Campaign campaignUserB;

    @BeforeEach
    void setup() {
        campaignUserA = TestDataFactory.persistCampaign(campaignRepository, "userA", "CampaignA");
        TestDataFactory.persistCampaign(campaignRepository, "userA", "CampaignA2");
        campaignUserB = TestDataFactory.persistCampaign(campaignRepository, "userB", "CampaignB");
    }

    @Test
    void getAllCampaignsForAUser_verifiesCampaignListRetrieval() throws Exception {
        mockMvc.perform(get("/api/campaign")
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists());
    }

    @Test
    void getAllCampaignsForAUser_returnsEmptyListWhenNoCampaigns() throws Exception {
        mockMvc.perform(get("/api/campaign")
                        .with(jwt().jwt(j -> j.subject("userC"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getCampaignByUserAndId_verifiesSingleCampaignRetrieval() throws Exception {
        mockMvc.perform(get("/api/campaign/{campaignId}", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(campaignUserA.getId()))
                .andExpect(jsonPath("$.name").value("CampaignA"));
    }

    @Test
    void userIsolation_userACannotRetrieveUserBCampaign() throws Exception {
        // UserA cannot access UserB's campaign - should return 404 Not Found
        mockMvc.perform(get("/api/campaign/{campaignId}", campaignUserB.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void userIsolation_campaignListOnlyReturnsCampaignsForAuthenticatedUser() throws Exception {
        // userA should only see their own campaigns, not userB's
        mockMvc.perform(get("/api/campaign")
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.name == 'CampaignB')]").isEmpty());
    }

    @Test
    void getCampaignByUserAndId_returns404WhenCampaignDoesNotExist() throws Exception {
        // Campaign that doesn't exist should return 404 Not Found
        mockMvc.perform(get("/api/campaign/{campaignId}", 99999L)
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isNotFound());
    }
}

