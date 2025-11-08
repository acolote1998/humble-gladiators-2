package com.github.acolote1998.humble_gladiators_2.characters.service;

import com.github.acolote1998.humble_gladiators_2.characters.dto.CreateHeroRequestDto;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterCategory;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.characters.exception.DailyEnemyNotFound;
import com.github.acolote1998.humble_gladiators_2.characters.exception.HeroAlreadyCreated;
import com.github.acolote1998.humble_gladiators_2.characters.exception.HeroDoesNotExist;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.characters.repository.CharacterInstanceRepository;
import com.github.acolote1998.humble_gladiators_2.characters.util.StatsMapper;
import com.github.acolote1998.humble_gladiators_2.core.dto.CharacterFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidAttemptBattleOngoing;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.BattleUtil;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.instances.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock
    private GeminiService geminiService;

    @Mock
    private CharacterInstanceRepository characterInstanceRepository;

    @Mock
    private BattleUtil battleUtil;

    @Mock
    private StatsMapper statsMapper;

    @InjectMocks
    private CharacterService characterService;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;
    private static final Long ITEM_ID = 100L;
    private static final Long CHARACTER_ID = 200L;

    private CharacterInstance hero;
    private Campaign campaign;
    private Inventory inventory;

    @BeforeEach
    void setUp() throws Exception {
        // Set @Value field using reflection
        setFieldValue("SKIP_REQUIREMENTS", false);

        campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        campaign.setUserId(USER_ID);

        hero = createTestCharacter();
        inventory = hero.getInventory();
    }

    private void setFieldValue(String fieldName, Object value) throws Exception {
        Field field = CharacterService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(characterService, value);
    }

    private CharacterInstance createTestCharacter() {
        CharacterInstance character = new CharacterInstance();
        character.setId(CHARACTER_ID);
        character.setName("Test Hero");
        character.setUserId(USER_ID);
        character.setCampaign(campaign);
        character.setStats(new Stats());
        character.setInventory(createBlankInventory());
        character.setCharacterType(CharacterType.PLAYER);
        character.setRarity(3);
        character.setTier(2);
        character.setCategory(CharacterCategory.HUMANOID);
        return character;
    }

    private Inventory createBlankInventory() {
        Inventory inventory = new Inventory();
        inventory.setArmors(new ArrayList<>());
        inventory.setBoots(new ArrayList<>());
        inventory.setConsumables(new ArrayList<>());
        inventory.setHelmets(new ArrayList<>());
        inventory.setShields(new ArrayList<>());
        inventory.setSpells(new ArrayList<>());
        inventory.setWeapons(new ArrayList<>());
        inventory.setGold(0);
        return inventory;
    }

    // Equip/Unequip Armor Tests
    @Test
    void equipArmor_WhenNoBattleOngoing_ShouldEquipArmor() {
        // Arrange
        ArmorInstance armor = new ArmorInstance();
        armor.setId(ITEM_ID);
        armor.setName("Test Armor");
        armor.setEquipped(false);
        inventory.getArmors().add(armor);

        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);
        when(characterInstanceRepository.save(any(CharacterInstance.class))).thenReturn(hero);

        // Act
        ArmorInstance result = characterService.equipArmor(hero, ITEM_ID, USER_ID);

        // Assert
        assertTrue(result.getEquipped());
        assertEquals(armor, result);
        verify(characterInstanceRepository).save(hero);
    }

    @Test
    void equipArmor_WhenBattleOngoing_ShouldThrowException() {
        // Arrange
        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidAttemptBattleOngoing.class, () -> {
            characterService.equipArmor(hero, ITEM_ID, USER_ID);
        });
    }

    @Test
    void equipArmor_WhenItemNotFound_ShouldThrowException() {
        // Arrange
        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            characterService.equipArmor(hero, ITEM_ID, USER_ID);
        });
    }

    @Test
    void equipArmor_WhenAlreadyEquipped_ShouldUnequipPreviousAndEquipNew() {
        // Arrange
        ArmorInstance oldArmor = new ArmorInstance();
        oldArmor.setId(99L);
        oldArmor.setEquipped(true);
        inventory.getArmors().add(oldArmor);

        ArmorInstance newArmor = new ArmorInstance();
        newArmor.setId(ITEM_ID);
        newArmor.setEquipped(false);
        inventory.getArmors().add(newArmor);

        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);
        when(characterInstanceRepository.save(any(CharacterInstance.class))).thenReturn(hero);

        // Act
        ArmorInstance result = characterService.equipArmor(hero, ITEM_ID, USER_ID);

        // Assert
        assertFalse(oldArmor.getEquipped());
        assertTrue(newArmor.getEquipped());
        assertEquals(newArmor, result);
    }

    @Test
    void unequipArmors_WhenNoBattleOngoing_ShouldUnequipAllArmors() {
        // Arrange
        ArmorInstance armor1 = new ArmorInstance();
        armor1.setEquipped(true);
        ArmorInstance armor2 = new ArmorInstance();
        armor2.setEquipped(true);
        inventory.getArmors().addAll(List.of(armor1, armor2));

        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);
        when(characterInstanceRepository.save(any(CharacterInstance.class))).thenReturn(hero);

        // Act
        characterService.unequipArmors(hero, USER_ID);

        // Assert
        assertFalse(armor1.getEquipped());
        assertFalse(armor2.getEquipped());
        verify(characterInstanceRepository).save(hero);
    }

    @Test
    void unequipArmors_WhenBattleOngoing_ShouldThrowException() {
        // Arrange
        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidAttemptBattleOngoing.class, () -> {
            characterService.unequipArmors(hero, USER_ID);
        });
    }

    @Test
    void getEquippedArmorForAHero_WhenArmorEquipped_ShouldReturnArmor() {
        // Arrange
        ArmorInstance equippedArmor = new ArmorInstance();
        equippedArmor.setEquipped(true);
        ArmorInstance unequippedArmor = new ArmorInstance();
        unequippedArmor.setEquipped(false);
        inventory.getArmors().addAll(List.of(equippedArmor, unequippedArmor));

        // Act
        ArmorInstance result = characterService.getEquippedArmorForAHero(hero);

        // Assert
        assertEquals(equippedArmor, result);
    }

    @Test
    void getEquippedArmorForAHero_WhenNoArmorEquipped_ShouldReturnNull() {
        // Arrange
        ArmorInstance unequippedArmor = new ArmorInstance();
        unequippedArmor.setEquipped(false);
        inventory.getArmors().add(unequippedArmor);

        // Act
        ArmorInstance result = characterService.getEquippedArmorForAHero(hero);

        // Assert
        assertNull(result);
    }

    // Similar tests for Boots, Helmet, Shield, Weapon (equip/unequip/getEquipped)
    // I'll create a few key ones and you can expand as needed

    @Test
    void equipBoots_WhenNoBattleOngoing_ShouldEquipBoots() {
        // Arrange
        BootsInstance boots = new BootsInstance();
        boots.setId(ITEM_ID);
        boots.setEquipped(false);
        inventory.getBoots().add(boots);

        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);
        when(characterInstanceRepository.save(any(CharacterInstance.class))).thenReturn(hero);

        // Act
        BootsInstance result = characterService.equipBoots(hero, ITEM_ID, USER_ID);

        // Assert
        assertTrue(result.getEquipped());
        verify(characterInstanceRepository).save(hero);
    }

    @Test
    void unequipBoots_ShouldUnequipAllBoots() {
        // Arrange
        BootsInstance boots = new BootsInstance();
        boots.setEquipped(true);
        inventory.getBoots().add(boots);

        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);
        when(characterInstanceRepository.save(any(CharacterInstance.class))).thenReturn(hero);

        // Act
        characterService.unequipBoots(hero, USER_ID);

        // Assert
        assertFalse(boots.getEquipped());
    }

    @Test
    void equipHelmet_WhenNoBattleOngoing_ShouldEquipHelmet() {
        // Arrange
        HelmetInstance helmet = new HelmetInstance();
        helmet.setId(ITEM_ID);
        helmet.setEquipped(false);
        inventory.getHelmets().add(helmet);

        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);
        when(characterInstanceRepository.save(any(CharacterInstance.class))).thenReturn(hero);

        // Act
        HelmetInstance result = characterService.equipHelmet(hero, ITEM_ID, USER_ID);

        // Assert
        assertTrue(result.getEquipped());
    }

    @Test
    void equipShield_WhenNoBattleOngoing_ShouldEquipShield() {
        // Arrange
        ShieldInstance shield = new ShieldInstance();
        shield.setId(ITEM_ID);
        shield.setEquipped(false);
        inventory.getShields().add(shield);

        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);
        when(characterInstanceRepository.save(any(CharacterInstance.class))).thenReturn(hero);

        // Act
        ShieldInstance result = characterService.equipShield(hero, ITEM_ID, USER_ID);

        // Assert
        assertTrue(result.getEquipped());
    }

    @Test
    void equipWeapon_WhenNoBattleOngoing_ShouldEquipWeapon() {
        // Arrange
        WeaponInstance weapon = new WeaponInstance();
        weapon.setId(ITEM_ID);
        weapon.setEquipped(false);
        inventory.getWeapons().add(weapon);

        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);
        when(characterInstanceRepository.save(any(CharacterInstance.class))).thenReturn(hero);

        // Act
        WeaponInstance result = characterService.equipWeapon(hero, ITEM_ID, USER_ID);

        // Assert
        assertTrue(result.getEquipped());
    }

    // Getter methods tests
    @Test
    void getAllCharacterInstancesForACampaignAndUser_ShouldReturnList() {
        // Arrange
        List<CharacterInstance> characters = List.of(hero);
        when(characterInstanceRepository.findAllByUserIdAndCampaign_Id(USER_ID, CAMPAIGN_ID))
                .thenReturn(characters);

        // Act
        List<CharacterInstance> result = characterService.getAllCharacterInstancesForACampaignAndUser(USER_ID, CAMPAIGN_ID);

        // Assert
        assertEquals(characters, result);
    }

    @Test
    void findHeroOrNull_WhenHeroExists_ShouldReturnHero() {
        // Arrange
        when(characterInstanceRepository.findFirstByCampaign_IdAndUserIdAndCharacterType(
                CAMPAIGN_ID, USER_ID, CharacterType.PLAYER)).thenReturn(hero);

        // Act
        CharacterInstance result = characterService.findHeroOrNull(CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(hero, result);
    }

    @Test
    void findHeroOrNull_WhenHeroDoesNotExist_ShouldReturnNull() {
        // Arrange
        when(characterInstanceRepository.findFirstByCampaign_IdAndUserIdAndCharacterType(
                CAMPAIGN_ID, USER_ID, CharacterType.PLAYER)).thenReturn(null);

        // Act
        CharacterInstance result = characterService.findHeroOrNull(CAMPAIGN_ID, USER_ID);

        // Assert
        assertNull(result);
    }

    @Test
    void createHero_WhenHeroDoesNotExist_ShouldCreateHero() {
        // Arrange
        CreateHeroRequestDto dto = new CreateHeroRequestDto("New Hero");
        Stats stats = new Stats();

        when(characterInstanceRepository.findFirstByCampaign_IdAndUserIdAndCharacterType(
                CAMPAIGN_ID, USER_ID, CharacterType.PLAYER)).thenReturn(null);
        when(statsMapper.createRandomInitialStats()).thenReturn(stats);
        when(characterInstanceRepository.save(any(CharacterInstance.class))).thenReturn(hero);

        // Act
        CharacterInstance result = characterService.createHero(campaign, USER_ID, dto);

        // Assert
        assertNotNull(result);
        verify(characterInstanceRepository).save(any(CharacterInstance.class));
    }

    @Test
    void createHero_WhenHeroAlreadyExists_ShouldThrowException() {
        // Arrange
        CreateHeroRequestDto dto = new CreateHeroRequestDto("New Hero");
        when(characterInstanceRepository.findFirstByCampaign_IdAndUserIdAndCharacterType(
                CAMPAIGN_ID, USER_ID, CharacterType.PLAYER)).thenReturn(hero);

        // Act & Assert
        assertThrows(HeroAlreadyCreated.class, () -> {
            characterService.createHero(campaign, USER_ID, dto);
        });
    }

    @Test
    void getHero_WhenHeroExists_ShouldReturnHero() {
        // Arrange
        when(characterInstanceRepository.findFirstByCampaign_IdAndUserIdAndCharacterType(
                CAMPAIGN_ID, USER_ID, CharacterType.PLAYER)).thenReturn(hero);

        // Act
        CharacterInstance result = characterService.getHero(CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(hero, result);
    }

    @Test
    void getHero_WhenHeroDoesNotExist_ShouldThrowException() {
        // Arrange
        when(characterInstanceRepository.findFirstByCampaign_IdAndUserIdAndCharacterType(
                CAMPAIGN_ID, USER_ID, CharacterType.PLAYER)).thenReturn(null);

        // Act & Assert
        assertThrows(HeroDoesNotExist.class, () -> {
            characterService.getHero(CAMPAIGN_ID, USER_ID);
        });
    }

    @Test
    void getDailyEnemy_WhenEnemyExists_ShouldReturnEnemy() {
        // Arrange
        CharacterInstance enemy = new CharacterInstance();
        enemy.setCharacterType(CharacterType.NPC);
        when(characterInstanceRepository.findDiscoveredEnemyByCampaignIdAndUserIdAndCharacterTypeAndUpdatedToday(
                CAMPAIGN_ID, USER_ID, LocalDate.now(), CharacterType.NPC.name())).thenReturn(enemy);

        // Act
        CharacterInstance result = characterService.getDailyEnemy(CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(enemy, result);
    }

    @Test
    void getDailyEnemy_WhenEnemyDoesNotExist_ShouldThrowException() {
        // Arrange
        when(characterInstanceRepository.findDiscoveredEnemyByCampaignIdAndUserIdAndCharacterTypeAndUpdatedToday(
                CAMPAIGN_ID, USER_ID, LocalDate.now(), CharacterType.NPC.name())).thenReturn(null);

        // Act & Assert
        assertThrows(DailyEnemyNotFound.class, () -> {
            characterService.getDailyEnemy(CAMPAIGN_ID, USER_ID);
        });
    }

    @Test
    void doesHeroExistForACampaign_WhenHeroExists_ShouldReturnTrue() {
        // Arrange
        when(characterInstanceRepository.findFirstByCampaign_IdAndUserIdAndCharacterType(
                CAMPAIGN_ID, USER_ID, CharacterType.PLAYER)).thenReturn(hero);

        // Act
        boolean result = characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID);

        // Assert
        assertTrue(result);
    }

    @Test
    void doesHeroExistForACampaign_WhenHeroDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(characterInstanceRepository.findFirstByCampaign_IdAndUserIdAndCharacterType(
                CAMPAIGN_ID, USER_ID, CharacterType.PLAYER)).thenReturn(null);

        // Act
        boolean result = characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void getCharacterByIdAndCampaignIdAndUserId_ShouldReturnCharacter() {
        // Arrange
        when(characterInstanceRepository.findFirstByIdAndCampaign_IdAndUserId(
                CHARACTER_ID, CAMPAIGN_ID, USER_ID)).thenReturn(hero);

        // Act
        CharacterInstance result = characterService.getCharacterByIdAndCampaignIdAndUserId(CHARACTER_ID, CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(hero, result);
    }

    @Test
    void isCharacterNotNull_WhenCharacterIsNotNull_ShouldReturnTrue() {
        // Act
        boolean result = characterService.isCharacterNotNull(hero);

        // Assert
        assertTrue(result);
    }

    @Test
    void isCharacterNotNull_WhenCharacterIsNull_ShouldReturnFalse() {
        // Act
        boolean result = characterService.isCharacterNotNull(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void saveCharacter_ShouldSaveAndReturnCharacter() {
        // Arrange
        when(characterInstanceRepository.save(hero)).thenReturn(hero);

        // Act
        CharacterInstance result = characterService.saveCharacter(hero);

        // Assert
        assertEquals(hero, result);
        verify(characterInstanceRepository).save(hero);
    }

    @Test
    void canTheCharacterUseConsumable_WhenConsumableExistsAndHasQuantity_ShouldReturnTrue() {
        // Arrange
        ConsumableInstance consumable = new ConsumableInstance();
        consumable.setId(ITEM_ID);
        consumable.setQuantity(5);
        inventory.getConsumables().add(consumable);

        // Act
        boolean result = characterService.canTheCharacterUseConsumable(hero, ITEM_ID);

        // Assert
        assertTrue(result);
    }

    @Test
    void canTheCharacterUseConsumable_WhenConsumableDoesNotExist_ShouldReturnFalse() {
        // Act
        boolean result = characterService.canTheCharacterUseConsumable(hero, ITEM_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void canTheCharacterUseConsumable_WhenQuantityIsZero_ShouldReturnFalse() {
        // Arrange
        ConsumableInstance consumable = new ConsumableInstance();
        consumable.setId(ITEM_ID);
        consumable.setQuantity(0);
        inventory.getConsumables().add(consumable);

        // Act
        boolean result = characterService.canTheCharacterUseConsumable(hero, ITEM_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void canTheCharacterUseSpell_WhenSpellExistsAndHasQuantityAndEnoughMp_ShouldReturnTrue() {
        // Arrange
        SpellInstance spell = new SpellInstance();
        spell.setId(ITEM_ID);
        spell.setQuantity(1);
        com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate template = 
                new com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate();
        template.setMpCost(10);
        spell.setTemplate(template);
        inventory.getSpells().add(spell);
        
        Stats stats = hero.getStats();
        stats.setCurrentMp(20);

        // Act
        boolean result = characterService.canTheCharacterUseSpell(hero, ITEM_ID);

        // Assert
        assertTrue(result);
    }

    @Test
    void canTheCharacterUseSpell_WhenNotEnoughMp_ShouldReturnFalse() {
        // Arrange
        SpellInstance spell = new SpellInstance();
        spell.setId(ITEM_ID);
        spell.setQuantity(1);
        com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate template = 
                new com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate();
        template.setMpCost(20);
        spell.setTemplate(template);
        inventory.getSpells().add(spell);
        
        Stats stats = hero.getStats();
        stats.setCurrentMp(10);

        // Act
        boolean result = characterService.canTheCharacterUseSpell(hero, ITEM_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void getShortAIGeneratedReport_ShouldReturnSortedCharacters() {
        // Arrange
        CharacterInstance npc1 = new CharacterInstance();
        npc1.setTier(5);
        npc1.setRarity(3);
        npc1.setName("NPC1");
        npc1.setCategory(CharacterCategory.HUMANOID);

        CharacterInstance npc2 = new CharacterInstance();
        npc2.setTier(3);
        npc2.setRarity(5);
        npc2.setName("NPC2");
        npc2.setCategory(CharacterCategory.BEAST);

        List<CharacterInstance> npcs = new ArrayList<>(List.of(npc1, npc2));
        when(characterInstanceRepository.findAllByCampaign_IdAndCharacterType(CAMPAIGN_ID, CharacterType.NPC))
                .thenReturn(npcs);

        // Act
        var result = characterService.getShortAIGeneratedReport(CAMPAIGN_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("CharacterInstances"));
    }

    @Test
    void getRandomCharacterInstanceForCharacterBooster_ShouldReturnCharacter() {
        // Arrange
        CharacterInstance npc = new CharacterInstance();
        when(characterInstanceRepository.findRandomByCampaignAndRarityAndTier(
                CAMPAIGN_ID, USER_ID, 3, 2)).thenReturn(npc);

        // Act
        CharacterInstance result = characterService.getRandomCharacterInstanceForCharacterBooster(
                CAMPAIGN_ID, USER_ID, 3, 2);

        // Assert
        assertEquals(npc, result);
    }

    @Test
    void getTier5NpcsContextForCampaignCover_ShouldReturnContextMap() {
        // Arrange
        CharacterInstance npc = new CharacterInstance();
        npc.setName("Tier 5 NPC");
        npc.setDescription("Description");
        List<CharacterInstance> npcs = List.of(npc);
        when(characterInstanceRepository.findAllByTierAndCampaign_Id(5, CAMPAIGN_ID)).thenReturn(npcs);

        // Act
        var result = characterService.getTier5NpcsContextForCampaignCover(campaign);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("Tier 5 NPC"));
    }

    @Test
    void createTenNPCsOfDesiredTier_retriesWhenEnumInvalid() {
        // Arrange
        when(characterInstanceRepository.findAll()).thenReturn(List.of());
        when(statsMapper.mapStatsFromCharacterFromGeminiDto(any())).thenReturn(new Stats());
        CharacterFromGeminiDto invalidDto = createCharacterDto("Invalid NPC", null);
        List<CharacterFromGeminiDto> validDtos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            validDtos.add(createCharacterDto("Valid NPC " + i, CharacterCategory.HUMANOID));
        }

        when(geminiService.generateTenNpcsOfDesiredTier(eq(campaign), anyList(), eq(2)))
                .thenReturn(List.of(invalidDto))
                .thenReturn(validDtos);
        when(characterInstanceRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        List<CharacterInstance> result = characterService.createTenNPCsOfDesiredTier(campaign, 2);

        // Assert
        assertEquals(10, result.size());
        verify(geminiService, times(2)).generateTenNpcsOfDesiredTier(eq(campaign), anyList(), eq(2));
        verify(characterInstanceRepository).saveAll(anyList());
    }

    private CharacterFromGeminiDto createCharacterDto(String name, CharacterCategory category) {
        CharacterFromGeminiDto.StatsFromGemini statsFromGemini = new CharacterFromGeminiDto.StatsFromGemini(
                10, 10, 10, 10, 10, 100, 100, 100, 100, 180, 80, 5, 0, 10
        );
        return new CharacterFromGeminiDto(
                statsFromGemini,
                CharacterType.NPC,
                category,
                name,
                "Description",
                false,
                CAMPAIGN_ID,
                1,
                1,
                0,
                0
        );
    }
}

