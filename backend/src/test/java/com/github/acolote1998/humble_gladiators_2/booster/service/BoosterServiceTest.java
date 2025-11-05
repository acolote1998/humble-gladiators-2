package com.github.acolote1998.humble_gladiators_2.booster.service;

import com.github.acolote1998.humble_gladiators_2.booster.exception.InvalidBooster;
import com.github.acolote1998.humble_gladiators_2.booster.model.CharacterBooster;
import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import com.github.acolote1998.humble_gladiators_2.booster.repository.CharacterBoosterRepository;
import com.github.acolote1998.humble_gladiators_2.booster.repository.ItemsBoosterRepository;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.BattleUtil;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import com.github.acolote1998.humble_gladiators_2.core.service.RunwareService;
import com.github.acolote1998.humble_gladiators_2.item.service.*;
import com.github.acolote1998.humble_gladiators_2.item.templates.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoosterServiceTest {

    @Mock
    private ArmorService armorService;

    @Mock
    private BootsService bootsService;

    @Mock
    private ConsumableService consumableService;

    @Mock
    private HelmetService helmetService;

    @Mock
    private ShieldService shieldService;

    @Mock
    private SpellService spellService;

    @Mock
    private WeaponService weaponService;

    @Mock
    private CampaignService campaignService;

    @Mock
    private ItemsBoosterRepository itemsBoosterRepository;

    @Mock
    private CharacterService characterService;

    @Mock
    private CharacterBoosterRepository characterBoosterRepository;

    @Mock
    private RunwareService runwareService;

    @Mock
    private BattleUtil battleUtil;

    @InjectMocks
    private BoosterService boosterService;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;

    @BeforeEach
    void setUp() throws Exception {
        // Set @Value fields using reflection
        setFieldValue("UNLIMITED_BOOSTERS_ALLOWED", false);
        setFieldValue("IMAGE_GENERATION_ACTIVATED", false);
        setFieldValue("NPC_ITEM_GENERATION_CHANCE", 50);
        setFieldValue("CARD_RARITY_ONE_CHANCE", 20);
        setFieldValue("CARD_RARITY_TWO_CHANCE", 20);
        setFieldValue("CARD_RARITY_THREE_CHANCE", 20);
        setFieldValue("CARD_RARITY_FOUR_CHANCE", 20);
        setFieldValue("CARD_RARITY_FIVE_CHANCE", 20);
        setFieldValue("CARD_TIER_ONE_CHANCE", 20);
        setFieldValue("CARD_TIER_TWO_CHANCE", 20);
        setFieldValue("CARD_TIER_THREE_CHANCE", 20);
        setFieldValue("CARD_TIER_FOUR_CHANCE", 20);
        setFieldValue("CARD_TIER_FIVE_CHANCE", 20);
        setFieldValue("AMOUNT_OF_CARDS_PER_ITEM_BOOSTER", 3);
        setFieldValue("AMOUNT_OF_CARDS_PER_CHARACTER_BOOSTER", 1);
    }

    private void setFieldValue(String fieldName, Object value) throws Exception {
        Field field = BoosterService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(boosterService, value);
    }

    @Test
    void canOpenAValidItemBooster_WhenAllConditionsMet_ShouldReturnTrue() {
        // Arrange
        when(itemsBoosterRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(true);
        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act
        Boolean result = boosterService.canOpenAValidItemBooster(CAMPAIGN_ID, USER_ID);

        // Assert
        assertTrue(result);
    }

    @Test
    void canOpenAValidItemBooster_WhenDailyBoosterAlreadyOpened_ShouldReturnFalse() {
        // Arrange
        ItemsBooster existingBooster = new ItemsBooster();
        when(itemsBoosterRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(existingBooster);

        // Act
        Boolean result = boosterService.canOpenAValidItemBooster(CAMPAIGN_ID, USER_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void canOpenAValidItemBooster_WhenHeroDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(itemsBoosterRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act
        Boolean result = boosterService.canOpenAValidItemBooster(CAMPAIGN_ID, USER_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void canOpenAValidItemBooster_WhenBattleOngoing_ShouldReturnFalse() {
        // Arrange
        when(itemsBoosterRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(true);
        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(true);

        // Act
        Boolean result = boosterService.canOpenAValidItemBooster(CAMPAIGN_ID, USER_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void canOpenAValidCharacterBooster_WhenAllConditionsMet_ShouldReturnTrue() {
        // Arrange
        when(characterBoosterRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(true);
        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act
        Boolean result = boosterService.canOpenAValidCharacterBooster(CAMPAIGN_ID, USER_ID);

        // Assert
        assertTrue(result);
    }

    @Test
    void canOpenAValidCharacterBooster_WhenDailyBoosterAlreadyOpened_ShouldReturnFalse() {
        // Arrange
        CharacterBooster existingBooster = new CharacterBooster();
        when(characterBoosterRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(existingBooster);

        // Act
        Boolean result = boosterService.canOpenAValidCharacterBooster(CAMPAIGN_ID, USER_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void userHasDailyItemBoosterAvailable_WhenUnlimitedBoostersAllowed_ShouldReturnTrue() throws Exception {
        // Arrange
        setFieldValue("UNLIMITED_BOOSTERS_ALLOWED", true);

        // Act
        Boolean result = boosterService.userHasDailyItemBoosterAvailable(CAMPAIGN_ID, USER_ID);

        // Assert
        assertTrue(result);
        verify(itemsBoosterRepository, never()).findByCampaignIdAndUserIdAndUpdatedAtDate(
                anyLong(), anyString(), any(LocalDate.class));
    }

    @Test
    void userHasDailyItemBoosterAvailable_WhenNoBoosterOpenedToday_ShouldReturnTrue() {
        // Arrange
        when(itemsBoosterRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);

        // Act
        Boolean result = boosterService.userHasDailyItemBoosterAvailable(CAMPAIGN_ID, USER_ID);

        // Assert
        assertTrue(result);
    }

    @Test
    void userHasDailyItemBoosterAvailable_WhenBoosterOpenedToday_ShouldReturnFalse() {
        // Arrange
        ItemsBooster todaysBooster = new ItemsBooster();
        when(itemsBoosterRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(todaysBooster);

        // Act
        Boolean result = boosterService.userHasDailyItemBoosterAvailable(CAMPAIGN_ID, USER_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void userHasDailyCharacterBoosterAvailable_WhenUnlimitedBoostersAllowed_ShouldReturnTrue() throws Exception {
        // Arrange
        setFieldValue("UNLIMITED_BOOSTERS_ALLOWED", true);

        // Act
        Boolean result = boosterService.userHasDailyCharacterBoosterAvailable(CAMPAIGN_ID, USER_ID);

        // Assert
        assertTrue(result);
        verify(characterBoosterRepository, never()).findByCampaignIdAndUserIdAndUpdatedAtDate(
                anyLong(), anyString(), any(LocalDate.class));
    }

    @Test
    void getCalculatedTier_WithValidConfiguration_ShouldReturnValidTier() {
        // Act
        Integer tier = boosterService.getCalculatedTier();

        // Assert
        assertNotNull(tier);
        assertTrue(tier >= 1 && tier <= 5);
    }

    @Test
    void getCalculatedTier_WithInvalidConfiguration_ShouldReturnDefaultTier() throws Exception {
        // Arrange
        setFieldValue("CARD_TIER_ONE_CHANCE", 10);
        setFieldValue("CARD_TIER_TWO_CHANCE", 10);
        setFieldValue("CARD_TIER_THREE_CHANCE", 10);
        setFieldValue("CARD_TIER_FOUR_CHANCE", 10);
        setFieldValue("CARD_TIER_FIVE_CHANCE", 10); // Total = 50, not 100

        // Act
        Integer tier = boosterService.getCalculatedTier();

        // Assert
        assertEquals(1, tier); // Default tier when configuration is invalid
    }

    @Test
    void getCalculatedRarity_WithValidConfiguration_ShouldReturnValidRarity() {
        // Act
        Integer rarity = boosterService.getCalculatedRarity();

        // Assert
        assertNotNull(rarity);
        assertTrue(rarity >= 1 && rarity <= 5);
    }

    @Test
    void getCalculatedRarity_WithInvalidConfiguration_ShouldReturnDefaultRarity() throws Exception {
        // Arrange
        setFieldValue("CARD_RARITY_ONE_CHANCE", 10);
        setFieldValue("CARD_RARITY_TWO_CHANCE", 10);
        setFieldValue("CARD_RARITY_THREE_CHANCE", 10);
        setFieldValue("CARD_RARITY_FOUR_CHANCE", 10);
        setFieldValue("CARD_RARITY_FIVE_CHANCE", 10); // Total = 50, not 100

        // Act
        Integer rarity = boosterService.getCalculatedRarity();

        // Assert
        assertEquals(1, rarity); // Default rarity when configuration is invalid
    }

    @Test
    void getNewItemsBooster_WhenCanOpenBooster_ShouldReturnItemsBooster() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        campaign.setUserId(USER_ID);

        CharacterInstance hero = new CharacterInstance();
        Inventory inventory = new Inventory();
        hero.setInventory(inventory);
        inventory.setArmors(new ArrayList<>());
        inventory.setBoots(new ArrayList<>());
        inventory.setConsumables(new ArrayList<>());
        inventory.setHelmets(new ArrayList<>());
        inventory.setShields(new ArrayList<>());
        inventory.setSpells(new ArrayList<>());
        inventory.setWeapons(new ArrayList<>());

        ArmorTemplate armorTemplate = new ArmorTemplate();
        armorTemplate.setName("Test Armor");
        armorTemplate.setDiscovered(false);

        when(itemsBoosterRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(true);
        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);
        when(campaignService.getCampaignByIdAndUserId(USER_ID, CAMPAIGN_ID)).thenReturn(campaign);
        
        // Use lenient stubbing since getRandomItemType() randomly selects item types
        // We stub all item services to handle any random selection
        lenient().when(armorService.getRandomArmorTemplateForItemBooster(
                eq(CAMPAIGN_ID), eq(USER_ID), anyInt(), anyInt())).thenReturn(armorTemplate);
        lenient().when(armorService.saveArmor(any(ArmorTemplate.class))).thenReturn(armorTemplate);
        
        // Stub other item services as well since any type might be randomly selected
        BootsTemplate bootsTemplate = new BootsTemplate();
        bootsTemplate.setName("Test Boots");
        lenient().when(bootsService.getRandomBootTemplateForItemBooster(
                eq(CAMPAIGN_ID), eq(USER_ID), anyInt(), anyInt())).thenReturn(bootsTemplate);
        lenient().when(bootsService.saveBoots(any())).thenReturn(bootsTemplate);
        
        ConsumableTemplate consumableTemplate = new ConsumableTemplate();
        consumableTemplate.setName("Test Consumable");
        lenient().when(consumableService.getRandomConsumableTemplateForItemBooster(
                eq(CAMPAIGN_ID), eq(USER_ID), anyInt(), anyInt())).thenReturn(consumableTemplate);
        lenient().when(consumableService.saveConsumable(any())).thenReturn(consumableTemplate);
        
        HelmetTemplate helmetTemplate = new HelmetTemplate();
        helmetTemplate.setName("Test Helmet");
        lenient().when(helmetService.getRandomHelmetTemplateForItemBooster(
                eq(CAMPAIGN_ID), eq(USER_ID), anyInt(), anyInt())).thenReturn(helmetTemplate);
        lenient().when(helmetService.saveHelmet(any())).thenReturn(helmetTemplate);
        
        ShieldTemplate shieldTemplate = new ShieldTemplate();
        shieldTemplate.setName("Test Shield");
        lenient().when(shieldService.getRandomShieldTemplateForItemBooster(
                eq(CAMPAIGN_ID), eq(USER_ID), anyInt(), anyInt())).thenReturn(shieldTemplate);
        lenient().when(shieldService.saveShield(any())).thenReturn(shieldTemplate);
        
        SpellTemplate spellTemplate = new SpellTemplate();
        spellTemplate.setName("Test Spell");
        lenient().when(spellService.getRandomSpellTemplateForItemBooster(
                eq(CAMPAIGN_ID), eq(USER_ID), anyInt(), anyInt())).thenReturn(spellTemplate);
        lenient().when(spellService.saveSpell(any())).thenReturn(spellTemplate);
        
        WeaponTemplate weaponTemplate = new WeaponTemplate();
        weaponTemplate.setName("Test Weapon");
        lenient().when(weaponService.getRandomWeaponTemplateForItemBooster(
                eq(CAMPAIGN_ID), eq(USER_ID), anyInt(), anyInt())).thenReturn(weaponTemplate);
        lenient().when(weaponService.saveWeapon(any())).thenReturn(weaponTemplate);
        when(armorService.instancesFromArmorTemplates(anyList(), any(Inventory.class)))
                .thenReturn(Collections.emptyList());
        when(bootsService.instancesFromBootsTemplates(anyList(), any(Inventory.class)))
                .thenReturn(Collections.emptyList());
        when(consumableService.instancesFromConsumableTemplates(anyList(), any(Inventory.class)))
                .thenReturn(Collections.emptyList());
        when(helmetService.instancesFromHelmetTemplates(anyList(), any(Inventory.class)))
                .thenReturn(Collections.emptyList());
        when(shieldService.instancesFromShieldTemplates(anyList(), any(Inventory.class)))
                .thenReturn(Collections.emptyList());
        when(spellService.instancesFromSpellTemplates(anyList(), any(Inventory.class)))
                .thenReturn(Collections.emptyList());
        when(weaponService.instancesFromWeaponTemplates(anyList(), any(Inventory.class)))
                .thenReturn(Collections.emptyList());
        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        when(characterService.saveCharacter(any(CharacterInstance.class))).thenReturn(hero);

        ItemsBooster savedBooster = new ItemsBooster();
        savedBooster.setId(100L);
        when(itemsBoosterRepository.save(any(ItemsBooster.class))).thenReturn(savedBooster);

        // Act
        ItemsBooster result = boosterService.getNewItemsBooster(CAMPAIGN_ID, USER_ID);

        // Assert
        assertNotNull(result);
        verify(itemsBoosterRepository).save(any(ItemsBooster.class));
    }

    @Test
    void getNewItemsBooster_WhenCannotOpenBooster_ShouldThrowInvalidBooster() {
        // Arrange
        when(itemsBoosterRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidBooster.class, () -> {
            boosterService.getNewItemsBooster(CAMPAIGN_ID, USER_ID);
        });
    }

    @Test
    void getNewCharacterBooster_WhenCannotOpenBooster_ShouldThrowInvalidBooster() {
        // Arrange
        when(characterBoosterRepository.findByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidBooster.class, () -> {
            boosterService.getNewCharacterBooster(CAMPAIGN_ID, USER_ID);
        });
    }

    @Test
    void discoverContentOfItemBooster_ShouldSetDiscoveredToTrue() {
        // Arrange
        ItemsBooster booster = new ItemsBooster();
        List<ArmorTemplate> armors = new ArrayList<>();
        ArmorTemplate armor = new ArmorTemplate();
        armor.setName("Test Armor");
        armor.setDiscovered(false);
        armors.add(armor);
        booster.setArmors(armors);
        booster.setBoots(new ArrayList<>());
        booster.setConsumables(new ArrayList<>());
        booster.setHelmets(new ArrayList<>());
        booster.setShields(new ArrayList<>());
        booster.setSpells(new ArrayList<>());
        booster.setWeapons(new ArrayList<>());

        when(armorService.saveArmor(any(ArmorTemplate.class))).thenReturn(armor);

        // Act
        boosterService.discoverContentOfItemBooster(booster);

        // Assert
        assertTrue(armor.getDiscovered());
        verify(armorService).saveArmor(armor);
    }

    @Test
    void discoverContentOfItemBooster_WhenAlreadyDiscovered_ShouldNotSaveAgain() {
        // Arrange
        ItemsBooster booster = new ItemsBooster();
        List<ArmorTemplate> armors = new ArrayList<>();
        ArmorTemplate armor = new ArmorTemplate();
        armor.setName("Test Armor");
        armor.setDiscovered(true); // Already discovered
        armors.add(armor);
        booster.setArmors(armors);
        booster.setBoots(new ArrayList<>());
        booster.setConsumables(new ArrayList<>());
        booster.setHelmets(new ArrayList<>());
        booster.setShields(new ArrayList<>());
        booster.setSpells(new ArrayList<>());
        booster.setWeapons(new ArrayList<>());

        // Act
        boosterService.discoverContentOfItemBooster(booster);

        // Assert
        verify(armorService, never()).saveArmor(armor);
    }

    @Test
    void discoverContentOfCharacterBooster_ShouldSetDiscoveredToTrue() {
        // Arrange
        CharacterBooster booster = new CharacterBooster();
        List<CharacterInstance> characters = new ArrayList<>();
        CharacterInstance character = new CharacterInstance();
        character.setName("Test Character");
        character.setDiscovered(false);
        characters.add(character);
        booster.setCharacters(characters);

        when(characterService.saveCharacter(any(CharacterInstance.class))).thenReturn(character);

        // Act
        boosterService.discoverContentOfCharacterBooster(booster);

        // Assert
        assertTrue(character.getDiscovered());
        verify(characterService).saveCharacter(character);
    }

    @Test
    void discoverContentOfCharacterBooster_WhenAlreadyDiscovered_ShouldNotSaveAgain() {
        // Arrange
        CharacterBooster booster = new CharacterBooster();
        List<CharacterInstance> characters = new ArrayList<>();
        CharacterInstance character = new CharacterInstance();
        character.setName("Test Character");
        character.setDiscovered(true); // Already discovered
        characters.add(character);
        booster.setCharacters(characters);

        // Act
        boosterService.discoverContentOfCharacterBooster(booster);

        // Assert
        verify(characterService, never()).saveCharacter(character);
    }

    @Test
    void doesCharacterGenerateThisItem_ShouldReturnBoolean() {
        // Act
        Boolean result = boosterService.doesCharacterGenerateThisItem();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }
}

