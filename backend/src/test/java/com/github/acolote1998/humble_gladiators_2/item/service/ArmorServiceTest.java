package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.ArmorInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.ArmorTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
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
class ArmorServiceTest {

    @Mock
    private GeminiService geminiService;

    @Mock
    private ArmorTemplateRepository armorTemplateRepository;

    @InjectMocks
    private ArmorService armorService;

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
    void getTier5ArmorsContextForCampaignCover_returnsContextMap() {
        // Arrange
        ArmorTemplate armor = new ArmorTemplate();
        armor.setName("Tier 5 Armor");
        armor.setDescription("Description");
        List<ArmorTemplate> armors = List.of(armor);
        when(armorTemplateRepository.findAllByTierAndCampaign_Id(5, CAMPAIGN_ID)).thenReturn(armors);

        // Act
        Map<String, String> result = armorService.getTier5ArmorsContextForCampaignCover(campaign);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("Tier 5 Armor"));
        assertEquals("Description", result.get("Tier 5 Armor"));
    }

    @Test
    void getShortAIGeneratedReport_returnsSortedReport() {
        // Arrange
        ArmorTemplate armor1 = new ArmorTemplate();
        armor1.setTier(5);
        armor1.setRarity(3);
        armor1.setName("Armor 1");
        armor1.setCategory(ArmorCategory.PLATE);

        ArmorTemplate armor2 = new ArmorTemplate();
        armor2.setTier(3);
        armor2.setRarity(5);
        armor2.setName("Armor 2");
        armor2.setCategory(ArmorCategory.ROBE);

        List<ArmorTemplate> armors = new ArrayList<>(List.of(armor1, armor2));
        when(armorTemplateRepository.findAllByCampaign_Id(CAMPAIGN_ID)).thenReturn(armors);

        // Act
        Map<String, Object> result = armorService.getShortAIGeneratedReport(CAMPAIGN_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("ArmorTemplates"));
    }

    @Test
    void getAllArmorTemplatesForACampaignAndUser_returnsList() {
        // Arrange
        List<ArmorTemplate> armors = Collections.emptyList();
        when(armorTemplateRepository.findAllByUserIdAndCampaign_Id(USER_ID, CAMPAIGN_ID)).thenReturn(armors);

        // Act
        List<ArmorTemplate> result = armorService.getAllArmorTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID);

        // Assert
        assertEquals(armors, result);
    }

    @Test
    void getRandomArmorTemplateForItemBooster_returnsArmor() {
        // Arrange
        ArmorTemplate armor = new ArmorTemplate();
        when(armorTemplateRepository.findRandomByCampaignAndRarityAndTier(
                CAMPAIGN_ID, USER_ID, 3, 2)).thenReturn(armor);

        // Act
        ArmorTemplate result = armorService.getRandomArmorTemplateForItemBooster(CAMPAIGN_ID, USER_ID, 3, 2);

        // Assert
        assertEquals(armor, result);
    }

    @Test
    void saveArmor_savesAndReturnsArmor() {
        // Arrange
        ArmorTemplate armor = new ArmorTemplate();
        when(armorTemplateRepository.save(armor)).thenReturn(armor);

        // Act
        ArmorTemplate result = armorService.saveArmor(armor);

        // Assert
        assertEquals(armor, result);
        verify(armorTemplateRepository).save(armor);
    }

    @Test
    void instanceFromArmorTemplate_createsInstance() {
        // Arrange
        ArmorTemplate template = new ArmorTemplate();
        template.setName("Test Armor");
        template.setDescription("Description");
        template.setRarity(3);
        template.setTier(2);
        template.setValue(100);
        template.setCampaign(campaign);

        Inventory inventory = new Inventory();
        inventory.setArmors(new ArrayList<>());

        // Act
        ArmorInstance result = armorService.instanceFromArmorTemplate(template, inventory);

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
    void instancesFromArmorTemplates_createsMultipleInstances() {
        // Arrange
        ArmorTemplate template1 = new ArmorTemplate();
        template1.setCampaign(campaign);
        ArmorTemplate template2 = new ArmorTemplate();
        template2.setCampaign(campaign);
        List<ArmorTemplate> templates = List.of(template1, template2);

        Inventory inventory = new Inventory();
        inventory.setArmors(new ArrayList<>());

        // Act
        List<ArmorInstance> result = armorService.instancesFromArmorTemplates(templates, inventory);

        // Assert
        assertEquals(2, result.size());
        assertEquals(inventory, result.get(0).getInventory());
        assertEquals(inventory, result.get(1).getInventory());
    }

    @Test
    void getRandomArmorByTierAndRarityAndCampaignAndUserId_returnsArmor() {
        // Arrange
        ArmorTemplate armor = new ArmorTemplate();
        List<ArmorTemplate> armors = List.of(armor);
        when(armorTemplateRepository.findAllByTierAndRarityAndCampaign_IdAndUserId(
                2, 3, CAMPAIGN_ID, USER_ID)).thenReturn(armors);

        // Act
        ArmorTemplate result = armorService.getRandomArmorByTierAndRarityAndCampaignAndUserId(2, 3, CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(armor, result);
    }
}

