package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.SpellCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.SpellInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.SpellTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpellServiceTest {

    @Mock
    private GeminiService geminiService;

    @Mock
    private SpellTemplateRepository spellTemplateRepository;

    @InjectMocks
    private SpellService spellService;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;

    private Campaign campaign;

    @BeforeEach
    void setUp() {
        campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        campaign.setUserId(USER_ID);
    }

    @Test
    void getTier5SpellsContextForCampaignCover_ShouldReturnContextMap() {
        // Arrange
        SpellTemplate spell = new SpellTemplate();
        spell.setName("Tier 5 Spell");
        spell.setDescription("Description");
        List<SpellTemplate> spells = List.of(spell);
        when(spellTemplateRepository.findAllByTierAndCampaign_Id(5, CAMPAIGN_ID)).thenReturn(spells);

        // Act
        Map<String, String> result = spellService.getTier5SpellsContextForCampaignCover(campaign);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("Tier 5 Spell"));
        assertEquals("Description", result.get("Tier 5 Spell"));
    }

    @Test
    void getShortAIGeneratedReport_ShouldReturnSortedReport() {
        // Arrange
        SpellTemplate spell1 = new SpellTemplate();
        spell1.setTier(5);
        spell1.setRarity(3);
        spell1.setName("Spell 1");
        spell1.setCategory(SpellCategory.HEALING_SPELL);

        List<SpellTemplate> spells = new ArrayList<>(List.of(spell1));
        when(spellTemplateRepository.findAllByCampaign_Id(CAMPAIGN_ID)).thenReturn(spells);

        // Act
        Map<String, Object> result = spellService.getShortAIGeneratedReport(CAMPAIGN_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("SpellTemplates"));
    }

    @Test
    void getAllSpellTemplatesForACampaignAndUser_ShouldReturnList() {
        // Arrange
        List<SpellTemplate> spells = Collections.emptyList();
        when(spellTemplateRepository.findAllByUserIdAndCampaign_Id(USER_ID, CAMPAIGN_ID)).thenReturn(spells);

        // Act
        List<SpellTemplate> result = spellService.getAllSpellTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID);

        // Assert
        assertEquals(spells, result);
    }

    @Test
    void getRandomSpellTemplateForItemBooster_ShouldReturnSpell() {
        // Arrange
        SpellTemplate spell = new SpellTemplate();
        when(spellTemplateRepository.findRandomByCampaignAndRarityAndTier(
                CAMPAIGN_ID, USER_ID, 3, 2)).thenReturn(spell);

        // Act
        SpellTemplate result = spellService.getRandomSpellTemplateForItemBooster(CAMPAIGN_ID, USER_ID, 3, 2);

        // Assert
        assertEquals(spell, result);
    }

    @Test
    void saveSpell_ShouldSaveAndReturnSpell() {
        // Arrange
        SpellTemplate spell = new SpellTemplate();
        when(spellTemplateRepository.save(spell)).thenReturn(spell);

        // Act
        SpellTemplate result = spellService.saveSpell(spell);

        // Assert
        assertEquals(spell, result);
        verify(spellTemplateRepository).save(spell);
    }

    @Test
    void instanceFromSpellTemplate_ShouldCreateInstance() {
        // Arrange
        SpellTemplate template = new SpellTemplate();
        template.setName("Test Spell");
        template.setDescription("Description");
        template.setRarity(3);
        template.setTier(2);
        template.setValue(100);
        template.setCampaign(campaign);
        Requirement requirement = new Requirement();
        template.setRequirement(requirement);

        Inventory inventory = new Inventory();
        inventory.setSpells(new ArrayList<>());

        // Act
        SpellInstance result = spellService.instanceFromSpellTemplate(template, inventory);

        // Assert
        assertNotNull(result);
        assertEquals(template.getName(), result.getName());
        assertEquals(template.getDescription(), result.getDescription());
        assertEquals(template.getRarity(), result.getRarity());
        assertEquals(template.getTier(), result.getTier());
        assertEquals(template.getValue(), result.getValue());
        assertEquals(template.getCampaign(), result.getCampaign());
        assertEquals(inventory, result.getInventory());
        assertTrue(result.getDiscovered());
        assertFalse(result.getEquipped());
        assertEquals(1, result.getQuantity());
    }

    @Test
    void instancesFromSpellTemplates_ShouldCreateMultipleInstances() {
        // Arrange
        SpellTemplate template1 = new SpellTemplate();
        template1.setCampaign(campaign);
        SpellTemplate template2 = new SpellTemplate();
        template2.setCampaign(campaign);
        List<SpellTemplate> templates = List.of(template1, template2);

        Inventory inventory = new Inventory();
        inventory.setSpells(new ArrayList<>());

        // Act
        List<SpellInstance> result = spellService.instancesFromSpellTemplates(templates, inventory);

        // Assert
        assertEquals(2, result.size());
        assertEquals(inventory, result.get(0).getInventory());
        assertEquals(inventory, result.get(1).getInventory());
    }

    @Test
    void getRandomSpellByTierAndRarityAndCampaignAndUserId_ShouldReturnSpell() {
        // Arrange
        SpellTemplate spell = new SpellTemplate();
        List<SpellTemplate> spells = List.of(spell);
        when(spellTemplateRepository.findAllByTierAndRarityAndCampaign_IdAndUserId(
                2, 3, CAMPAIGN_ID, USER_ID)).thenReturn(spells);

        // Act
        SpellTemplate result = spellService.getRandomSpellByTierAndRarityAndCampaignAndUserId(2, 3, CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(spell, result);
    }
}

