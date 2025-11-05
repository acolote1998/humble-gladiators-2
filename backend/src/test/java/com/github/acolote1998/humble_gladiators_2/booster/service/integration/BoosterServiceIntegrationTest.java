package com.github.acolote1998.humble_gladiators_2.booster.service.integration;

import com.github.acolote1998.humble_gladiators_2.booster.model.CharacterBooster;
import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import com.github.acolote1998.humble_gladiators_2.booster.service.BoosterService;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class BoosterServiceIntegrationTest {

    @Autowired
    private BoosterService boosterService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private CampaignRepository campaignRepository;

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
    private GeminiService geminiService; // Prevent accidental external calls if reached

    private String userId;
    private Campaign campaign;

    @BeforeEach
    void setup() {
        userId = "user-integration";
        campaign = TestDataFactory.persistCampaign(campaignRepository, userId, "Test Campaign");

        // Ensure hero exists
        CharacterInstance hero = characterService.findHeroOrNull(campaign.getId(), userId);
        if (hero == null) {
            TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        }

        // Create test NPCs and item templates for booster tests
        TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, campaign, userId);
        TestDataFactory.createAllTestItemTemplates(entityManager, campaign, userId,
                weaponTemplateRepository, armorTemplateRepository, bootsTemplateRepository,
                helmetTemplateRepository, shieldTemplateRepository, spellTemplateRepository,
                consumableTemplateRepository);

        // Mock all image generation calls to return bytes since GENERATE_IMAGES=true in tests
        Mockito.when(runwareService.generateArmorTemplateImageToBytes(any(), any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateBootsTemplateImageToBytes(any(), any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateConsumableTemplateImageToBytes(any(), any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateHelmetTemplateImageToBytes(any(), any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateShieldTemplateImageToBytes(any(), any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateSpellTemplateImageToBytes(any(), any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateWeaponTemplateImageToBytes(any(), any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateCharacterInstanceImageToBytes(any(), any())).thenReturn(new byte[]{1});
        Mockito.when(runwareService.generateCharacterInstanceBackgroundImageToBytes(any(), any())).thenReturn(new byte[]{1});
    }

    @Test
    void openItemBooster_persistsTemplates_andAddsInstancesToHeroInventory_andDailyAvailabilityToggles() {
        boolean availableBefore = boosterService.userHasDailyItemBoosterAvailable(campaign.getId(), userId);
        assertThat(availableBefore).isTrue();

        ItemsBooster booster = boosterService.getNewItemsBooster(campaign.getId(), userId);
        assertThat(booster.getId()).isNotNull();
        assertThat(booster.getArmors().size()
                + booster.getBoots().size()
                + booster.getConsumables().size()
                + booster.getHelmets().size()
                + booster.getShields().size()
                + booster.getSpells().size()
                + booster.getWeapons().size()).isGreaterThan(0);

        CharacterInstance hero = characterService.getHero(campaign.getId(), userId);
        int totalInstances = hero.getInventory().getArmors().size()
                + hero.getInventory().getBoots().size()
                + hero.getInventory().getConsumables().size()
                + hero.getInventory().getHelmets().size()
                + hero.getInventory().getShields().size()
                + hero.getInventory().getSpells().size()
                + hero.getInventory().getWeapons().size();
        assertThat(totalInstances).isGreaterThan(0);

        boolean availableAfter = boosterService.userHasDailyItemBoosterAvailable(campaign.getId(), userId);
        assertThat(availableAfter).isFalse();
    }

    @Test
    void openCharacterBooster_persistsCharacters() {
        boolean availableBefore = boosterService.userHasDailyCharacterBoosterAvailable(campaign.getId(), userId);
        assertThat(availableBefore).isTrue();

        CharacterBooster booster = boosterService.getNewCharacterBooster(campaign.getId(), userId);
        assertThat(booster.getId()).isNotNull();
        assertThat(booster.getCharacters()).isNotNull();
        assertThat(booster.getCharacters().size()).isGreaterThan(0);

        boolean availableAfter = boosterService.userHasDailyCharacterBoosterAvailable(campaign.getId(), userId);
        assertThat(availableAfter).isFalse();
    }
}


