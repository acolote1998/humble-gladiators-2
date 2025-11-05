package com.github.acolote1998.humble_gladiators_2.core.service.integration;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.repository.CharacterInstanceRepository;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.dto.TurnRequestDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidBattle;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidTurn;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.model.BattleReward;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Turn;
import com.github.acolote1998.humble_gladiators_2.core.repository.BattleRepository;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.BattleService;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.repository.*;
import com.github.acolote1998.humble_gladiators_2.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class BattleServiceIntegrationTest {

    @Autowired
    private BattleService battleService;

    @Autowired
    private BattleRepository battleRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private CharacterInstanceRepository characterInstanceRepository;

    @Autowired
    private WeaponTemplateRepository weaponTemplateRepository;

    @Autowired
    private ArmorTemplateRepository armorTemplateRepository;

    @Autowired
    private BootsTemplateRepository bootsTemplateRepository;

    @Autowired
    private HelmetTemplateRepository helmetTemplateRepository;

    @Autowired
    private ShieldTemplateRepository shieldTemplateRepository;

    @Autowired
    private SpellTemplateRepository spellTemplateRepository;

    @Autowired
    private ConsumableTemplateRepository consumableTemplateRepository;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private GeminiService geminiService;

    private String userId;
    private Campaign campaign;
    private CharacterInstance hero;
    private CharacterInstance enemy;

    @BeforeEach
    void setup() {
        userId = "user-test";
        campaign = TestDataFactory.persistCampaign(campaignRepository, userId, "Test Campaign");
        hero = TestDataFactory.createHero(characterService, campaign, userId, "Hero");
        
        // Create NPC and make it discoverable for today (daily enemy)
        // First create it, then update discovered flag so updatedAt gets refreshed
        CharacterInstance npc = TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, campaign, userId);
        npc.setDiscovered(true);
        // Save again to trigger @UpdateTimestamp annotation
        npc = characterInstanceRepository.save(npc);
        
        TestDataFactory.createAllTestItemTemplates(entityManager, campaign, userId,
                weaponTemplateRepository, armorTemplateRepository, bootsTemplateRepository,
                helmetTemplateRepository, shieldTemplateRepository, spellTemplateRepository,
                consumableTemplateRepository);

        // Get the daily enemy - it should be available now (updatedAt should be today)
        enemy = characterService.getDailyEnemy(campaign.getId(), userId);
    }

    @Test
    void createNewBattle_verifiesBattleTeamsAndTurnsArePersisted() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);

        assertThat(battle.getId()).isNotNull();
        assertThat(battle.getCampaign().getId()).isEqualTo(campaign.getId());
        assertThat(battle.getUserId()).isEqualTo(userId);
        assertThat(battle.isOngoing()).isTrue();
        assertThat(battle.getTeamOne()).hasSize(1);
        assertThat(battle.getTeamTwo()).hasSize(1);
        assertThat(battle.getTeamOne().get(0).getId()).isEqualTo(hero.getId());
        assertThat(battle.getTeamTwo().get(0).getId()).isEqualTo(enemy.getId());
        assertThat(battle.getTurns()).isEmpty();
        assertThat(battle.getStartingTeamOne()).hasSize(1);
        assertThat(battle.getStartingTeamTwo()).hasSize(1);
        assertThat(battle.getCurrentCharacterToPlay()).isNotNull();

        // Verify persistence
        Battle saved = battleRepository.findById(battle.getId()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getTeamOne()).hasSize(1);
        assertThat(saved.getTeamTwo()).hasSize(1);
    }

    @Test
    void createNewBattle_withNonExistentCampaignId_throwsException() {
        assertThatThrownBy(() -> battleService.createNewBattle(99999L, userId))
                .isInstanceOf(Exception.class);
    }

    @Test
    void performPhysicalAttack_verifiesTurnIsCreatedAndPersisted() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);
        CharacterInstance performer = battle.getCurrentCharacterToPlay();
        CharacterInstance target = battle.getTeamOne().contains(performer) 
                ? battle.getTeamTwo().get(0) 
                : battle.getTeamOne().get(0);

        TurnRequestDto turnRequest = new TurnRequestDto(
                performer.getId(),
                target.getId(),
                ActionType.PHYSICAL_ATTACK,
                null
        );

        Turn turn = battleService.performPhysicalAttack(campaign.getId(), userId, battle.getId(), turnRequest);

        assertThat(turn).isNotNull();
        assertThat(turn.getPerformingCharacter().getId()).isEqualTo(performer.getId());
        assertThat(turn.getTargetCharacter().getId()).isEqualTo(target.getId());
        assertThat(turn.getAction()).isNotNull();
        assertThat(turn.getAction().getActionType()).isEqualTo(ActionType.PHYSICAL_ATTACK);

        // Verify persistence - reload battle to get persisted turn with ID
        Battle updated = battleRepository.findById(battle.getId()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getTurns()).hasSize(1);
        Turn persistedTurn = updated.getTurns().get(0);
        assertThat(persistedTurn.getId()).isNotNull();
        assertThat(persistedTurn.getPerformingCharacter().getId()).isEqualTo(performer.getId());
        assertThat(updated.getCurrentCharacterToPlay()).isNotNull();
    }

    @Test
    void getUpdatedBattle_verifiesBattleStateUpdatesPersist() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);
        assertThat(battle.isOngoing()).isTrue();

        // Perform a turn to update battle state
        CharacterInstance performer = battle.getCurrentCharacterToPlay();
        CharacterInstance target = battle.getTeamOne().contains(performer) 
                ? battle.getTeamTwo().get(0) 
                : battle.getTeamOne().get(0);

        TurnRequestDto turnRequest = new TurnRequestDto(
                performer.getId(),
                target.getId(),
                ActionType.PHYSICAL_ATTACK,
                null
        );
        battleService.performPhysicalAttack(campaign.getId(), userId, battle.getId(), turnRequest);

        Battle updated = battleService.getBattleByIdAndCampaignIdAndUserId(battle.getId(), campaign.getId(), userId);
        assertThat(updated).isNotNull();
        assertThat(updated.getTurns()).hasSize(1);
        assertThat(updated.getCurrentCharacterToPlay()).isNotNull();
    }

    @Test
    void getUpdatedBattle_withBattleThatHasNoTurnsYet() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);
        
        Battle updated = battleService.getBattleByIdAndCampaignIdAndUserId(battle.getId(), campaign.getId(), userId);
        
        assertThat(updated).isNotNull();
        assertThat(updated.getTurns()).isEmpty();
        assertThat(updated.getCurrentCharacterToPlay()).isNotNull();
    }

    @Test
    void getRewardForBattle_verifiesRewardCreationAndPersistence() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);
        
        // End the battle by making one character die
        CharacterInstance hero = battle.getTeamOne().get(0);
        hero.getStats().setCurrentHp(0);
        characterService.saveCharacter(hero);
        
        battle.getWinningTeam().add(battle.getTeamTwo().get(0));
        battle.getLosingTeam().add(battle.getTeamOne().get(0));
        battle.setOngoing(false);
        battleRepository.save(battle);

        BattleReward reward = battleService.getRewardForBattle(battle);

        assertThat(reward).isNotNull();
        assertThat(reward.getBattleResult()).isNotNull();

        // Verify persistence - reload battle to get persisted reward with ID
        // The reward ID might not be set until transaction flushes, so we reload
        Battle saved = battleRepository.findById(battle.getId()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getReward()).isNotNull();
        assertThat(saved.getReward().getId()).isNotNull();
        assertThat(saved.getReward().getBattleResult()).isEqualTo(reward.getBattleResult());
    }

    @Test
    void battleStateTransitions_createdToOngoing() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);
        
        assertThat(battle.isOngoing()).isTrue();
        assertThat(battle.getWinningTeam()).isEmpty();
        assertThat(battle.getLosingTeam()).isEmpty();
    }

    @Test
    void battleStateTransitions_ongoingToFinished() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);
        
        // End the battle
        CharacterInstance hero = battle.getTeamOne().get(0);
        hero.getStats().setCurrentHp(0);
        characterService.saveCharacter(hero);
        
        battle.getWinningTeam().add(battle.getTeamTwo().get(0));
        battle.getLosingTeam().add(battle.getTeamOne().get(0));
        battle.setOngoing(false);
        battleRepository.save(battle);

        Battle saved = battleRepository.findById(battle.getId()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.isOngoing()).isFalse();
        assertThat(saved.getWinningTeam()).hasSize(1);
        assertThat(saved.getLosingTeam()).hasSize(1);
    }

    @Test
    void stateConsistency_ongoingFalseWhenWinnersExist() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);
        
        battle.getWinningTeam().add(battle.getTeamTwo().get(0));
        battle.getLosingTeam().add(battle.getTeamOne().get(0));
        battle.setOngoing(false);
        battleRepository.save(battle);

        Battle saved = battleRepository.findById(battle.getId()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.isOngoing()).isFalse();
        assertThat(saved.getWinningTeam()).isNotEmpty();
        assertThat(saved.getLosingTeam()).isNotEmpty();
    }

    @Test
    void currentCharacterToPlay_updatesCorrectlyDuringTurns() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);
        CharacterInstance initialCharacter = battle.getCurrentCharacterToPlay();
        
        CharacterInstance performer = initialCharacter;
        CharacterInstance target = battle.getTeamOne().contains(performer) 
                ? battle.getTeamTwo().get(0) 
                : battle.getTeamOne().get(0);

        TurnRequestDto turnRequest = new TurnRequestDto(
                performer.getId(),
                target.getId(),
                ActionType.PHYSICAL_ATTACK,
                null
        );
        battleService.performPhysicalAttack(campaign.getId(), userId, battle.getId(), turnRequest);

        Battle updated = battleRepository.findById(battle.getId()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getCurrentCharacterToPlay()).isNotNull();
        // The current character should have changed (turn alternates)
        assertThat(updated.getCurrentCharacterToPlay().getId()).isNotEqualTo(initialCharacter.getId());
    }

    @Test
    void battleCreation_persistsAllRelationships() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);

        assertThat(battle.getCampaign()).isNotNull();
        assertThat(battle.getCampaign().getId()).isEqualTo(campaign.getId());
        assertThat(battle.getTeamOne()).hasSize(1);
        assertThat(battle.getTeamTwo()).hasSize(1);
        assertThat(battle.getTurns()).isEmpty();
        assertThat(battle.getStartingTeamOne()).hasSize(1);
        assertThat(battle.getStartingTeamTwo()).hasSize(1);
        assertThat(battle.getCurrentCharacterToPlay()).isNotNull();

        // Verify relationships are persisted
        Battle saved = battleRepository.findById(battle.getId()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getCampaign().getId()).isEqualTo(campaign.getId());
        assertThat(saved.getTeamOne()).hasSize(1);
        assertThat(saved.getStartingTeamOne()).hasSize(1);
    }

    @Test
    void performPhysicalAttack_withInvalidTurn_throwsInvalidTurn() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);
        
        // Try to use wrong character (not current character's turn)
        CharacterInstance wrongPerformer = battle.getTeamOne().contains(battle.getCurrentCharacterToPlay())
                ? battle.getTeamTwo().get(0)
                : battle.getTeamOne().get(0);
        CharacterInstance target = battle.getTeamOne().get(0);

        TurnRequestDto turnRequest = new TurnRequestDto(
                wrongPerformer.getId(),
                target.getId(),
                ActionType.PHYSICAL_ATTACK,
                null
        );

        assertThatThrownBy(() -> battleService.performPhysicalAttack(campaign.getId(), userId, battle.getId(), turnRequest))
                .isInstanceOf(InvalidTurn.class);
    }

    @Test
    void getRewardForBattle_whenBattleStillActive_throwsInvalidBattle() {
        Battle battle = battleService.createNewBattle(campaign.getId(), userId);
        assertThat(battle.isOngoing()).isTrue();

        assertThatThrownBy(() -> battleService.getRewardForBattle(battle))
                .isInstanceOf(InvalidBattle.class);
    }
}

