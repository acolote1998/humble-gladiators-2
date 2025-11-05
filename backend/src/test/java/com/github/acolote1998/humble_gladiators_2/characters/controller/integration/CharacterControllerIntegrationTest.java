package com.github.acolote1998.humble_gladiators_2.characters.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.acolote1998.humble_gladiators_2.characters.dto.CreateHeroRequestDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class CharacterControllerIntegrationTest {

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
    }

    @Test
    void createHero_fullFlowWithDatabase() throws Exception {
        CreateHeroRequestDto dto = new CreateHeroRequestDto("HeroName");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/campaign/{id}/character-instances/hero", campaignUserA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.campaignId").value(campaignUserA.getId()));
    }

    @Test
    void getHero_verifiesDatabaseRetrieval() throws Exception {
        TestDataFactory.createHero(characterService, campaignUserA, "userA", "HeroA");

        mockMvc.perform(get("/api/campaign/{id}/character-instances/hero", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HeroA"))
                .andExpect(jsonPath("$.stats").exists())
                .andExpect(jsonPath("$.inventory").exists());
    }

    @Test
    void getAllCharacters_verifiesListRetrieval() throws Exception {
        TestDataFactory.createHero(characterService, campaignUserA, "userA", "HeroA");

        mockMvc.perform(get("/api/campaign/{id}/character-instances", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("HeroA"));
    }

    @Test
    void userIsolation_userACannotAccessUserBHero() throws Exception {
        TestDataFactory.createHero(characterService, campaignUserB, "userB", "HeroB");

        mockMvc.perform(get("/api/campaign/{id}/character-instances/hero", campaignUserB.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void userIsolation_userACannotRetrieveUserBCharacters() throws Exception {
        TestDataFactory.createHero(characterService, campaignUserB, "userB", "HeroB");

        mockMvc.perform(get("/api/campaign/{id}/character-instances", campaignUserB.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createHero_returns409WhenHeroAlreadyExists() throws Exception {
        TestDataFactory.createHero(characterService, campaignUserA, "userA", "HeroA");
        CreateHeroRequestDto dto = new CreateHeroRequestDto("HeroB");
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/campaign/{id}/character-instances/hero", campaignUserA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isConflict());
    }

    @Test
    void getHero_returns404WhenHeroDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/campaign/{id}/character-instances/hero", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isNotFound());
    }
}

