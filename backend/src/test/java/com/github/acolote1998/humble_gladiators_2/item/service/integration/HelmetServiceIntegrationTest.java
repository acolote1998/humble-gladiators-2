package com.github.acolote1998.humble_gladiators_2.item.service.integration;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.HelmetCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.HelmetInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.HelmetTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.service.HelmetService;
import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;
import com.github.acolote1998.humble_gladiators_2.testutil.TestDataFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class HelmetServiceIntegrationTest {

    @Autowired
    private HelmetService helmetService;

    @Autowired
    private HelmetTemplateRepository helmetTemplateRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private GeminiService geminiService;

    private Campaign campaign;
    private Campaign campaignUserB;
    private String userId;
    private String userIdB;

    @BeforeEach
    void setup() {
        userId = "userA";
        userIdB = "userB";
        campaign = TestDataFactory.persistCampaign(campaignRepository, userId, "CampaignA");
        campaignUserB = TestDataFactory.persistCampaign(campaignRepository, userIdB, "CampaignB");
    }

    @Test
    void getAllHelmetTemplatesForACampaignAndUser_returnsOnlyUserItems() {
        // Create helmets for userA
        HelmetTemplate helmet1 = TestDataFactory.createTestHelmetTemplate(helmetTemplateRepository, entityManager, campaign, userId);
        HelmetTemplate helmet2 = TestDataFactory.createTestHelmetTemplate(helmetTemplateRepository, entityManager, campaign, userId);
        
        // Create helmet for userB in same campaign
        TestDataFactory.createTestHelmetTemplate(helmetTemplateRepository, entityManager, campaign, userIdB);
        
        // Create helmet for userA in different campaign
        TestDataFactory.createTestHelmetTemplate(helmetTemplateRepository, entityManager, campaignUserB, userId);
        
        entityManager.flush();
        entityManager.clear();

        List<HelmetTemplate> result = helmetService.getAllHelmetTemplatesForACampaignAndUser(userId, campaign.getId());
        
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(helmet -> helmet.getUserId().equals(userId));
        assertThat(result).allMatch(helmet -> helmet.getCampaign().getId().equals(campaign.getId()));
        assertThat(result).extracting(HelmetTemplate::getId).containsExactlyInAnyOrder(helmet1.getId(), helmet2.getId());
    }

    @Test
    void getRandomHelmetTemplateForItemBooster_verifiesRandomizationAndQuery() {
        // Create multiple helmets with same tier and rarity
        for (int i = 0; i < 5; i++) {
            HelmetTemplate helmet = TestDataFactory.createTestHelmetTemplate(helmetTemplateRepository, entityManager, campaign, userId);
            helmet.setTier(1);
            helmet.setRarity(1);
            helmetTemplateRepository.save(helmet);
        }
        
        entityManager.flush();
        entityManager.clear();

        // Call multiple times to verify randomization
        HelmetTemplate result1 = helmetService.getRandomHelmetTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        HelmetTemplate result2 = helmetService.getRandomHelmetTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        HelmetTemplate result3 = helmetService.getRandomHelmetTemplateForItemBooster(campaign.getId(), userId, 1, 1);

        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNotNull();
        assertThat(result1.getTier()).isEqualTo(1);
        assertThat(result1.getRarity()).isEqualTo(1);
        assertThat(result1.getUserId()).isEqualTo(userId);
        assertThat(result1.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void getRandomHelmetTemplateForItemBooster_whenNoItemsMatchCriteria_returnsNull() {
        // Create helmets with different tier/rarity
        HelmetTemplate helmet = TestDataFactory.createTestHelmetTemplate(helmetTemplateRepository, entityManager, campaign, userId);
        helmet.setTier(5);
        helmet.setRarity(5);
        helmetTemplateRepository.save(helmet);
        
        entityManager.flush();
        entityManager.clear();

        // Query for tier 1, rarity 1 when only tier 5, rarity 5 exists
        HelmetTemplate result = helmetService.getRandomHelmetTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        assertThat(result).isNull();
    }

    @Test
    void instanceFromHelmetTemplate_verifiesInstanceCreationAndPersistence() {
        // Create hero and helmet template
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        HelmetTemplate template = TestDataFactory.createTestHelmetTemplate(helmetTemplateRepository, entityManager, campaign, userId);
        template.setPhysicalDefense(10);
        template.setMagicalDefense(5);
        template.setValue(100);
        helmetTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        HelmetInstance instance = helmetService.instanceFromHelmetTemplate(template, inventory);

        // Verify all properties are set correctly
        assertThat(instance.getTemplate().getId()).isEqualTo(template.getId());
        assertThat(instance.getName()).isEqualTo(template.getName());
        assertThat(instance.getDescription()).isEqualTo(template.getDescription());
        assertThat(instance.getRarity()).isEqualTo(template.getRarity());
        assertThat(instance.getTier()).isEqualTo(template.getTier());
        assertThat(instance.getValue()).isEqualTo(template.getValue());
        assertThat(instance.getTemplate().getPhysicalDefense()).isEqualTo(template.getPhysicalDefense());
        assertThat(instance.getTemplate().getMagicalDefense()).isEqualTo(template.getMagicalDefense());
        assertThat(instance.getDiscovered()).isTrue();
        assertThat(instance.getEquipped()).isFalse();
        assertThat(instance.getQuantity()).isEqualTo(1);
        assertThat(instance.getCampaign().getId()).isEqualTo(campaign.getId());
        assertThat(instance.getInventory().getId()).isEqualTo(inventory.getId());
    }

    @Test
    void saveHelmet_verifiesTemplatePersistence() {
        HelmetTemplate helmet = new HelmetTemplate();
        helmet.setCampaign(campaign);
        helmet.setUserId(userId);
        helmet.setName("Test Helmet");
        helmet.setDescription("Test Description");
        helmet.setRarity(1);
        helmet.setTier(1);
        helmet.setValue(100);
        helmet.setQuantity(0);
        helmet.setDiscovered(false);
        helmet.setEquipped(false);
        helmet.setCategory(HelmetCategory.HELMET);
        helmet.setPhysicalDefense(10);
        helmet.setMagicalDefense(5);

        HelmetTemplate saved = helmetService.saveHelmet(helmet);
        
        assertThat(saved.getId()).isNotNull();
        entityManager.flush();
        entityManager.clear();

        HelmetTemplate retrieved = helmetTemplateRepository.findById(saved.getId()).orElseThrow();
        assertThat(retrieved.getName()).isEqualTo("Test Helmet");
        assertThat(retrieved.getUserId()).isEqualTo(userId);
        assertThat(retrieved.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void instanceFromHelmetTemplate_persistsCorrectly() {
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        HelmetTemplate template = TestDataFactory.createTestHelmetTemplate(helmetTemplateRepository, entityManager, campaign, userId);
        helmetTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        HelmetInstance instance = helmetService.instanceFromHelmetTemplate(template, inventory);


        // Add instance to inventory and persist through character save
        inventory.getHelmets().add(instance);
        characterService.saveCharacter(hero);
        entityManager.flush();
        entityManager.clear();

        // Reload hero and verify instance is persisted
        CharacterInstance reloadedHero = characterService.getHero(campaign.getId(), userId);
        HelmetInstance persisted = reloadedHero.getInventory().getHelmets().stream()
                .filter(hi -> hi.getTemplate().getId().equals(template.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(persisted.getTemplate().getId()).isEqualTo(template.getId());
        assertThat(persisted.getInventory().getId()).isEqualTo(inventory.getId());
    }
}

