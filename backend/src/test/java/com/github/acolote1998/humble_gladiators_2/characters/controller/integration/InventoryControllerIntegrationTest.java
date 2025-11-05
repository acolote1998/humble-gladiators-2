package com.github.acolote1998.humble_gladiators_2.characters.controller.integration;

import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class InventoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CharacterService characterService;

    @MockitoBean
    private GeminiService geminiService;

    private Campaign campaignUserA;
    private Campaign campaignUserB;

    @BeforeEach
    void setup() {
        campaignUserA = TestDataFactory.persistCampaign(campaignRepository, "userA", "CampaignA");
        campaignUserB = TestDataFactory.persistCampaign(campaignRepository, "userB", "CampaignB");
        TestDataFactory.createHero(characterService, campaignUserA, "userA", "HeroA");
        TestDataFactory.createHero(characterService, campaignUserB, "userB", "HeroB");
    }

    @Test
    void unequipItem_fullFlow() throws Exception {
        mockMvc.perform(patch("/api/campaign/{id}/character-instances/hero/unequip/armor", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk());
    }

    @Test
    void userIsolation_userACannotEquipItemsFromUserBInventory() throws Exception {
        mockMvc.perform(patch("/api/campaign/{id}/character-instances/hero/equip/armor/{itemId}", campaignUserB.getId(), 999L)
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void userIsolation_userACannotAccessUserBCharacterInventory() throws Exception {
        mockMvc.perform(patch("/api/campaign/{id}/character-instances/hero/unequip/armor", campaignUserB.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isNotFound());
    }
}

