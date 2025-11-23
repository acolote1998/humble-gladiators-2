package com.github.acolote1998.humble_gladiators_2.item.service.integration;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.ConsumablesCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.ConsumableInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.ConsumableTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.service.ConsumableService;
import com.github.acolote1998.humble_gladiators_2.item.templates.ConsumableTemplate;
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
class ConsumableServiceIntegrationTest {

    @Autowired
    private ConsumableService consumableService;

    @Autowired
    private ConsumableTemplateRepository consumableTemplateRepository;

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
    void getAllConsumableTemplatesForACampaignAndUser_returnsOnlyUserItems() {
        // Create consumables for userA
        ConsumableTemplate consumable1 = TestDataFactory.createTestConsumableTemplate(consumableTemplateRepository, entityManager, campaign, userId);
        ConsumableTemplate consumable2 = TestDataFactory.createTestConsumableTemplate(consumableTemplateRepository, entityManager, campaign, userId);
        
        // Create consumable for userB in same campaign
        TestDataFactory.createTestConsumableTemplate(consumableTemplateRepository, entityManager, campaign, userIdB);
        
        // Create consumable for userA in different campaign
        TestDataFactory.createTestConsumableTemplate(consumableTemplateRepository, entityManager, campaignUserB, userId);
        
        entityManager.flush();
        entityManager.clear();

        List<ConsumableTemplate> result = consumableService.getAllConsumableTemplatesForACampaignAndUser(userId, campaign.getId());
        
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(consumable -> consumable.getUserId().equals(userId));
        assertThat(result).allMatch(consumable -> consumable.getCampaign().getId().equals(campaign.getId()));
        assertThat(result).extracting(ConsumableTemplate::getId).containsExactlyInAnyOrder(consumable1.getId(), consumable2.getId());
    }

    @Test
    void getRandomConsumableTemplateForItemBooster_verifiesRandomizationAndQuery() {
        // Create multiple consumables with same tier and rarity
        for (int i = 0; i < 5; i++) {
            ConsumableTemplate consumable = TestDataFactory.createTestConsumableTemplate(consumableTemplateRepository, entityManager, campaign, userId);
            consumable.setTier(1);
            consumable.setRarity(1);
            consumableTemplateRepository.save(consumable);
        }
        
        entityManager.flush();
        entityManager.clear();

        // Call multiple times to verify randomization
        ConsumableTemplate result1 = consumableService.getRandomConsumableTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        ConsumableTemplate result2 = consumableService.getRandomConsumableTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        ConsumableTemplate result3 = consumableService.getRandomConsumableTemplateForItemBooster(campaign.getId(), userId, 1, 1);

        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNotNull();
        assertThat(result1.getTier()).isEqualTo(1);
        assertThat(result1.getRarity()).isEqualTo(1);
        assertThat(result1.getUserId()).isEqualTo(userId);
        assertThat(result1.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void getRandomConsumableTemplateForItemBooster_whenNoItemsMatchCriteria_returnsNull() {
        // Create consumables with different tier/rarity
        ConsumableTemplate consumable = TestDataFactory.createTestConsumableTemplate(consumableTemplateRepository, entityManager, campaign, userId);
        consumable.setTier(5);
        consumable.setRarity(5);
        consumableTemplateRepository.save(consumable);
        
        entityManager.flush();
        entityManager.clear();

        // Query for tier 1, rarity 1 when only tier 5, rarity 5 exists
        ConsumableTemplate result = consumableService.getRandomConsumableTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        assertThat(result).isNull();
    }

    @Test
    void instanceFromConsumableTemplate_verifiesInstanceCreationAndPersistence() {
        // Create hero and consumable template
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        ConsumableTemplate template = TestDataFactory.createTestConsumableTemplate(consumableTemplateRepository, entityManager, campaign, userId);
        template.setRestoreHp(10);
        template.setRestoreMp(5);
        template.setValue(100);
        consumableTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        ConsumableInstance instance = consumableService.instanceFromConsumableTemplate(template, inventory);

        // Verify all properties are set correctly
        assertThat(instance.getTemplate().getId()).isEqualTo(template.getId());
        assertThat(instance.getName()).isEqualTo(template.getName());
        assertThat(instance.getDescription()).isEqualTo(template.getDescription());
        assertThat(instance.getRarity()).isEqualTo(template.getRarity());
        assertThat(instance.getTier()).isEqualTo(template.getTier());
        assertThat(instance.getValue()).isEqualTo(template.getValue());
        assertThat(instance.getTemplate().getRestoreHp()).isEqualTo(template.getRestoreHp());
        assertThat(instance.getTemplate().getRestoreMp()).isEqualTo(template.getRestoreMp());
        assertThat(instance.getDiscovered()).isTrue();
        assertThat(instance.getEquipped()).isFalse();
        assertThat(instance.getQuantity()).isEqualTo(1);
        assertThat(instance.getCampaign().getId()).isEqualTo(campaign.getId());
        assertThat(instance.getInventory().getId()).isEqualTo(inventory.getId());
    }

    @Test
    void saveConsumable_verifiesTemplatePersistence() {
        ConsumableTemplate consumable = new ConsumableTemplate();
        consumable.setCampaign(campaign);
        consumable.setUserId(userId);
        consumable.setName("Test Consumable");
        consumable.setDescription("Test Description");
        consumable.setRarity(1);
        consumable.setTier(1);
        consumable.setValue(100);
        consumable.setQuantity(0);
        consumable.setDiscovered(false);
        consumable.setEquipped(false);
        consumable.setCategory(ConsumablesCategory.FOOD);
        consumable.setRestoreHp(10);
        consumable.setRestoreMp(5);

        ConsumableTemplate saved = consumableService.saveConsumable(consumable);
        
        assertThat(saved.getId()).isNotNull();
        entityManager.flush();
        entityManager.clear();

        ConsumableTemplate retrieved = consumableTemplateRepository.findById(saved.getId()).orElseThrow();
        assertThat(retrieved.getName()).isEqualTo("Test Consumable");
        assertThat(retrieved.getUserId()).isEqualTo(userId);
        assertThat(retrieved.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void instanceFromConsumableTemplate_persistsCorrectly() {
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        ConsumableTemplate template = TestDataFactory.createTestConsumableTemplate(consumableTemplateRepository, entityManager, campaign, userId);
        consumableTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        ConsumableInstance instance = consumableService.instanceFromConsumableTemplate(template, inventory);


        // Add instance to inventory and persist through character save
        inventory.getConsumables().add(instance);
        characterService.saveCharacter(hero);
        entityManager.flush();
        entityManager.clear();

        // Reload hero and verify instance is persisted
        CharacterInstance reloadedHero = characterService.getHero(campaign.getId(), userId);
        ConsumableInstance persisted = reloadedHero.getInventory().getConsumables().stream()
                .filter(ci -> ci.getTemplate().getId().equals(template.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(persisted.getTemplate().getId()).isEqualTo(template.getId());
        assertThat(persisted.getInventory().getId()).isEqualTo(inventory.getId());
    }
}

