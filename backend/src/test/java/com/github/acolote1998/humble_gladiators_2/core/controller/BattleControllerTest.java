package com.github.acolote1998.humble_gladiators_2.core.controller;

import com.github.acolote1998.humble_gladiators_2.core.dto.TurnRequestDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidBattle;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidTurn;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.model.BattleReward;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Turn;
import com.github.acolote1998.humble_gladiators_2.core.service.BattleService;
import com.github.acolote1998.humble_gladiators_2.core.service.BattleUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BattleController.class)
class BattleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BattleService battleService;

    @MockitoBean
    private BattleUtil battleUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;
    private static final Long BATTLE_ID = 100L;

    private Jwt jwt;

    @BeforeEach
    void setUp() {
        jwt = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .claim("sub", USER_ID)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
    }

    @Test
    @WithMockUser
    void checkIfPossibleToStartABattleToday_ShouldReturnBoolean() throws Exception {
        // Arrange
        when(battleService.isBattleAvailableForToday(CAMPAIGN_ID, USER_ID)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/battle/check-availability", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser
    void checkIfThereIsAnOngoingBattleForToday_ShouldReturnBoolean() throws Exception {
        // Arrange
        when(battleService.getBattleUtil()).thenReturn(battleUtil);
        when(battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/battle/check-ongoing", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @WithMockUser
    void getOnGoingBattleForToday_ShouldReturnBattleResponse() throws Exception {
        // Arrange
        Battle battle = createTestBattle();
        when(battleService.getBattleUtil()).thenReturn(battleUtil);
        when(battleUtil.getOnGoingBattleForTodayByCampaignAndUserId(CAMPAIGN_ID, USER_ID))
                .thenReturn(battle);
        when(battleService.getUpdatedBattle(battle)).thenReturn(battle);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/battle/ongoing", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void getRewardsForTodaysFinishedBattle_ShouldReturnRewards() throws Exception {
        // Arrange
        Battle battle = createTestBattle();
        BattleReward reward = new BattleReward();
        when(battleService.getBattleUtil()).thenReturn(battleUtil);
        when(battleUtil.getFinishedBattleForTodayByCampaignAndUserId(CAMPAIGN_ID, USER_ID))
                .thenReturn(battle);
        when(battleService.getUpdatedBattle(battle)).thenReturn(battle);
        when(battleService.getRewardForBattle(battle)).thenReturn(reward);
        doNothing().when(battleService).fullyRecoverBothTeams(battle);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/battle/get-rewards", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void getAnyBattleForToday_ShouldReturnBattleResponse() throws Exception {
        // Arrange
        Battle battle = createTestBattle();
        when(battleService.getAnyBattleForTodayByCampaignAndUserId(CAMPAIGN_ID, USER_ID)).thenReturn(battle);
        when(battleService.getUpdatedBattle(battle)).thenReturn(battle);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/battle", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void getAllWonBattlesForAHero_ShouldReturnListOfBattles() throws Exception {
        // Arrange
        List<Battle> wonBattles = Collections.emptyList();
        when(battleService.getAllWonBattlesForCampaignHero(CAMPAIGN_ID, USER_ID)).thenReturn(wonBattles);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/battle/won", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void getAllLostBattlesForAHero_ShouldReturnListOfBattles() throws Exception {
        // Arrange
        List<Battle> lostBattles = Collections.emptyList();
        when(battleService.getAllLostBattlesForACharacter(CAMPAIGN_ID, USER_ID)).thenReturn(lostBattles);

        // Act & Assert
        mockMvc.perform(get("/api/campaign/{campaignId}/battle/lost", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void triggerNpcTurnInBattleOfToday_ShouldReturnCreated() throws Exception {
        // Arrange
        Battle battle = createTestBattle();
        when(battleService.getBattleUtil()).thenReturn(battleUtil);
        when(battleUtil.getOnGoingBattleForTodayByCampaignAndUserId(CAMPAIGN_ID, USER_ID))
                .thenReturn(battle);
        when(battleService.getUpdatedBattle(battle)).thenReturn(battle);
        when(battleService.triggerNpcTurn(battle)).thenReturn(battle);

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/action/trigger-npc-turn", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void createNewDailyBattle_ShouldReturnCreatedResponse() throws Exception {
        // Arrange
        Battle newBattle = createTestBattle();
        when(battleService.createNewBattle(CAMPAIGN_ID, USER_ID)).thenReturn(newBattle);

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/new", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/campaign/" + CAMPAIGN_ID + "/battle/" + BATTLE_ID))
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void characterAttacks_ShouldReturnTurnResponse() throws Exception {
        // Arrange
        TurnRequestDto turnRequest = new TurnRequestDto(1L, 2L, ActionType.PHYSICAL_ATTACK, null);
        Turn turn = createTestTurn();
        when(battleService.performPhysicalAttack(CAMPAIGN_ID, USER_ID, BATTLE_ID, turnRequest)).thenReturn(turn);

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/{battleId}/action/attack", CAMPAIGN_ID, BATTLE_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turnRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void characterUsesConsumable_ShouldReturnTurnResponse() throws Exception {
        // Arrange
        TurnRequestDto turnRequest = new TurnRequestDto(1L, 2L, ActionType.CONSUMABLE, 100L);
        Turn turn = createTestTurn();
        when(battleService.useConsumable(CAMPAIGN_ID, USER_ID, BATTLE_ID, turnRequest)).thenReturn(turn);

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/{battleId}/action/consumable", CAMPAIGN_ID, BATTLE_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turnRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void characterCastsSpell_ShouldReturnTurnResponse() throws Exception {
        // Arrange
        TurnRequestDto turnRequest = new TurnRequestDto(1L, 2L, ActionType.SPELL, 100L);
        Turn turn = createTestTurn();
        when(battleService.castSpell(CAMPAIGN_ID, USER_ID, BATTLE_ID, turnRequest)).thenReturn(turn);

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/{battleId}/action/spell", CAMPAIGN_ID, BATTLE_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turnRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void handleBattleAlreadyStarted_ShouldReturnConflict() throws Exception {
        // Arrange
        when(battleService.createNewBattle(CAMPAIGN_ID, USER_ID))
                .thenThrow(new InvalidBattle("Battle already started"));

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/new", CAMPAIGN_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("Battle already started"));
    }

    @Test
    @WithMockUser
    void handleInvalidTurn_ShouldReturnConflict() throws Exception {
        // Arrange
        TurnRequestDto turnRequest = new TurnRequestDto(1L, 2L, ActionType.PHYSICAL_ATTACK, null);
        when(battleService.performPhysicalAttack(CAMPAIGN_ID, USER_ID, BATTLE_ID, turnRequest))
                .thenThrow(new InvalidTurn("Invalid turn"));

        // Act & Assert
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/{battleId}/action/attack", CAMPAIGN_ID, BATTLE_ID)
                        .with(jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turnRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Invalid turn"));
    }

    private Battle createTestBattle() {
        Battle battle = new Battle();
        battle.setId(BATTLE_ID);
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        battle.setCampaign(campaign);
        
        // Initialize all list fields to avoid null pointer exceptions in DTO conversion
        battle.setTurns(new java.util.ArrayList<>());
        battle.setStartingTeamOne(new java.util.ArrayList<>());
        battle.setStartingTeamTwo(new java.util.ArrayList<>());
        battle.setTeamOne(new java.util.ArrayList<>());
        battle.setTeamTwo(new java.util.ArrayList<>());
        battle.setWinningTeam(new java.util.ArrayList<>());
        battle.setLosingTeam(new java.util.ArrayList<>());
        
        return battle;
    }

    private Turn createTestTurn() {
        Turn turn = new Turn();
        com.github.acolote1998.humble_gladiators_2.core.model.Action action = 
                new com.github.acolote1998.humble_gladiators_2.core.model.Action();
        action.setDamageCaused(10);
        turn.setAction(action);
        
        // Set characters with Stats and Inventory to avoid null pointer in DTO conversion
        com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance performingChar = 
                createTestCharacterInstance();
        com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance targetChar = 
                createTestCharacterInstance();
        turn.setPerformingCharacter(performingChar);
        turn.setTargetCharacter(targetChar);
        
        return turn;
    }
    
    private com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance createTestCharacterInstance() {
        com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance character = 
                new com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance();
        character.setName("Test Character");
        
        // Set Stats
        com.github.acolote1998.humble_gladiators_2.characters.model.Stats stats = 
                new com.github.acolote1998.humble_gladiators_2.characters.model.Stats();
        stats.setConstitution(10);
        stats.setIntelligence(10);
        stats.setStrength(10);
        stats.setSpeed(10);
        stats.setLuck(10);
        stats.setMaxHp(100);
        stats.setCurrentHp(100);
        stats.setMaxMp(50);
        stats.setCurrentMp(50);
        stats.setHeight(170);
        stats.setWeight(70);
        stats.setLevel(1);
        stats.setCurrentExp(0);
        stats.setExpForNextLevel(100);
        character.setStats(stats);
        
        // Set Inventory
        com.github.acolote1998.humble_gladiators_2.characters.model.Inventory inventory = 
                new com.github.acolote1998.humble_gladiators_2.characters.model.Inventory();
        inventory.setArmors(new java.util.ArrayList<>());
        inventory.setBoots(new java.util.ArrayList<>());
        inventory.setConsumables(new java.util.ArrayList<>());
        inventory.setHelmets(new java.util.ArrayList<>());
        inventory.setShields(new java.util.ArrayList<>());
        inventory.setSpells(new java.util.ArrayList<>());
        inventory.setWeapons(new java.util.ArrayList<>());
        inventory.setGold(0);
        character.setInventory(inventory);
        
        // Set Campaign
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);
        character.setCampaign(campaign);
        
        return character;
    }
}

