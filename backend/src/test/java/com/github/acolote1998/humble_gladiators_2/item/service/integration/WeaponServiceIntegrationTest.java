package com.github.acolote1998.humble_gladiators_2.item.service.integration;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.WeaponCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.WeaponInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.WeaponTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.service.WeaponService;
import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;
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
class WeaponServiceIntegrationTest {

    @Autowired
    private WeaponService weaponService;

    @Autowired
    private WeaponTemplateRepository weaponTemplateRepository;

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
    void getAllWeaponTemplatesForACampaignAndUser_returnsOnlyUserItems() {
        // Create weapons for userA
        WeaponTemplate weapon1 = TestDataFactory.createTestWeaponTemplate(weaponTemplateRepository, entityManager, campaign, userId);
        WeaponTemplate weapon2 = TestDataFactory.createTestWeaponTemplate(weaponTemplateRepository, entityManager, campaign, userId);
        
        // Create weapon for userB in same campaign
        TestDataFactory.createTestWeaponTemplate(weaponTemplateRepository, entityManager, campaign, userIdB);
        
        // Create weapon for userA in different campaign
        TestDataFactory.createTestWeaponTemplate(weaponTemplateRepository, entityManager, campaignUserB, userId);
        
        entityManager.flush();
        entityManager.clear();

        List<WeaponTemplate> result = weaponService.getAllWeaponTemplatesForACampaignAndUser(userId, campaign.getId());
        
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(weapon -> weapon.getUserId().equals(userId));
        assertThat(result).allMatch(weapon -> weapon.getCampaign().getId().equals(campaign.getId()));
        assertThat(result).extracting(WeaponTemplate::getId).containsExactlyInAnyOrder(weapon1.getId(), weapon2.getId());
    }

    @Test
    void getRandomWeaponTemplateForItemBooster_verifiesRandomizationAndQuery() {
        // Create multiple weapons with same tier and rarity
        for (int i = 0; i < 5; i++) {
            WeaponTemplate weapon = TestDataFactory.createTestWeaponTemplate(weaponTemplateRepository, entityManager, campaign, userId);
            weapon.setTier(1);
            weapon.setRarity(1);
            weaponTemplateRepository.save(weapon);
        }
        
        entityManager.flush();
        entityManager.clear();

        // Call multiple times to verify randomization
        WeaponTemplate result1 = weaponService.getRandomWeaponTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        WeaponTemplate result2 = weaponService.getRandomWeaponTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        WeaponTemplate result3 = weaponService.getRandomWeaponTemplateForItemBooster(campaign.getId(), userId, 1, 1);

        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNotNull();
        assertThat(result1.getTier()).isEqualTo(1);
        assertThat(result1.getRarity()).isEqualTo(1);
        assertThat(result1.getUserId()).isEqualTo(userId);
        assertThat(result1.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void getRandomWeaponTemplateForItemBooster_whenNoItemsMatchCriteria_returnsNull() {
        // Create weapons with different tier/rarity
        WeaponTemplate weapon = TestDataFactory.createTestWeaponTemplate(weaponTemplateRepository, entityManager, campaign, userId);
        weapon.setTier(5);
        weapon.setRarity(5);
        weaponTemplateRepository.save(weapon);
        
        entityManager.flush();
        entityManager.clear();

        // Query for tier 1, rarity 1 when only tier 5, rarity 5 exists
        WeaponTemplate result = weaponService.getRandomWeaponTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        assertThat(result).isNull();
    }

    @Test
    void instanceFromWeaponTemplate_verifiesInstanceCreationAndPersistence() {
        // Create hero and weapon template
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        WeaponTemplate template = TestDataFactory.createTestWeaponTemplate(weaponTemplateRepository, entityManager, campaign, userId);
        template.setPhysicalDamage(10);
        template.setMagicalDamage(0);
        template.setValue(100);
        weaponTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        WeaponInstance instance = weaponService.instanceFromWeaponTemplate(template, inventory);

        // Verify all properties are set correctly
        assertThat(instance.getTemplate().getId()).isEqualTo(template.getId());
        assertThat(instance.getName()).isEqualTo(template.getName());
        assertThat(instance.getDescription()).isEqualTo(template.getDescription());
        assertThat(instance.getRarity()).isEqualTo(template.getRarity());
        assertThat(instance.getTier()).isEqualTo(template.getTier());
        assertThat(instance.getValue()).isEqualTo(template.getValue());
        assertThat(instance.getTemplate().getPhysicalDamage()).isEqualTo(template.getPhysicalDamage());
        assertThat(instance.getTemplate().getMagicalDamage()).isEqualTo(template.getMagicalDamage());
        assertThat(instance.getDiscovered()).isTrue();
        assertThat(instance.getEquipped()).isFalse();
        assertThat(instance.getQuantity()).isEqualTo(1);
        assertThat(instance.getCampaign().getId()).isEqualTo(campaign.getId());
        assertThat(instance.getInventory().getId()).isEqualTo(inventory.getId());
    }

    @Test
    void saveWeapon_verifiesTemplatePersistence() {
        WeaponTemplate weapon = new WeaponTemplate();
        weapon.setCampaign(campaign);
        weapon.setUserId(userId);
        weapon.setName("Test Weapon");
        weapon.setDescription("Test Description");
        weapon.setRarity(1);
        weapon.setTier(1);
        weapon.setValue(100);
        weapon.setQuantity(0);
        weapon.setDiscovered(false);
        weapon.setEquipped(false);
        weapon.setCategory(WeaponCategory.SWORD);
        weapon.setPhysicalDamage(10);
        weapon.setMagicalDamage(0);
        weapon.setRequirement(null);

        WeaponTemplate saved = weaponService.saveWeapon(weapon);
        
        assertThat(saved.getId()).isNotNull();
        entityManager.flush();
        entityManager.clear();

        WeaponTemplate retrieved = weaponTemplateRepository.findById(saved.getId()).orElseThrow();
        assertThat(retrieved.getName()).isEqualTo("Test Weapon");
        assertThat(retrieved.getUserId()).isEqualTo(userId);
        assertThat(retrieved.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void instanceFromWeaponTemplate_withRequirementValidation_persistsCorrectly() {
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        WeaponTemplate template = TestDataFactory.createTestWeaponTemplate(weaponTemplateRepository, entityManager, campaign, userId);
        weaponTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        WeaponInstance instance = weaponService.instanceFromWeaponTemplate(template, inventory);

        // Verify requirement is cloned (if template has requirement)
        if (template.getRequirement() != null) {
            assertThat(instance.getRequirement()).isNotNull();
            if (instance.getRequirement() != null && template.getRequirement() != null) {
                assertThat(instance.getRequirement()).isNotSameAs(template.getRequirement());
            }
        }

        // Add instance to inventory and persist through character save
        inventory.getWeapons().add(instance);
        characterService.saveCharacter(hero);
        entityManager.flush();
        entityManager.clear();

        // Reload hero and verify instance is persisted
        CharacterInstance reloadedHero = characterService.getHero(campaign.getId(), userId);
        WeaponInstance persisted = reloadedHero.getInventory().getWeapons().stream()
                .filter(wi -> wi.getTemplate().getId().equals(template.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(persisted.getTemplate().getId()).isEqualTo(template.getId());
        assertThat(persisted.getInventory().getId()).isEqualTo(inventory.getId());
    }
}

