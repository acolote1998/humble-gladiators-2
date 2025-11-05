package com.github.acolote1998.humble_gladiators_2.core.service.integration;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.repository.CharacterInstanceRepository;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.BattleRepository;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.BattleCleanupService;
import com.github.acolote1998.humble_gladiators_2.core.service.BattleService;
import com.github.acolote1998.humble_gladiators_2.testutil.TestDataFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class BattleCleanupServiceIntegrationTest {

    @Autowired
    private BattleCleanupService battleCleanupService;

    @Autowired
    private BattleRepository battleRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private CharacterInstanceRepository characterInstanceRepository;

    @Autowired
    private BattleService battleService;

    @Autowired
    private EntityManager entityManager;

    private Campaign campaign;
    private String userId;
    private CharacterInstance hero;
    private CharacterInstance enemy;

    @BeforeEach
    void setup() {
        userId = "test-user";
        campaign = TestDataFactory.persistCampaign(campaignRepository, userId, "Test Campaign");
        hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        
        // Create NPC and make it discoverable for today
        CharacterInstance npc = TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, campaign, userId);
        npc.setDiscovered(true);
        enemy = characterInstanceRepository.save(npc);
    }

    @Test
    void consolidateOldBattles_verifiesDatabaseDeletionBasedOnDate() {
        // Create an old battle (yesterday)
        Battle oldBattle = createBattleForDate(LocalDate.now().minusDays(1));
        oldBattle.setOngoing(true);
        battleRepository.save(oldBattle);
        
        entityManager.flush();
        entityManager.clear();

        // Execute consolidation
        battleCleanupService.consolidateOldBattles();
        
        entityManager.flush();
        entityManager.clear();

        // Verify battle was consolidated (ongoing = false, teams cleared)
        Battle consolidated = battleRepository.findById(oldBattle.getId()).orElseThrow();
        assertThat(consolidated.isOngoing()).isFalse();
        assertThat(consolidated.getCurrentCharacterToPlay()).isNull();
        assertThat(consolidated.getTeamOne()).isEmpty();
        assertThat(consolidated.getTeamTwo()).isEmpty();
    }

    @Test
    void consolidateOldBattles_onlyAffectsOldBattles_notCurrentOnes() {
        // Create separate campaigns to avoid battle conflicts
        Campaign campaign1 = TestDataFactory.persistCampaign(campaignRepository, userId, "Campaign 1");
        Campaign campaign2 = TestDataFactory.persistCampaign(campaignRepository, userId, "Campaign 2");
        
        TestDataFactory.createHero(characterService, campaign1, userId, "Hero1");
        CharacterInstance enemy1 = TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, campaign1, userId);
        enemy1.setDiscovered(true);
        characterInstanceRepository.save(enemy1);
        
        TestDataFactory.createHero(characterService, campaign2, userId, "Hero2");
        CharacterInstance enemy2 = TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, campaign2, userId);
        enemy2.setDiscovered(true);
        characterInstanceRepository.save(enemy2);
        
        // Create an old battle (yesterday)
        Battle oldBattle = createBattleForDateAndCampaign(LocalDate.now().minusDays(1), campaign1);
        oldBattle.setOngoing(true);
        battleRepository.save(oldBattle);
        
        // Create a current battle (today)
        Battle currentBattle = createBattleForDateAndCampaign(LocalDate.now(), campaign2);
        currentBattle.setOngoing(true);
        battleRepository.save(currentBattle);
        
        entityManager.flush();
        entityManager.clear();

        // Execute consolidation
        battleCleanupService.consolidateOldBattles();
        
        entityManager.flush();
        entityManager.clear();

        // Verify old battle was consolidated
        Battle consolidatedOld = battleRepository.findById(oldBattle.getId()).orElseThrow();
        assertThat(consolidatedOld.isOngoing()).isFalse();
        assertThat(consolidatedOld.getTeamOne()).isEmpty();
        
        // Verify current battle was NOT consolidated
        Battle current = battleRepository.findById(currentBattle.getId()).orElseThrow();
        assertThat(current.isOngoing()).isTrue();
        assertThat(current.getTeamOne()).isNotEmpty();
    }

    @Test
    void consolidateOldBattles_withEmptyBattleList_handlesGracefully() {
        // No battles created
        
        // Execute consolidation - should not throw exception
        battleCleanupService.consolidateOldBattles();
        
        // Verify no battles exist
        List<Battle> allBattles = battleRepository.findAll();
        assertThat(allBattles).isEmpty();
    }

    @Test
    void consolidateBattle_preservesCharactersCorrectly() {
        // Create an old battle with characters
        Battle battle = createBattleForDate(LocalDate.now().minusDays(1));
        battle.setOngoing(true);
        
        // Get character IDs before consolidation
        Long heroId = hero.getId();
        Long enemyId = enemy.getId();
        
        entityManager.flush();
        entityManager.clear();

        // Execute consolidation
        battleCleanupService.consolidateOldBattles();
        
        entityManager.flush();
        entityManager.clear();

        // Verify characters still exist and are not deleted
        assertThat(characterInstanceRepository.findById(heroId)).isPresent();
        assertThat(characterInstanceRepository.findById(enemyId)).isPresent();
        
        // Verify characters were fully recovered (HP/MP restored)
        CharacterInstance recoveredHero = characterInstanceRepository.findById(heroId).orElseThrow();
        CharacterInstance recoveredEnemy = characterInstanceRepository.findById(enemyId).orElseThrow();
        assertThat(recoveredHero.getStats().getCurrentHp()).isEqualTo(recoveredHero.getStats().getMaxHp());
        assertThat(recoveredHero.getStats().getCurrentMp()).isEqualTo(recoveredHero.getStats().getMaxMp());
        assertThat(recoveredEnemy.getStats().getCurrentHp()).isEqualTo(recoveredEnemy.getStats().getMaxHp());
        assertThat(recoveredEnemy.getStats().getCurrentMp()).isEqualTo(recoveredEnemy.getStats().getMaxMp());
    }

    @Test
    void consolidateBattle_withEmptyTeams_handlesGracefully() {
        // Create an old battle with empty winning/losing teams (but teamOne and teamTwo will have characters)
        // The consolidation logic requires teamOne and teamTwo to have at least one character
        // to populate winning/losing teams, so we create a battle with proper teams first
        Battle battle = createBattleForDate(LocalDate.now().minusDays(1));
        battle.setOngoing(true);
        battle.setWinningTeam(new ArrayList<>());
        battle.setLosingTeam(new ArrayList<>());
        battle = battleRepository.save(battle);
        
        // Ensure the date update is persisted
        entityManager.flush();
        entityManager.clear();
        
        // Reload battle to ensure all changes are persisted
        battle = battleRepository.findById(battle.getId()).orElseThrow();
        assertThat(battle.isOngoing()).isTrue(); // Verify it's still ongoing before consolidation

        // Execute consolidation - will set winning/losing teams from teamOne/teamTwo
        battleCleanupService.consolidateOldBattles();
        
        entityManager.flush();
        entityManager.clear();

        // Verify battle was consolidated
        Battle consolidated = battleRepository.findById(battle.getId()).orElseThrow();
        assertThat(consolidated.isOngoing()).isFalse();
        assertThat(consolidated.getCurrentCharacterToPlay()).isNull();
        // Teams should be cleared after consolidation
        assertThat(consolidated.getTeamOne()).isEmpty();
        assertThat(consolidated.getTeamTwo()).isEmpty();
    }

    @Test
    void consolidateBattle_boundaryCondition_exactlyOneDayOld() {
        // Create battle exactly 1 day old (yesterday)
        Battle battle = createBattleForDate(LocalDate.now().minusDays(1));
        battle.setOngoing(true);
        battleRepository.save(battle);
        
        entityManager.flush();
        entityManager.clear();

        // Execute consolidation
        battleCleanupService.consolidateOldBattles();
        
        entityManager.flush();
        entityManager.clear();

        // Verify battle was consolidated
        Battle consolidated = battleRepository.findById(battle.getId()).orElseThrow();
        assertThat(consolidated.isOngoing()).isFalse();
    }

    @Test
    void consolidateBattle_setsWinningAndLosingTeams_whenEmpty() {
        // Create an old battle with teams but empty winning/losing teams
        Battle battle = createBattleForDate(LocalDate.now().minusDays(1));
        battle.setOngoing(true);
        battle.setWinningTeam(new ArrayList<>());
        battle.setLosingTeam(new ArrayList<>());
        battle = battleRepository.save(battle);
        
        // Ensure the date update is persisted
        entityManager.flush();
        entityManager.clear();
        
        // Reload battle to ensure all changes are persisted
        battle = battleRepository.findById(battle.getId()).orElseThrow();
        // Verify teams have characters before consolidation
        assertThat(battle.getTeamOne()).isNotEmpty();
        assertThat(battle.getTeamTwo()).isNotEmpty();
        assertThat(battle.getWinningTeam()).isEmpty();
        assertThat(battle.getLosingTeam()).isEmpty();

        // Execute consolidation
        battleCleanupService.consolidateOldBattles();
        
        entityManager.flush();
        entityManager.clear();

        // Verify winning and losing teams were set
        Battle consolidated = battleRepository.findById(battle.getId()).orElseThrow();
        assertThat(consolidated.getWinningTeam()).isNotEmpty();
        assertThat(consolidated.getLosingTeam()).isNotEmpty();
        // Winning team should have teamTwo's first character (enemy)
        assertThat(consolidated.getWinningTeam().getFirst().getId()).isEqualTo(enemy.getId());
        // Losing team should have teamOne's first character (hero)
        assertThat(consolidated.getLosingTeam().getFirst().getId()).isEqualTo(hero.getId());
    }

    @Test
    void consolidateBattle_clearsCurrentCharacterToPlay() {
        // Create a new campaign and characters for this test to avoid conflicts
        Campaign testCampaign = TestDataFactory.persistCampaign(campaignRepository, userId, "Test Campaign 2");
        CharacterInstance testHero = TestDataFactory.createHero(characterService, testCampaign, userId, "Hero2");
        CharacterInstance testEnemy = TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, testCampaign, userId);
        testEnemy.setDiscovered(true);
        testEnemy = characterInstanceRepository.save(testEnemy);
        
        // Create an old battle with a current character to play
        Battle battle = createBattleForDateAndCampaign(LocalDate.now().minusDays(1), testCampaign);
        battle.setOngoing(true);
        battle.setCurrentCharacterToPlay(testHero);
        battleRepository.save(battle);
        
        entityManager.flush();
        entityManager.clear();

        // Execute consolidation
        battleCleanupService.consolidateOldBattles();
        
        entityManager.flush();
        entityManager.clear();

        // Verify currentCharacterToPlay was cleared
        Battle consolidated = battleRepository.findById(battle.getId()).orElseThrow();
        assertThat(consolidated.getCurrentCharacterToPlay()).isNull();
    }

    @Test
    void consolidateBattle_clearsTeamOneAndTeamTwo() {
        // Create an old battle with teams
        Battle battle = createBattleForDate(LocalDate.now().minusDays(1));
        battle.setOngoing(true);
        battleRepository.save(battle);
        
        // Verify teams have characters before consolidation
        assertThat(battle.getTeamOne()).isNotEmpty();
        assertThat(battle.getTeamTwo()).isNotEmpty();
        
        entityManager.flush();
        entityManager.clear();

        // Execute consolidation
        battleCleanupService.consolidateOldBattles();
        
        entityManager.flush();
        entityManager.clear();

        // Verify teams were cleared
        Battle consolidated = battleRepository.findById(battle.getId()).orElseThrow();
        assertThat(consolidated.getTeamOne()).isEmpty();
        assertThat(consolidated.getTeamTwo()).isEmpty();
    }

    @Test
    void consolidateOldBattles_handlesMultipleOldBattles() {
        // Create separate campaigns for each battle to avoid conflicts
        Campaign campaign1 = TestDataFactory.persistCampaign(campaignRepository, userId, "Campaign 1");
        Campaign campaign2 = TestDataFactory.persistCampaign(campaignRepository, userId, "Campaign 2");
        Campaign campaign3 = TestDataFactory.persistCampaign(campaignRepository, userId, "Campaign 3");
        
        // Setup heroes and enemies for each campaign
        TestDataFactory.createHero(characterService, campaign1, userId, "Hero1");
        CharacterInstance enemy1 = TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, campaign1, userId);
        enemy1.setDiscovered(true);
        characterInstanceRepository.save(enemy1);
        
        TestDataFactory.createHero(characterService, campaign2, userId, "Hero2");
        CharacterInstance enemy2 = TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, campaign2, userId);
        enemy2.setDiscovered(true);
        characterInstanceRepository.save(enemy2);
        
        TestDataFactory.createHero(characterService, campaign3, userId, "Hero3");
        CharacterInstance enemy3 = TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, campaign3, userId);
        enemy3.setDiscovered(true);
        characterInstanceRepository.save(enemy3);
        
        // Create multiple old battles
        Battle battle1 = createBattleForDateAndCampaign(LocalDate.now().minusDays(2), campaign1);
        battle1.setOngoing(true);
        battleRepository.save(battle1);
        
        Battle battle2 = createBattleForDateAndCampaign(LocalDate.now().minusDays(3), campaign2);
        battle2.setOngoing(true);
        battleRepository.save(battle2);
        
        Battle battle3 = createBattleForDateAndCampaign(LocalDate.now().minusDays(5), campaign3);
        battle3.setOngoing(true);
        battleRepository.save(battle3);
        
        entityManager.flush();
        entityManager.clear();

        // Execute consolidation
        battleCleanupService.consolidateOldBattles();
        
        entityManager.flush();
        entityManager.clear();

        // Verify all battles were consolidated
        Battle consolidated1 = battleRepository.findById(battle1.getId()).orElseThrow();
        Battle consolidated2 = battleRepository.findById(battle2.getId()).orElseThrow();
        Battle consolidated3 = battleRepository.findById(battle3.getId()).orElseThrow();
        
        assertThat(consolidated1.isOngoing()).isFalse();
        assertThat(consolidated2.isOngoing()).isFalse();
        assertThat(consolidated3.isOngoing()).isFalse();
        
        assertThat(consolidated1.getTeamOne()).isEmpty();
        assertThat(consolidated2.getTeamOne()).isEmpty();
        assertThat(consolidated3.getTeamOne()).isEmpty();
    }

    /**
     * Helper method to create a battle with a specific creation date.
     * Note: This sets the createdAt date using native SQL since JPA doesn't allow
     * setting @CreatedDate fields directly.
     */
    private Battle createBattleForDate(LocalDate date) {
        return createBattleForDateAndCampaign(date, campaign);
    }

    /**
     * Helper method to create a battle with a specific creation date and campaign.
     * Note: This sets the createdAt date using native SQL since JPA doesn't allow
     * setting @CreatedDate fields directly.
     */
    private Battle createBattleForDateAndCampaign(LocalDate date, Campaign targetCampaign) {
        Battle battle = battleService.createNewBattle(targetCampaign.getId(), userId);
        Long battleId = battle.getId();
        
        // Set createdAt to the specified date using native query
        entityManager.flush();
        entityManager.createNativeQuery(
                "UPDATE battle SET created_at = :date WHERE id = :id")
                .setParameter("date", date.atStartOfDay())
                .setParameter("id", battleId)
                .executeUpdate();
        
        // Flush and clear to ensure the update is persisted
        entityManager.flush();
        entityManager.clear();
        
        // Reload battle to ensure the date change is reflected
        return battleRepository.findById(battleId).orElseThrow();
    }
}

