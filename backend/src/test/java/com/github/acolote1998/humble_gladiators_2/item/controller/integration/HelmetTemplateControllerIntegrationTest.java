package com.github.acolote1998.humble_gladiators_2.item.controller.integration;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.item.repository.HelmetTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.testutil.TestDataFactory;
import jakarta.persistence.EntityManager;
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
class HelmetTemplateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private HelmetTemplateRepository helmetTemplateRepository;

    @Autowired
    private EntityManager entityManager;

    private Campaign campaignUserA;
    private Campaign campaignUserB;
    private String userIdA;
    private String userIdB;

    @BeforeEach
    void setup() {
        userIdA = "userA";
        userIdB = "userB";
        campaignUserA = TestDataFactory.persistCampaign(campaignRepository, userIdA, "CampaignA");
        campaignUserB = TestDataFactory.persistCampaign(campaignRepository, userIdB, "CampaignB");

        // Create helmets for userA
        TestDataFactory.createTestHelmetTemplate(helmetTemplateRepository, entityManager, campaignUserA, userIdA);
        TestDataFactory.createTestHelmetTemplate(helmetTemplateRepository, entityManager, campaignUserA, userIdA);

        // Create helmet for userB
        TestDataFactory.createTestHelmetTemplate(helmetTemplateRepository, entityManager, campaignUserB, userIdB);
    }

    @Test
    void getAllHelmetTemplatesForACampaign_verifiesFullFlowWithDatabase() throws Exception {
        mockMvc.perform(get("/api/campaign/{campaignId}/helmet-templates", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject(userIdA))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void getAllHelmetTemplatesForACampaign_verifiesAuthenticationAndAuthorization() throws Exception {
        mockMvc.perform(get("/api/campaign/{campaignId}/helmet-templates", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject(userIdA))))
                .andExpect(status().isOk());
    }

    @Test
    void getAllHelmetTemplatesForACampaign_userIsolation_userACannotAccessUserBTemplates() throws Exception {
        mockMvc.perform(get("/api/campaign/{campaignId}/helmet-templates", campaignUserB.getId())
                        .with(jwt().jwt(j -> j.subject(userIdA))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

