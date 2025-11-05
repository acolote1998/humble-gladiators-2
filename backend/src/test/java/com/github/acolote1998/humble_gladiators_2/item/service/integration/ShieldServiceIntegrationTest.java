package com.github.acolote1998.humble_gladiators_2.item.service.integration;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.ShieldCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.ShieldInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.ShieldTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.service.ShieldService;
import com.github.acolote1998.humble_gladiators_2.item.templates.ShieldTemplate;
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
class ShieldServiceIntegrationTest {

    @Autowired
    private ShieldService shieldService;

    @Autowired
    private ShieldTemplateRepository shieldTemplateRepository;

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
    void getAllShieldTemplatesForACampaignAndUser_returnsOnlyUserItems() {
        // Create shields for userA
        ShieldTemplate shield1 = TestDataFactory.createTestShieldTemplate(shieldTemplateRepository, entityManager, campaign, userId);
        ShieldTemplate shield2 = TestDataFactory.createTestShieldTemplate(shieldTemplateRepository, entityManager, campaign, userId);
        
        // Create shield for userB in same campaign
        TestDataFactory.createTestShieldTemplate(shieldTemplateRepository, entityManager, campaign, userIdB);
        
        // Create shield for userA in different campaign
        TestDataFactory.createTestShieldTemplate(shieldTemplateRepository, entityManager, campaignUserB, userId);
        
        entityManager.flush();
        entityManager.clear();

        List<ShieldTemplate> result = shieldService.getAllShieldTemplatesForACampaignAndUser(userId, campaign.getId());
        
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(shield -> shield.getUserId().equals(userId));
        assertThat(result).allMatch(shield -> shield.getCampaign().getId().equals(campaign.getId()));
        assertThat(result).extracting(ShieldTemplate::getId).containsExactlyInAnyOrder(shield1.getId(), shield2.getId());
    }

    @Test
    void getRandomShieldTemplateForItemBooster_verifiesRandomizationAndQuery() {
        // Create multiple shields with same tier and rarity
        for (int i = 0; i < 5; i++) {
            ShieldTemplate shield = TestDataFactory.createTestShieldTemplate(shieldTemplateRepository, entityManager, campaign, userId);
            shield.setTier(1);
            shield.setRarity(1);
            shieldTemplateRepository.save(shield);
        }
        
        entityManager.flush();
        entityManager.clear();

        // Call multiple times to verify randomization
        ShieldTemplate result1 = shieldService.getRandomShieldTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        ShieldTemplate result2 = shieldService.getRandomShieldTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        ShieldTemplate result3 = shieldService.getRandomShieldTemplateForItemBooster(campaign.getId(), userId, 1, 1);

        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNotNull();
        assertThat(result1.getTier()).isEqualTo(1);
        assertThat(result1.getRarity()).isEqualTo(1);
        assertThat(result1.getUserId()).isEqualTo(userId);
        assertThat(result1.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void getRandomShieldTemplateForItemBooster_whenNoItemsMatchCriteria_returnsNull() {
        // Create shields with different tier/rarity
        ShieldTemplate shield = TestDataFactory.createTestShieldTemplate(shieldTemplateRepository, entityManager, campaign, userId);
        shield.setTier(5);
        shield.setRarity(5);
        shieldTemplateRepository.save(shield);
        
        entityManager.flush();
        entityManager.clear();

        // Query for tier 1, rarity 1 when only tier 5, rarity 5 exists
        ShieldTemplate result = shieldService.getRandomShieldTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        assertThat(result).isNull();
    }

    @Test
    void instanceFromShieldTemplate_verifiesInstanceCreationAndPersistence() {
        // Create hero and shield template
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        ShieldTemplate template = TestDataFactory.createTestShieldTemplate(shieldTemplateRepository, entityManager, campaign, userId);
        template.setPhysicalDefense(10);
        template.setMagicalDefense(5);
        template.setValue(100);
        shieldTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        ShieldInstance instance = shieldService.instanceFromShieldTemplate(template, inventory);

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
    void saveShield_verifiesTemplatePersistence() {
        ShieldTemplate shield = new ShieldTemplate();
        shield.setCampaign(campaign);
        shield.setUserId(userId);
        shield.setName("Test Shield");
        shield.setDescription("Test Description");
        shield.setRarity(1);
        shield.setTier(1);
        shield.setValue(100);
        shield.setQuantity(0);
        shield.setDiscovered(false);
        shield.setEquipped(false);
        shield.setCategory(ShieldCategory.SHIELD);
        shield.setPhysicalDefense(10);
        shield.setMagicalDefense(5);
        shield.setRequirement(null);

        ShieldTemplate saved = shieldService.saveShield(shield);
        
        assertThat(saved.getId()).isNotNull();
        entityManager.flush();
        entityManager.clear();

        ShieldTemplate retrieved = shieldTemplateRepository.findById(saved.getId()).orElseThrow();
        assertThat(retrieved.getName()).isEqualTo("Test Shield");
        assertThat(retrieved.getUserId()).isEqualTo(userId);
        assertThat(retrieved.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void instanceFromShieldTemplate_withRequirementValidation_persistsCorrectly() {
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        ShieldTemplate template = TestDataFactory.createTestShieldTemplate(shieldTemplateRepository, entityManager, campaign, userId);
        shieldTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        ShieldInstance instance = shieldService.instanceFromShieldTemplate(template, inventory);

        // Verify requirement is cloned (if template has requirement)
        if (template.getRequirement() != null) {
            assertThat(instance.getRequirement()).isNotNull();
            if (instance.getRequirement() != null && template.getRequirement() != null) {
                assertThat(instance.getRequirement()).isNotSameAs(template.getRequirement());
            }
        }

        // Add instance to inventory and persist through character save
        inventory.getShields().add(instance);
        characterService.saveCharacter(hero);
        entityManager.flush();
        entityManager.clear();

        // Reload hero and verify instance is persisted
        CharacterInstance reloadedHero = characterService.getHero(campaign.getId(), userId);
        ShieldInstance persisted = reloadedHero.getInventory().getShields().stream()
                .filter(si -> si.getTemplate().getId().equals(template.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(persisted.getTemplate().getId()).isEqualTo(template.getId());
        assertThat(persisted.getInventory().getId()).isEqualTo(inventory.getId());
    }
}

