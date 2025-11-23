package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.BootsCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.BootsInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.BootsTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.BootsTemplate;
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
class BootsServiceTest {

    @Mock
    private GeminiService geminiService;

    @Mock
    private BootsTemplateRepository bootsTemplateRepository;

    @InjectMocks
    private BootsService bootsService;

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
    void getTier5BootsContextForCampaignCover_returnsContextMap() {
        // Arrange
        BootsTemplate boots = new BootsTemplate();
        boots.setName("Tier 5 Boots");
        boots.setDescription("Description");
        List<BootsTemplate> bootsList = List.of(boots);
        when(bootsTemplateRepository.findAllByTierAndCampaign_Id(5, CAMPAIGN_ID)).thenReturn(bootsList);

        // Act
        Map<String, String> result = bootsService.getTier5BootsContextForCampaignCover(campaign);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("Tier 5 Boots"));
        assertEquals("Description", result.get("Tier 5 Boots"));
    }

    @Test
    void getShortAIGeneratedReport_returnsSortedReport() {
        // Arrange
        BootsTemplate boots1 = new BootsTemplate();
        boots1.setTier(5);
        boots1.setRarity(3);
        boots1.setName("Boots 1");
        boots1.setCategory(BootsCategory.COMBAT_BOOTS);

        List<BootsTemplate> boots = new ArrayList<>(List.of(boots1));
        when(bootsTemplateRepository.findAllByCampaign_Id(CAMPAIGN_ID)).thenReturn(boots);

        // Act
        Map<String, Object> result = bootsService.getShortAIGeneratedReport(CAMPAIGN_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("BootsTemplates"));
    }

    @Test
    void getAllBootsTemplatesForACampaignAndUser_returnsList() {
        // Arrange
        List<BootsTemplate> boots = Collections.emptyList();
        when(bootsTemplateRepository.findAllByUserIdAndCampaign_Id(USER_ID, CAMPAIGN_ID)).thenReturn(boots);

        // Act
        List<BootsTemplate> result = bootsService.getAllBootsTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID);

        // Assert
        assertEquals(boots, result);
    }

    @Test
    void getRandomBootTemplateForItemBooster_returnsBoots() {
        // Arrange
        BootsTemplate boots = new BootsTemplate();
        when(bootsTemplateRepository.findRandomByCampaignAndRarityAndTier(
                CAMPAIGN_ID, USER_ID, 3, 2)).thenReturn(boots);

        // Act
        BootsTemplate result = bootsService.getRandomBootTemplateForItemBooster(CAMPAIGN_ID, USER_ID, 3, 2);

        // Assert
        assertEquals(boots, result);
    }

    @Test
    void saveBoots_savesAndReturnsBoots() {
        // Arrange
        BootsTemplate boots = new BootsTemplate();
        when(bootsTemplateRepository.save(boots)).thenReturn(boots);

        // Act
        BootsTemplate result = bootsService.saveBoots(boots);

        // Assert
        assertEquals(boots, result);
        verify(bootsTemplateRepository).save(boots);
    }

    @Test
    void instanceFromBootsTemplate_createsInstance() {
        // Arrange
        BootsTemplate template = new BootsTemplate();
        template.setName("Test Boots");
        template.setDescription("Description");
        template.setRarity(3);
        template.setTier(2);
        template.setValue(100);
        template.setCampaign(campaign);

        Inventory inventory = new Inventory();
        inventory.setBoots(new ArrayList<>());

        // Act
        BootsInstance result = bootsService.instanceFromBootsTemplate(template, inventory);

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
    void instancesFromBootsTemplates_createsMultipleInstances() {
        // Arrange
        BootsTemplate template1 = new BootsTemplate();
        template1.setCampaign(campaign);
        BootsTemplate template2 = new BootsTemplate();
        template2.setCampaign(campaign);
        List<BootsTemplate> templates = List.of(template1, template2);

        Inventory inventory = new Inventory();
        inventory.setBoots(new ArrayList<>());

        // Act
        List<BootsInstance> result = bootsService.instancesFromBootsTemplates(templates, inventory);

        // Assert
        assertEquals(2, result.size());
        assertEquals(inventory, result.get(0).getInventory());
        assertEquals(inventory, result.get(1).getInventory());
    }

    @Test
    void getRandomBootsByTierAndRarityAndCampaignAndUserId_returnsBoots() {
        // Arrange
        BootsTemplate boots = new BootsTemplate();
        List<BootsTemplate> bootsList = List.of(boots);
        when(bootsTemplateRepository.findAllByTierAndRarityAndCampaign_IdAndUserId(
                2, 3, CAMPAIGN_ID, USER_ID)).thenReturn(bootsList);

        // Act
        BootsTemplate result = bootsService.getRandomBootsByTierAndRarityAndCampaignAndUserId(2, 3, CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(boots, result);
    }
}

