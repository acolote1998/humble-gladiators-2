package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.ShieldCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.ShieldInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.ShieldTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.ShieldTemplate;
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
class ShieldServiceTest {

    @Mock
    private GeminiService geminiService;

    @Mock
    private ShieldTemplateRepository shieldTemplateRepository;

    @InjectMocks
    private ShieldService shieldService;

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
    void getTier5ShieldsContextForCampaignCover_ShouldReturnContextMap() {
        // Arrange
        ShieldTemplate shield = new ShieldTemplate();
        shield.setName("Tier 5 Shield");
        shield.setDescription("Description");
        List<ShieldTemplate> shields = List.of(shield);
        when(shieldTemplateRepository.findAllByTierAndCampaign_Id(5, CAMPAIGN_ID)).thenReturn(shields);

        // Act
        Map<String, String> result = shieldService.getTier5ShieldsContextForCampaignCover(campaign);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("Tier 5 Shield"));
        assertEquals("Description", result.get("Tier 5 Shield"));
    }

    @Test
    void getShortAIGeneratedReport_ShouldReturnSortedReport() {
        // Arrange
        ShieldTemplate shield1 = new ShieldTemplate();
        shield1.setTier(5);
        shield1.setRarity(3);
        shield1.setName("Shield 1");
        shield1.setCategory(ShieldCategory.SHIELD);

        List<ShieldTemplate> shields = new ArrayList<>(List.of(shield1));
        when(shieldTemplateRepository.findAllByCampaign_Id(CAMPAIGN_ID)).thenReturn(shields);

        // Act
        Map<String, Object> result = shieldService.getShortAIGeneratedReport(CAMPAIGN_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("ShieldTemplates"));
    }

    @Test
    void getAllShieldTemplatesForACampaignAndUser_ShouldReturnList() {
        // Arrange
        List<ShieldTemplate> shields = Collections.emptyList();
        when(shieldTemplateRepository.findAllByUserIdAndCampaign_Id(USER_ID, CAMPAIGN_ID)).thenReturn(shields);

        // Act
        List<ShieldTemplate> result = shieldService.getAllShieldTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID);

        // Assert
        assertEquals(shields, result);
    }

    @Test
    void getRandomShieldTemplateForItemBooster_ShouldReturnShield() {
        // Arrange
        ShieldTemplate shield = new ShieldTemplate();
        when(shieldTemplateRepository.findRandomByCampaignAndRarityAndTier(
                CAMPAIGN_ID, USER_ID, 3, 2)).thenReturn(shield);

        // Act
        ShieldTemplate result = shieldService.getRandomShieldTemplateForItemBooster(CAMPAIGN_ID, USER_ID, 3, 2);

        // Assert
        assertEquals(shield, result);
    }

    @Test
    void saveShield_ShouldSaveAndReturnShield() {
        // Arrange
        ShieldTemplate shield = new ShieldTemplate();
        when(shieldTemplateRepository.save(shield)).thenReturn(shield);

        // Act
        ShieldTemplate result = shieldService.saveShield(shield);

        // Assert
        assertEquals(shield, result);
        verify(shieldTemplateRepository).save(shield);
    }

    @Test
    void instanceFromShieldTemplate_ShouldCreateInstance() {
        // Arrange
        ShieldTemplate template = new ShieldTemplate();
        template.setName("Test Shield");
        template.setDescription("Description");
        template.setRarity(3);
        template.setTier(2);
        template.setValue(100);
        template.setCampaign(campaign);
        Requirement requirement = new Requirement();
        template.setRequirement(requirement);

        Inventory inventory = new Inventory();
        inventory.setShields(new ArrayList<>());

        // Act
        ShieldInstance result = shieldService.instanceFromShieldTemplate(template, inventory);

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
    void instancesFromShieldTemplates_ShouldCreateMultipleInstances() {
        // Arrange
        ShieldTemplate template1 = new ShieldTemplate();
        template1.setCampaign(campaign);
        ShieldTemplate template2 = new ShieldTemplate();
        template2.setCampaign(campaign);
        List<ShieldTemplate> templates = List.of(template1, template2);

        Inventory inventory = new Inventory();
        inventory.setShields(new ArrayList<>());

        // Act
        List<ShieldInstance> result = shieldService.instancesFromShieldTemplates(templates, inventory);

        // Assert
        assertEquals(2, result.size());
        assertEquals(inventory, result.get(0).getInventory());
        assertEquals(inventory, result.get(1).getInventory());
    }

    @Test
    void getRandomShieldByTierAndRarityAndCampaignAndUserId_ShouldReturnShield() {
        // Arrange
        ShieldTemplate shield = new ShieldTemplate();
        List<ShieldTemplate> shields = List.of(shield);
        when(shieldTemplateRepository.findAllByTierAndRarityAndCampaign_IdAndUserId(
                2, 3, CAMPAIGN_ID, USER_ID)).thenReturn(shields);

        // Act
        ShieldTemplate result = shieldService.getRandomShieldByTierAndRarityAndCampaignAndUserId(2, 3, CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(shield, result);
    }
}

