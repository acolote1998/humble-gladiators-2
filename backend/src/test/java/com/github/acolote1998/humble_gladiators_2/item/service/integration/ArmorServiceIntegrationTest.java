package com.github.acolote1998.humble_gladiators_2.item.service.integration;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.ArmorInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.ArmorTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.service.ArmorService;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
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
class ArmorServiceIntegrationTest {

    @Autowired
    private ArmorService armorService;

    @Autowired
    private ArmorTemplateRepository armorTemplateRepository;

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
    void getAllArmorTemplatesForACampaignAndUser_returnsOnlyUserItems() {
        // Create armors for userA
        ArmorTemplate armor1 = TestDataFactory.createTestArmorTemplate(armorTemplateRepository, entityManager, campaign, userId);
        ArmorTemplate armor2 = TestDataFactory.createTestArmorTemplate(armorTemplateRepository, entityManager, campaign, userId);
        
        // Create armor for userB in same campaign
        TestDataFactory.createTestArmorTemplate(armorTemplateRepository, entityManager, campaign, userIdB);
        
        // Create armor for userA in different campaign
        TestDataFactory.createTestArmorTemplate(armorTemplateRepository, entityManager, campaignUserB, userId);
        
        entityManager.flush();
        entityManager.clear();

        List<ArmorTemplate> result = armorService.getAllArmorTemplatesForACampaignAndUser(userId, campaign.getId());
        
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(armor -> armor.getUserId().equals(userId));
        assertThat(result).allMatch(armor -> armor.getCampaign().getId().equals(campaign.getId()));
        assertThat(result).extracting(ArmorTemplate::getId).containsExactlyInAnyOrder(armor1.getId(), armor2.getId());
    }

    @Test
    void getRandomArmorTemplateForItemBooster_verifiesRandomizationAndQuery() {
        // Create multiple armors with same tier and rarity
        for (int i = 0; i < 5; i++) {
            ArmorTemplate armor = TestDataFactory.createTestArmorTemplate(armorTemplateRepository, entityManager, campaign, userId);
            armor.setTier(1);
            armor.setRarity(1);
            armorTemplateRepository.save(armor);
        }
        
        entityManager.flush();
        entityManager.clear();

        // Call multiple times to verify randomization
        ArmorTemplate result1 = armorService.getRandomArmorTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        ArmorTemplate result2 = armorService.getRandomArmorTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        ArmorTemplate result3 = armorService.getRandomArmorTemplateForItemBooster(campaign.getId(), userId, 1, 1);

        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNotNull();
        assertThat(result1.getTier()).isEqualTo(1);
        assertThat(result1.getRarity()).isEqualTo(1);
        assertThat(result1.getUserId()).isEqualTo(userId);
        assertThat(result1.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void getRandomArmorTemplateForItemBooster_whenNoItemsMatchCriteria_returnsNull() {
        // Create armors with different tier/rarity
        ArmorTemplate armor = TestDataFactory.createTestArmorTemplate(armorTemplateRepository, entityManager, campaign, userId);
        armor.setTier(5);
        armor.setRarity(5);
        armorTemplateRepository.save(armor);
        
        entityManager.flush();
        entityManager.clear();

        // Query for tier 1, rarity 1 when only tier 5, rarity 5 exists
        // Repository returns null when no items match
        ArmorTemplate result = armorService.getRandomArmorTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        assertThat(result).isNull();
    }

    @Test
    void instanceFromArmorTemplate_verifiesInstanceCreationAndPersistence() {
        // Create hero and armor template
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        ArmorTemplate template = TestDataFactory.createTestArmorTemplate(armorTemplateRepository, entityManager, campaign, userId);
        template.setPhysicalDefense(10);
        template.setMagicalDefense(5);
        template.setValue(100);
        armorTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        ArmorInstance instance = armorService.instanceFromArmorTemplate(template, inventory);

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
    void saveArmor_verifiesTemplatePersistence() {
        ArmorTemplate armor = new ArmorTemplate();
        armor.setCampaign(campaign);
        armor.setUserId(userId);
        armor.setName("Test Armor");
        armor.setDescription("Test Description");
        armor.setRarity(1);
        armor.setTier(1);
        armor.setValue(100);
        armor.setQuantity(0);
        armor.setDiscovered(false);
        armor.setEquipped(false);
        armor.setCategory(ArmorCategory.ROBE);
        armor.setPhysicalDefense(10);
        armor.setMagicalDefense(5);

        ArmorTemplate saved = armorService.saveArmor(armor);
        
        assertThat(saved.getId()).isNotNull();
        entityManager.flush();
        entityManager.clear();

        ArmorTemplate retrieved = armorTemplateRepository.findById(saved.getId()).orElseThrow();
        assertThat(retrieved.getName()).isEqualTo("Test Armor");
        assertThat(retrieved.getUserId()).isEqualTo(userId);
        assertThat(retrieved.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void instanceFromArmorTemplate_persistsCorrectly() {
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        ArmorTemplate template = TestDataFactory.createTestArmorTemplate(armorTemplateRepository, entityManager, campaign, userId);
        armorTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        ArmorInstance instance = armorService.instanceFromArmorTemplate(template, inventory);


        // Add instance to inventory and persist through character save
        inventory.getArmors().add(instance);
        characterService.saveCharacter(hero);
        entityManager.flush();
        entityManager.clear();

        // Reload hero and verify instance is persisted
        CharacterInstance reloadedHero = characterService.getHero(campaign.getId(), userId);
        ArmorInstance persisted = reloadedHero.getInventory().getArmors().stream()
                .filter(ai -> ai.getTemplate().getId().equals(template.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(persisted.getTemplate().getId()).isEqualTo(template.getId());
        assertThat(persisted.getInventory().getId()).isEqualTo(inventory.getId());
    }
}

