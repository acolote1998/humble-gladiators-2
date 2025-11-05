package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.HelmetCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.HelmetInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.HelmetTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;
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
class HelmetServiceTest {

    @Mock
    private GeminiService geminiService;

    @Mock
    private HelmetTemplateRepository helmetTemplateRepository;

    @InjectMocks
    private HelmetService helmetService;

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
    void getTier5HelmetsContextForCampaignCover_returnsContextMap() {
        // Arrange
        HelmetTemplate helmet = new HelmetTemplate();
        helmet.setName("Tier 5 Helmet");
        helmet.setDescription("Description");
        List<HelmetTemplate> helmets = List.of(helmet);
        when(helmetTemplateRepository.findAllByTierAndCampaign_Id(5, CAMPAIGN_ID)).thenReturn(helmets);

        // Act
        Map<String, String> result = helmetService.getTier5HelmetsContextForCampaignCover(campaign);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("Tier 5 Helmet"));
        assertEquals("Description", result.get("Tier 5 Helmet"));
    }

    @Test
    void getShortAIGeneratedReport_returnsSortedReport() {
        // Arrange
        HelmetTemplate helmet1 = new HelmetTemplate();
        helmet1.setTier(5);
        helmet1.setRarity(3);
        helmet1.setName("Helmet 1");
        helmet1.setCategory(HelmetCategory.HELMET);

        List<HelmetTemplate> helmets = new ArrayList<>(List.of(helmet1));
        when(helmetTemplateRepository.findAllByCampaign_Id(CAMPAIGN_ID)).thenReturn(helmets);

        // Act
        Map<String, Object> result = helmetService.getShortAIGeneratedReport(CAMPAIGN_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("HelmetTemplates"));
    }

    @Test
    void getAllHelmetTemplatesForACampaignAndUser_returnsList() {
        // Arrange
        List<HelmetTemplate> helmets = Collections.emptyList();
        when(helmetTemplateRepository.findAllByUserIdAndCampaign_Id(USER_ID, CAMPAIGN_ID)).thenReturn(helmets);

        // Act
        List<HelmetTemplate> result = helmetService.getAllHelmetTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID);

        // Assert
        assertEquals(helmets, result);
    }

    @Test
    void getRandomHelmetTemplateForItemBooster_returnsHelmet() {
        // Arrange
        HelmetTemplate helmet = new HelmetTemplate();
        when(helmetTemplateRepository.findRandomByCampaignAndRarityAndTier(
                CAMPAIGN_ID, USER_ID, 3, 2)).thenReturn(helmet);

        // Act
        HelmetTemplate result = helmetService.getRandomHelmetTemplateForItemBooster(CAMPAIGN_ID, USER_ID, 3, 2);

        // Assert
        assertEquals(helmet, result);
    }

    @Test
    void saveHelmet_savesAndReturnsHelmet() {
        // Arrange
        HelmetTemplate helmet = new HelmetTemplate();
        when(helmetTemplateRepository.save(helmet)).thenReturn(helmet);

        // Act
        HelmetTemplate result = helmetService.saveHelmet(helmet);

        // Assert
        assertEquals(helmet, result);
        verify(helmetTemplateRepository).save(helmet);
    }

    @Test
    void instanceFromHelmetTemplate_createsInstance() {
        // Arrange
        HelmetTemplate template = new HelmetTemplate();
        template.setName("Test Helmet");
        template.setDescription("Description");
        template.setRarity(3);
        template.setTier(2);
        template.setValue(100);
        template.setCampaign(campaign);
        Requirement requirement = new Requirement();
        template.setRequirement(requirement);

        Inventory inventory = new Inventory();
        inventory.setHelmets(new ArrayList<>());

        // Act
        HelmetInstance result = helmetService.instanceFromHelmetTemplate(template, inventory);

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
    void instancesFromHelmetTemplates_createsMultipleInstances() {
        // Arrange
        HelmetTemplate template1 = new HelmetTemplate();
        template1.setCampaign(campaign);
        HelmetTemplate template2 = new HelmetTemplate();
        template2.setCampaign(campaign);
        List<HelmetTemplate> templates = List.of(template1, template2);

        Inventory inventory = new Inventory();
        inventory.setHelmets(new ArrayList<>());

        // Act
        List<HelmetInstance> result = helmetService.instancesFromHelmetTemplates(templates, inventory);

        // Assert
        assertEquals(2, result.size());
        assertEquals(inventory, result.get(0).getInventory());
        assertEquals(inventory, result.get(1).getInventory());
    }

    @Test
    void getRandomHelmetByTierAndRarityAndCampaignAndUserId_returnsHelmet() {
        // Arrange
        HelmetTemplate helmet = new HelmetTemplate();
        List<HelmetTemplate> helmets = List.of(helmet);
        when(helmetTemplateRepository.findAllByTierAndRarityAndCampaign_IdAndUserId(
                2, 3, CAMPAIGN_ID, USER_ID)).thenReturn(helmets);

        // Act
        HelmetTemplate result = helmetService.getRandomHelmetByTierAndRarityAndCampaignAndUserId(2, 3, CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(helmet, result);
    }
}

