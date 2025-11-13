package com.github.acolote1998.humble_gladiators_2.item.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.core.config.GameBalanceConfig;
import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.enums.WeaponCategory;
import com.github.acolote1998.humble_gladiators_2.item.instances.WeaponInstance;
import com.github.acolote1998.humble_gladiators_2.item.repository.WeaponTemplateRepository;
import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;
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
class WeaponServiceTest {

    @Mock
    private GeminiService geminiService;

    @Mock
    private WeaponTemplateRepository weaponTemplateRepository;

    @Mock
    private GameBalanceConfig balanceConfig;

    @InjectMocks
    private WeaponService weaponService;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;

    private Campaign campaign;

    @BeforeEach
    void setUp() {
        campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        campaign.setUserId(USER_ID);
        
        // Setup GameBalanceConfig mocks (lenient to avoid unnecessary stubbing warnings)
        lenient().when(balanceConfig.getPhysicalDamageMultiplier()).thenReturn(15);
        lenient().when(balanceConfig.getMagicalDamageMultiplier()).thenReturn(15);
    }

    @Test
    void getTier5WeaponsContextForCampaignCover_returnsContextMap() {
        // Arrange
        WeaponTemplate weapon = new WeaponTemplate();
        weapon.setName("Tier 5 Weapon");
        weapon.setDescription("Description");
        List<WeaponTemplate> weapons = List.of(weapon);
        when(weaponTemplateRepository.findAllByTierAndCampaign_Id(5, CAMPAIGN_ID)).thenReturn(weapons);

        // Act
        Map<String, String> result = weaponService.getTier5WeaponsContextForCampaignCover(campaign);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("Tier 5 Weapon"));
        assertEquals("Description", result.get("Tier 5 Weapon"));
    }

    @Test
    void getShortAIGeneratedReport_returnsSortedReport() {
        // Arrange
        WeaponTemplate weapon1 = new WeaponTemplate();
        weapon1.setTier(5);
        weapon1.setRarity(3);
        weapon1.setName("Weapon 1");
        weapon1.setCategory(WeaponCategory.SWORD);

        List<WeaponTemplate> weapons = new ArrayList<>(List.of(weapon1));
        when(weaponTemplateRepository.findAllByCampaign_Id(CAMPAIGN_ID)).thenReturn(weapons);

        // Act
        Map<String, Object> result = weaponService.getShortAIGeneratedReport(CAMPAIGN_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("WeaponTemplates"));
    }

    @Test
    void getAllWeaponTemplatesForACampaignAndUser_returnsList() {
        // Arrange
        List<WeaponTemplate> weapons = Collections.emptyList();
        when(weaponTemplateRepository.findAllByUserIdAndCampaign_Id(USER_ID, CAMPAIGN_ID)).thenReturn(weapons);

        // Act
        List<WeaponTemplate> result = weaponService.getAllWeaponTemplatesForACampaignAndUser(USER_ID, CAMPAIGN_ID);

        // Assert
        assertEquals(weapons, result);
    }

    @Test
    void getRandomWeaponTemplateForItemBooster_returnsWeapon() {
        // Arrange
        WeaponTemplate weapon = new WeaponTemplate();
        when(weaponTemplateRepository.findRandomByCampaignAndRarityAndTier(
                CAMPAIGN_ID, USER_ID, 3, 2)).thenReturn(weapon);

        // Act
        WeaponTemplate result = weaponService.getRandomWeaponTemplateForItemBooster(CAMPAIGN_ID, USER_ID, 3, 2);

        // Assert
        assertEquals(weapon, result);
    }

    @Test
    void saveWeapon_savesAndReturnsWeapon() {
        // Arrange
        WeaponTemplate weapon = new WeaponTemplate();
        when(weaponTemplateRepository.save(weapon)).thenReturn(weapon);

        // Act
        WeaponTemplate result = weaponService.saveWeapon(weapon);

        // Assert
        assertEquals(weapon, result);
        verify(weaponTemplateRepository).save(weapon);
    }

    @Test
    void instanceFromWeaponTemplate_createsInstance() {
        // Arrange
        WeaponTemplate template = new WeaponTemplate();
        template.setName("Test Weapon");
        template.setDescription("Description");
        template.setRarity(3);
        template.setTier(2);
        template.setValue(100);
        template.setCampaign(campaign);
        Requirement requirement = new Requirement();
        template.setRequirement(requirement);

        Inventory inventory = new Inventory();
        inventory.setWeapons(new ArrayList<>());

        // Act
        WeaponInstance result = weaponService.instanceFromWeaponTemplate(template, inventory);

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
    void instancesFromWeaponTemplates_createsMultipleInstances() {
        // Arrange
        WeaponTemplate template1 = new WeaponTemplate();
        template1.setCampaign(campaign);
        WeaponTemplate template2 = new WeaponTemplate();
        template2.setCampaign(campaign);
        List<WeaponTemplate> templates = List.of(template1, template2);

        Inventory inventory = new Inventory();
        inventory.setWeapons(new ArrayList<>());

        // Act
        List<WeaponInstance> result = weaponService.instancesFromWeaponTemplates(templates, inventory);

        // Assert
        assertEquals(2, result.size());
        assertEquals(inventory, result.get(0).getInventory());
        assertEquals(inventory, result.get(1).getInventory());
    }

    @Test
    void getRandomWeaponByTierAndRarityAndCampaignAndUserId_returnsWeapon() {
        // Arrange
        WeaponTemplate weapon = new WeaponTemplate();
        List<WeaponTemplate> weapons = List.of(weapon);
        when(weaponTemplateRepository.findAllByTierAndRarityAndCampaign_IdAndUserId(
                2, 3, CAMPAIGN_ID, USER_ID)).thenReturn(weapons);

        // Act
        WeaponTemplate result = weaponService.getRandomWeaponByTierAndRarityAndCampaignAndUserId(2, 3, CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(weapon, result);
    }

    @Test
    void createTwentyFiveNewWeaponTemplates_retriesWhenEnumInvalid() {
        // Arrange
        ItemFromGeminiDto invalidDto = createWeaponDto("Invalid Weapon", "INVALID_CATEGORY");
        List<ItemFromGeminiDto> validDtos = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            validDtos.add(createWeaponDto("Valid Weapon " + i, "SWORD"));
        }

        when(geminiService.generateTwentyFiveWeapons(campaign))
                .thenReturn(List.of(invalidDto))
                .thenReturn(validDtos);

        // Act
        List<WeaponTemplate> templates = weaponService.createTwentyFiveNewWeaponTemplates(campaign);

        // Assert
        assertEquals(25, templates.size());
        verify(geminiService, times(2)).generateTwentyFiveWeapons(campaign);
        verify(weaponTemplateRepository).saveAll(anyList());
    }

    private ItemFromGeminiDto createWeaponDto(String name, String category) {
        return new ItemFromGeminiDto(
                name,
                "Description",
                1,
                1,
                10,
                false,
                0,
                false,
                CAMPAIGN_ID,
                null,
                category,
                null,
                null,
                null,
                null,
                1,
                0
        );
    }
}

