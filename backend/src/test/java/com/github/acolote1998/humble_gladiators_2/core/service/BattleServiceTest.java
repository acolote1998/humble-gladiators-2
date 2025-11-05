package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.characters.exception.DailyEnemyNotFound;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidBattle;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.BattleRepository;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleServiceTest {

    @Mock
    private CharacterService characterService;

    @Mock
    private BattleRepository battleRepository;

    @Mock
    private BattleUtil battleUtil;

    @InjectMocks
    private BattleService battleService;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;
    private static final Long BATTLE_ID = 100L;

    private Campaign campaign;
    private CharacterInstance hero;
    private CharacterInstance enemy;
    private Battle battle;

    @BeforeEach
    void setUp() throws Exception {
        // Set @Value fields using reflection
        ReflectionTestUtils.setField(battleService, "UNLIMITED_BATTLES_ALLOWED", true);
        ReflectionTestUtils.setField(battleService, "NPC_ITEM_DROP_CHANCE", 50);

        campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        campaign.setUserId(USER_ID);

        hero = new CharacterInstance();
        hero.setId(1L);
        hero.setName("Hero");
        hero.setCharacterType(CharacterType.PLAYER);
        hero.setCampaign(campaign);

        enemy = new CharacterInstance();
        enemy.setId(2L);
        enemy.setName("Enemy");
        enemy.setCharacterType(CharacterType.NPC);
        enemy.setCampaign(campaign);

        battle = new Battle();
        battle.setId(BATTLE_ID);
        battle.setCampaign(campaign);
        battle.setUserId(USER_ID);
        battle.setOngoing(true);
        battle.setCreatedAt(LocalDateTime.now());
        battle.setTurns(new ArrayList<>());
        battle.setTeamOne(new ArrayList<>());
        battle.setTeamTwo(new ArrayList<>());
        battle.setWinningTeam(new ArrayList<>());
        battle.setLosingTeam(new ArrayList<>());
        battle.setStartingTeamOne(new ArrayList<>());
        battle.setStartingTeamTwo(new ArrayList<>());
    }

    @Test
    void doesCharacterDropThisItem_returnsRandomBooleanValue() {
        // Act
        Boolean result = battleService.doesCharacterDropThisItem();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    @Test
    void isBattleNotNull_returnsTrue_whenBattleIsNotNull() {
        // Act
        boolean result = battleService.isBattleNotNull(battle);

        // Assert
        assertTrue(result);
    }

    @Test
    void isBattleNotNull_returnsFalse_whenBattleIsNull() {
        // Act
        boolean result = battleService.isBattleNotNull(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void isBattleAvailableForToday_returnsTrue_whenAllConditionsMet() {
        // Arrange
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(true);
        when(characterService.getDailyEnemy(CAMPAIGN_ID, USER_ID)).thenReturn(enemy);
        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act
        boolean result = battleService.isBattleAvailableForToday(CAMPAIGN_ID, USER_ID);

        // Assert
        assertTrue(result);
    }

    @Test
    void isBattleAvailableForToday_returnsFalse_whenHeroDoesNotExist() {
        // Arrange
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act
        boolean result = battleService.isBattleAvailableForToday(CAMPAIGN_ID, USER_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void isBattleAvailableForToday_returnsFalse_whenDailyEnemyNotFound() {
        // Arrange
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(true);
        when(characterService.getDailyEnemy(CAMPAIGN_ID, USER_ID)).thenThrow(new DailyEnemyNotFound("No enemy"));

        // Act
        boolean result = battleService.isBattleAvailableForToday(CAMPAIGN_ID, USER_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void isBattleAvailableForToday_returnsFalse_whenBattleOngoing() {
        // Arrange
        when(characterService.doesHeroExistForACampaign(CAMPAIGN_ID, USER_ID)).thenReturn(true);
        when(characterService.getDailyEnemy(CAMPAIGN_ID, USER_ID)).thenReturn(enemy);
        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(true);

        // Act
        boolean result = battleService.isBattleAvailableForToday(CAMPAIGN_ID, USER_ID);

        // Assert
        assertFalse(result);
    }

    @Test
    void getAnyBattleForTodayByCampaignAndUserId_returnsBattle_whenBattleExists() {
        // Arrange
        when(battleRepository.findAnyByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(battle);

        // Act
        Battle result = battleService.getAnyBattleForTodayByCampaignAndUserId(CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(battle, result);
    }

    @Test
    void getAnyBattleForTodayByCampaignAndUserId_throwsInvalidBattle_whenBattleDoesNotExist() {
        // Arrange
        when(battleRepository.findAnyByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidBattle.class, () -> {
            battleService.getAnyBattleForTodayByCampaignAndUserId(CAMPAIGN_ID, USER_ID);
        });
    }

    @Test
    void getAllWonBattlesForCampaignHero_returnsListOfBattles() {
        // Arrange
        List<Battle> wonBattles = List.of(battle);
        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        when(battleRepository.findAllByWinningTeamContains(anyList())).thenReturn(wonBattles);

        // Act
        List<Battle> result = battleService.getAllWonBattlesForCampaignHero(CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(wonBattles, result);
    }

    @Test
    void getAllLostBattlesForACharacter_returnsListOfBattles() {
        // Arrange
        List<Battle> lostBattles = List.of(battle);
        when(characterService.getHero(CAMPAIGN_ID, USER_ID)).thenReturn(hero);
        when(battleRepository.findAllByLosingTeamContains(anyList())).thenReturn(lostBattles);

        // Act
        List<Battle> result = battleService.getAllLostBattlesForACharacter(CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(lostBattles, result);
    }

    @Test
    void getUpdatedBattle_returnsBattle() {
        // Arrange
        // Set up Stats and Inventory for characters to avoid null pointer exceptions
        Stats heroStats = new Stats();
        heroStats.setSpeed(10);
        heroStats.setCurrentHp(100);
        heroStats.setMaxHp(100);
        hero.setStats(heroStats);
        Inventory heroInventory = new Inventory();
        heroInventory.setArmors(new ArrayList<>());
        heroInventory.setBoots(new ArrayList<>());
        heroInventory.setConsumables(new ArrayList<>());
        heroInventory.setHelmets(new ArrayList<>());
        heroInventory.setShields(new ArrayList<>());
        heroInventory.setSpells(new ArrayList<>());
        heroInventory.setWeapons(new ArrayList<>());
        hero.setInventory(heroInventory);

        Stats enemyStats = new Stats();
        enemyStats.setSpeed(8);
        enemyStats.setCurrentHp(100);
        enemyStats.setMaxHp(100);
        enemy.setStats(enemyStats);
        Inventory enemyInventory = new Inventory();
        enemyInventory.setArmors(new ArrayList<>());
        enemyInventory.setBoots(new ArrayList<>());
        enemyInventory.setConsumables(new ArrayList<>());
        enemyInventory.setHelmets(new ArrayList<>());
        enemyInventory.setShields(new ArrayList<>());
        enemyInventory.setSpells(new ArrayList<>());
        enemyInventory.setWeapons(new ArrayList<>());
        enemy.setInventory(enemyInventory);

        battle.getTeamOne().add(hero);
        battle.getTeamTwo().add(enemy);
        when(battleRepository.save(any(Battle.class))).thenReturn(battle);

        // Act
        Battle result = battleService.getUpdatedBattle(battle);

        // Assert
        assertNotNull(result);
        verify(battleRepository).save(any(Battle.class));
    }
}

