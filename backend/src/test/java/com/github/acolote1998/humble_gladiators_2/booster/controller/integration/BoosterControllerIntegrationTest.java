package com.github.acolote1998.humble_gladiators_2.booster.controller.integration;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.repository.CharacterInstanceRepository;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RunwareService;
import com.github.acolote1998.humble_gladiators_2.item.repository.*;
import com.github.acolote1998.humble_gladiators_2.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.persistence.EntityManager;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class BoosterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private CharacterInstanceRepository characterInstanceRepository;

    @Autowired
    private WeaponTemplateRepository weaponTemplateRepository;

    @Autowired
    private ArmorTemplateRepository armorTemplateRepository;

    @Autowired
    private BootsTemplateRepository bootsTemplateRepository;

    @Autowired
    private HelmetTemplateRepository helmetTemplateRepository;

    @Autowired
    private ShieldTemplateRepository shieldTemplateRepository;

    @Autowired
    private SpellTemplateRepository spellTemplateRepository;

    @Autowired
    private ConsumableTemplateRepository consumableTemplateRepository;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private RunwareService runwareService;

    @MockitoBean
    private GeminiService geminiService;

    private Campaign campaignUserA;
    private Campaign campaignUserB;

    @BeforeEach
    void setup() {
        campaignUserA = TestDataFactory.persistCampaign(campaignRepository, "userA", "A");
        campaignUserB = TestDataFactory.persistCampaign(campaignRepository, "userB", "B");
        CharacterInstance heroA = characterService.findHeroOrNull(campaignUserA.getId(), "userA");
        if (heroA == null) {
            TestDataFactory.createHero(characterService, campaignUserA, "userA", "HeroA");
        }
        CharacterInstance heroB = characterService.findHeroOrNull(campaignUserB.getId(), "userB");
        if (heroB == null) {
            TestDataFactory.createHero(characterService, campaignUserB, "userB", "HeroB");
        }

        // Create test NPCs and item templates for booster tests
        TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, campaignUserA, "userA");
        TestDataFactory.createAllTestItemTemplates(entityManager, campaignUserA, "userA",
                weaponTemplateRepository, armorTemplateRepository, bootsTemplateRepository,
                helmetTemplateRepository, shieldTemplateRepository, spellTemplateRepository,
                consumableTemplateRepository);

        Mockito.when(runwareService.generateArmorTemplateImageToBytes(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateBootsTemplateImageToBytes(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateConsumableTemplateImageToBytes(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateHelmetTemplateImageToBytes(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateShieldTemplateImageToBytes(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateSpellTemplateImageToBytes(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateWeaponTemplateImageToBytes(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateCharacterInstanceImageToBytes(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateCharacterInstanceBackgroundImageToBytes(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(new byte[]{1});
    }

    @Test
    void itemsBooster_fullHttpFlow_andAvailability() throws Exception {
        // available before
        mockMvc.perform(get("/api/campaign/{id}/items-booster/check-if-available", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));

        // open booster
        mockMvc.perform(post("/api/campaign/{id}/items-booster", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/campaign/" + campaignUserA.getId() + "/items-booster/")))
                .andExpect(jsonPath("$.armors").isArray());

        // unavailable after
        mockMvc.perform(get("/api/campaign/{id}/items-booster/check-if-available", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void characterBooster_fullHttpFlow_andAvailability() throws Exception {
        // available before
        mockMvc.perform(get("/api/campaign/{id}/character-booster/check-if-available", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // open booster
        mockMvc.perform(post("/api/campaign/{id}/character-booster", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/campaign/" + campaignUserA.getId() + "/character-booster/")))
                .andExpect(jsonPath("$.characters").isArray());

        // unavailable after
        mockMvc.perform(get("/api/campaign/{id}/character-booster/check-if-available", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void userIsolation_openBoosterWithDifferentUser_returnsConflict() throws Exception {
        mockMvc.perform(post("/api/campaign/{id}/items-booster", campaignUserB.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/api/campaign/{id}/character-booster", campaignUserB.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isConflict());
    }
}


