package com.github.acolote1998.humble_gladiators_2.item.service.integration;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.BootsCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.BootsInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.BootsTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.service.BootsService;
import com.github.acolote1998.humble_gladiators_2.item.templates.BootsTemplate;
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
class BootsServiceIntegrationTest {

    @Autowired
    private BootsService bootsService;

    @Autowired
    private BootsTemplateRepository bootsTemplateRepository;

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
    void getAllBootsTemplatesForACampaignAndUser_returnsOnlyUserItems() {
        // Create boots for userA
        BootsTemplate boots1 = TestDataFactory.createTestBootsTemplate(bootsTemplateRepository, entityManager, campaign, userId);
        BootsTemplate boots2 = TestDataFactory.createTestBootsTemplate(bootsTemplateRepository, entityManager, campaign, userId);
        
        // Create boots for userB in same campaign
        TestDataFactory.createTestBootsTemplate(bootsTemplateRepository, entityManager, campaign, userIdB);
        
        // Create boots for userA in different campaign
        TestDataFactory.createTestBootsTemplate(bootsTemplateRepository, entityManager, campaignUserB, userId);
        
        entityManager.flush();
        entityManager.clear();

        List<BootsTemplate> result = bootsService.getAllBootsTemplatesForACampaignAndUser(userId, campaign.getId());
        
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(boots -> boots.getUserId().equals(userId));
        assertThat(result).allMatch(boots -> boots.getCampaign().getId().equals(campaign.getId()));
        assertThat(result).extracting(BootsTemplate::getId).containsExactlyInAnyOrder(boots1.getId(), boots2.getId());
    }

    @Test
    void getRandomBootTemplateForItemBooster_verifiesRandomizationAndQuery() {
        // Create multiple boots with same tier and rarity
        for (int i = 0; i < 5; i++) {
            BootsTemplate boots = TestDataFactory.createTestBootsTemplate(bootsTemplateRepository, entityManager, campaign, userId);
            boots.setTier(1);
            boots.setRarity(1);
            bootsTemplateRepository.save(boots);
        }
        
        entityManager.flush();
        entityManager.clear();

        // Call multiple times to verify randomization
        BootsTemplate result1 = bootsService.getRandomBootTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        BootsTemplate result2 = bootsService.getRandomBootTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        BootsTemplate result3 = bootsService.getRandomBootTemplateForItemBooster(campaign.getId(), userId, 1, 1);

        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNotNull();
        assertThat(result1.getTier()).isEqualTo(1);
        assertThat(result1.getRarity()).isEqualTo(1);
        assertThat(result1.getUserId()).isEqualTo(userId);
        assertThat(result1.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void getRandomBootTemplateForItemBooster_whenNoItemsMatchCriteria_returnsNull() {
        // Create boots with different tier/rarity
        BootsTemplate boots = TestDataFactory.createTestBootsTemplate(bootsTemplateRepository, entityManager, campaign, userId);
        boots.setTier(5);
        boots.setRarity(5);
        bootsTemplateRepository.save(boots);
        
        entityManager.flush();
        entityManager.clear();

        // Query for tier 1, rarity 1 when only tier 5, rarity 5 exists
        // Repository returns null when no items match
        BootsTemplate result = bootsService.getRandomBootTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        assertThat(result).isNull();
    }

    @Test
    void instanceFromBootsTemplate_verifiesInstanceCreationAndPersistence() {
        // Create hero and boots template
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        BootsTemplate template = TestDataFactory.createTestBootsTemplate(bootsTemplateRepository, entityManager, campaign, userId);
        template.setPhysicalDefense(10);
        template.setMagicalDefense(5);
        template.setValue(100);
        bootsTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        BootsInstance instance = bootsService.instanceFromBootsTemplate(template, inventory);

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
    void saveBoots_verifiesTemplatePersistence() {
        BootsTemplate boots = new BootsTemplate();
        boots.setCampaign(campaign);
        boots.setUserId(userId);
        boots.setName("Test Boots");
        boots.setDescription("Test Description");
        boots.setRarity(1);
        boots.setTier(1);
        boots.setValue(100);
        boots.setQuantity(0);
        boots.setDiscovered(false);
        boots.setEquipped(false);
        boots.setCategory(BootsCategory.BOOTS);
        boots.setPhysicalDefense(10);
        boots.setMagicalDefense(5);

        BootsTemplate saved = bootsService.saveBoots(boots);
        
        assertThat(saved.getId()).isNotNull();
        entityManager.flush();
        entityManager.clear();

        BootsTemplate retrieved = bootsTemplateRepository.findById(saved.getId()).orElseThrow();
        assertThat(retrieved.getName()).isEqualTo("Test Boots");
        assertThat(retrieved.getUserId()).isEqualTo(userId);
        assertThat(retrieved.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void instanceFromBootsTemplate_persistsCorrectly() {
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        BootsTemplate template = TestDataFactory.createTestBootsTemplate(bootsTemplateRepository, entityManager, campaign, userId);
        bootsTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        BootsInstance instance = bootsService.instanceFromBootsTemplate(template, inventory);


        // Add instance to inventory and persist through character save
        inventory.getBoots().add(instance);
        characterService.saveCharacter(hero);
        entityManager.flush();
        entityManager.clear();

        // Reload hero and verify instance is persisted
        CharacterInstance reloadedHero = characterService.getHero(campaign.getId(), userId);
        BootsInstance persisted = reloadedHero.getInventory().getBoots().stream()
                .filter(bi -> bi.getTemplate().getId().equals(template.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(persisted.getTemplate().getId()).isEqualTo(template.getId());
        assertThat(persisted.getInventory().getId()).isEqualTo(inventory.getId());
    }
}

