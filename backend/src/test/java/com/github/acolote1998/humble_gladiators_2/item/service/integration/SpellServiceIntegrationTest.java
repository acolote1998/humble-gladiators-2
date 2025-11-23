package com.github.acolote1998.humble_gladiators_2.item.service.integration;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.SpellCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.SpellInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.SpellTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.service.SpellService;
import com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate;
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
class SpellServiceIntegrationTest {

    @Autowired
    private SpellService spellService;

    @Autowired
    private SpellTemplateRepository spellTemplateRepository;

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
    void getAllSpellTemplatesForACampaignAndUser_returnsOnlyUserItems() {
        // Create spells for userA
        SpellTemplate spell1 = TestDataFactory.createTestSpellTemplate(spellTemplateRepository, entityManager, campaign, userId);
        SpellTemplate spell2 = TestDataFactory.createTestSpellTemplate(spellTemplateRepository, entityManager, campaign, userId);
        
        // Create spell for userB in same campaign
        TestDataFactory.createTestSpellTemplate(spellTemplateRepository, entityManager, campaign, userIdB);
        
        // Create spell for userA in different campaign
        TestDataFactory.createTestSpellTemplate(spellTemplateRepository, entityManager, campaignUserB, userId);
        
        entityManager.flush();
        entityManager.clear();

        List<SpellTemplate> result = spellService.getAllSpellTemplatesForACampaignAndUser(userId, campaign.getId());
        
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(spell -> spell.getUserId().equals(userId));
        assertThat(result).allMatch(spell -> spell.getCampaign().getId().equals(campaign.getId()));
        assertThat(result).extracting(SpellTemplate::getId).containsExactlyInAnyOrder(spell1.getId(), spell2.getId());
    }

    @Test
    void getRandomSpellTemplateForItemBooster_verifiesRandomizationAndQuery() {
        // Create multiple spells with same tier and rarity
        for (int i = 0; i < 5; i++) {
            SpellTemplate spell = TestDataFactory.createTestSpellTemplate(spellTemplateRepository, entityManager, campaign, userId);
            spell.setTier(1);
            spell.setRarity(1);
            spellTemplateRepository.save(spell);
        }
        
        entityManager.flush();
        entityManager.clear();

        // Call multiple times to verify randomization
        SpellTemplate result1 = spellService.getRandomSpellTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        SpellTemplate result2 = spellService.getRandomSpellTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        SpellTemplate result3 = spellService.getRandomSpellTemplateForItemBooster(campaign.getId(), userId, 1, 1);

        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNotNull();
        assertThat(result1.getTier()).isEqualTo(1);
        assertThat(result1.getRarity()).isEqualTo(1);
        assertThat(result1.getUserId()).isEqualTo(userId);
        assertThat(result1.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void getRandomSpellTemplateForItemBooster_whenNoItemsMatchCriteria_returnsNull() {
        // Create spells with different tier/rarity
        SpellTemplate spell = TestDataFactory.createTestSpellTemplate(spellTemplateRepository, entityManager, campaign, userId);
        spell.setTier(5);
        spell.setRarity(5);
        spellTemplateRepository.save(spell);
        
        entityManager.flush();
        entityManager.clear();

        // Query for tier 1, rarity 1 when only tier 5, rarity 5 exists
        SpellTemplate result = spellService.getRandomSpellTemplateForItemBooster(campaign.getId(), userId, 1, 1);
        assertThat(result).isNull();
    }

    @Test
    void instanceFromSpellTemplate_verifiesInstanceCreationAndPersistence() {
        // Create hero and spell template
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        SpellTemplate template = TestDataFactory.createTestSpellTemplate(spellTemplateRepository, entityManager, campaign, userId);
        template.setPhysicalDamage(0);
        template.setMagicalDamage(10);
        template.setRestoreHp(0);
        template.setMpCost(5);
        template.setValue(100);
        spellTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        SpellInstance instance = spellService.instanceFromSpellTemplate(template, inventory);

        // Verify all properties are set correctly
        assertThat(instance.getTemplate().getId()).isEqualTo(template.getId());
        assertThat(instance.getName()).isEqualTo(template.getName());
        assertThat(instance.getDescription()).isEqualTo(template.getDescription());
        assertThat(instance.getRarity()).isEqualTo(template.getRarity());
        assertThat(instance.getTier()).isEqualTo(template.getTier());
        assertThat(instance.getValue()).isEqualTo(template.getValue());
        assertThat(instance.getTemplate().getPhysicalDamage()).isEqualTo(template.getPhysicalDamage());
        assertThat(instance.getTemplate().getMagicalDamage()).isEqualTo(template.getMagicalDamage());
        assertThat(instance.getTemplate().getRestoreHp()).isEqualTo(template.getRestoreHp());
        assertThat(instance.getTemplate().getMpCost()).isEqualTo(template.getMpCost());
        assertThat(instance.getDiscovered()).isTrue();
        assertThat(instance.getEquipped()).isFalse();
        assertThat(instance.getQuantity()).isEqualTo(1);
        assertThat(instance.getCampaign().getId()).isEqualTo(campaign.getId());
        assertThat(instance.getInventory().getId()).isEqualTo(inventory.getId());
    }

    @Test
    void saveSpell_verifiesTemplatePersistence() {
        SpellTemplate spell = new SpellTemplate();
        spell.setCampaign(campaign);
        spell.setUserId(userId);
        spell.setName("Test Spell");
        spell.setDescription("Test Description");
        spell.setRarity(1);
        spell.setTier(1);
        spell.setValue(100);
        spell.setQuantity(0);
        spell.setDiscovered(false);
        spell.setEquipped(false);
        spell.setCategory(SpellCategory.FIRE_SPELL);
        spell.setPhysicalDamage(0);
        spell.setMagicalDamage(10);
        spell.setRestoreHp(0);
        spell.setMpCost(5);

        SpellTemplate saved = spellService.saveSpell(spell);
        
        assertThat(saved.getId()).isNotNull();
        entityManager.flush();
        entityManager.clear();

        SpellTemplate retrieved = spellTemplateRepository.findById(saved.getId()).orElseThrow();
        assertThat(retrieved.getName()).isEqualTo("Test Spell");
        assertThat(retrieved.getUserId()).isEqualTo(userId);
        assertThat(retrieved.getCampaign().getId()).isEqualTo(campaign.getId());
    }

    @Test
    void instanceFromSpellTemplate_persistsCorrectly() {
        CharacterInstance hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        SpellTemplate template = TestDataFactory.createTestSpellTemplate(spellTemplateRepository, entityManager, campaign, userId);
        spellTemplateRepository.save(template);
        
        entityManager.flush();
        entityManager.clear();

        Inventory inventory = hero.getInventory();
        SpellInstance instance = spellService.instanceFromSpellTemplate(template, inventory);

        // Add instance to inventory and persist through character save
        inventory.getSpells().add(instance);
        characterService.saveCharacter(hero);
        entityManager.flush();
        entityManager.clear();

        // Reload hero and verify instance is persisted
        CharacterInstance reloadedHero = characterService.getHero(campaign.getId(), userId);
        SpellInstance persisted = reloadedHero.getInventory().getSpells().stream()
                .filter(si -> si.getTemplate().getId().equals(template.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(persisted.getTemplate().getId()).isEqualTo(template.getId());
        assertThat(persisted.getInventory().getId()).isEqualTo(inventory.getId());
    }
}

