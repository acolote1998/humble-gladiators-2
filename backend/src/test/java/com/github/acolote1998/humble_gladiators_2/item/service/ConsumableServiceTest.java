package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.config.GameBalanceConfig;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.ConsumablesCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.ConsumableInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.ConsumableTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.ConsumableTemplate;
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
class ConsumableServiceTest {

    @Mock
    private GeminiService geminiService;

    @Mock
    private ConsumableTemplateRepository consumableTemplateRepository;

    @Mock
    private GameBalanceConfig balanceConfig;

    @InjectMocks
    private ConsumableService consumableService;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;

    private Campaign campaign;

    @BeforeEach
    void setUp() {
        campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        campaign.setUserId(USER_ID);
        
        // Setup GameBalanceConfig mocks (lenient to avoid unnecessary stubbing warnings)
        lenient().when(balanceConfig.getConsumableHpMultiplier()).thenReturn(8);
        lenient().when(balanceConfig.getConsumableMpMultiplier()).thenReturn(12);
    }

    @Test
    void getTier5ConsumablesContextForCampaignCover_returnsContextMap() {
        // Arrange
        ConsumableTemplate consumable = new ConsumableTemplate();
        consumable.setName("Tier 5 Consumable");
        consumable.setDescription("Description");
        List<ConsumableTemplate> consumables = List.of(consumable);
        when(consumableTemplateRepository.findAllByTierAndCampaign_Id(5, CAMPAIGN_ID)).thenReturn(consumables);

        // Act
        Map<String, String> result = consumableService.getTier5ConsumablesContextForCampaignCover(campaign);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("Tier 5 Consumable"));
        assertEquals("Description", result.get("Tier 5 Consumable"));
    }

    @Test
    void getShortAIGeneratedReport_returnsSortedReport() {
        // Arrange
        ConsumableTemplate consumable1 = new ConsumableTemplate();
        consumable1.setTier(5);
        consumable1.setRarity(3);
        consumable1.setName("Consumable 1");
        consumable1.setCategory(ConsumablesCategory.MEDICINE);

        List<ConsumableTemplate> consumables = new ArrayList<>(List.of(consumable1));
        when(consumableTemplateRepository.findAllByCampaign_Id(CAMPAIGN_ID)).thenReturn(consumables);

        // Act
        Map<String, Object> result = consumableService.getShortAIGeneratedReport(CAMPAIGN_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("ConsumableTemplates"));
    }

    @Test
    void getAllConsumableTemplatesForACampaignAndUser_returnsList() {
        // Arrange
        List<ConsumableTemplate> consumables = Collections.emptyList();
        when(consumableTemplateRepository.findAllByUserIdAndCampaign_Id(USER_ID, CAMPAIGN_ID)).thenReturn(consumables);

        // Act
        List<ConsumableTemplate> result = consumableService.getAllConsumableTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID);

        // Assert
        assertEquals(consumables, result);
    }

    @Test
    void getRandomConsumableTemplateForItemBooster_returnsConsumable() {
        // Arrange
        ConsumableTemplate consumable = new ConsumableTemplate();
        when(consumableTemplateRepository.findRandomByCampaignAndRarityAndTier(
                CAMPAIGN_ID, USER_ID, 3, 2)).thenReturn(consumable);

        // Act
        ConsumableTemplate result = consumableService.getRandomConsumableTemplateForItemBooster(CAMPAIGN_ID, USER_ID, 3, 2);

        // Assert
        assertEquals(consumable, result);
    }

    @Test
    void saveConsumable_savesAndReturnsConsumable() {
        // Arrange
        ConsumableTemplate consumable = new ConsumableTemplate();
        when(consumableTemplateRepository.save(consumable)).thenReturn(consumable);

        // Act
        ConsumableTemplate result = consumableService.saveConsumable(consumable);

        // Assert
        assertEquals(consumable, result);
        verify(consumableTemplateRepository).save(consumable);
    }

    @Test
    void instanceFromConsumableTemplate_createsInstance() {
        // Arrange
        ConsumableTemplate template = new ConsumableTemplate();
        template.setName("Test Consumable");
        template.setDescription("Description");
        template.setRarity(3);
        template.setTier(2);
        template.setValue(100);
        template.setCampaign(campaign);
        Requirement requirement = new Requirement();
        template.setRequirement(requirement);

        Inventory inventory = new Inventory();
        inventory.setConsumables(new ArrayList<>());

        // Act
        ConsumableInstance result = consumableService.instanceFromConsumableTemplate(template, inventory);

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
    void instancesFromConsumableTemplates_createsMultipleInstances() {
        // Arrange
        ConsumableTemplate template1 = new ConsumableTemplate();
        template1.setCampaign(campaign);
        ConsumableTemplate template2 = new ConsumableTemplate();
        template2.setCampaign(campaign);
        List<ConsumableTemplate> templates = List.of(template1, template2);

        Inventory inventory = new Inventory();
        inventory.setConsumables(new ArrayList<>());

        // Act
        List<ConsumableInstance> result = consumableService.instancesFromConsumableTemplates(templates, inventory);

        // Assert
        assertEquals(2, result.size());
        assertEquals(inventory, result.get(0).getInventory());
        assertEquals(inventory, result.get(1).getInventory());
    }

    @Test
    void getRandomConsumableByTierAndRarityAndCampaignAndUserId_returnsConsumable() {
        // Arrange
        ConsumableTemplate consumable = new ConsumableTemplate();
        List<ConsumableTemplate> consumables = List.of(consumable);
        when(consumableTemplateRepository.findAllByTierAndRarityAndCampaign_IdAndUserId(
                2, 3, CAMPAIGN_ID, USER_ID)).thenReturn(consumables);

        // Act
        ConsumableTemplate result = consumableService.getRandomConsumableByTierAndRarityAndCampaignAndUserId(2, 3, CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(consumable, result);
    }
}

