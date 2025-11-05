package com.github.acolote1998.humble_gladiators_2.core.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.repository.CharacterInstanceRepository;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.dto.TurnRequestDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.repository.CampaignRepository;
import com.github.acolote1998.humble_gladiators_2.core.service.BattleService;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.repository.*;
import com.github.acolote1998.humble_gladiators_2.testutil.TestDataFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
class BattleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private BattleService battleService;

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

    private Campaign campaignUserA;
    private Campaign campaignUserB;
    private CharacterInstance heroUserA;
    private CharacterInstance enemyUserA;
    private CharacterInstance heroUserB;
    private CharacterInstance enemyUserB;

    @BeforeEach
    void setup() {
        // Setup User A campaign with hero and enemy
        campaignUserA = TestDataFactory.persistCampaign(campaignRepository, "userA", "CampaignA");
        heroUserA = TestDataFactory.createHero(characterService, campaignUserA, "userA", "HeroA");
        
        CharacterInstance npcA = TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, campaignUserA, "userA");
        npcA.setDiscovered(true);
        npcA = characterInstanceRepository.save(npcA);
        
        TestDataFactory.createAllTestItemTemplates(entityManager, campaignUserA, "userA",
                weaponTemplateRepository, armorTemplateRepository, bootsTemplateRepository,
                helmetTemplateRepository, shieldTemplateRepository, spellTemplateRepository,
                consumableTemplateRepository);
        
        enemyUserA = characterService.getDailyEnemy(campaignUserA.getId(), "userA");

        // Setup User B campaign with hero and enemy
        campaignUserB = TestDataFactory.persistCampaign(campaignRepository, "userB", "CampaignB");
        heroUserB = TestDataFactory.createHero(characterService, campaignUserB, "userB", "HeroB");
        
        CharacterInstance npcB = TestDataFactory.createTestNPC(characterInstanceRepository, entityManager, campaignUserB, "userB");
        npcB.setDiscovered(true);
        npcB = characterInstanceRepository.save(npcB);
        
        TestDataFactory.createAllTestItemTemplates(entityManager, campaignUserB, "userB",
                weaponTemplateRepository, armorTemplateRepository, bootsTemplateRepository,
                helmetTemplateRepository, shieldTemplateRepository, spellTemplateRepository,
                consumableTemplateRepository);
        
        enemyUserB = characterService.getDailyEnemy(campaignUserB.getId(), "userB");
    }

    @Test
    void createNewDailyBattle_fullBattleCreationFlow() throws Exception {
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/new", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.onGoing").value(true))
                .andExpect(jsonPath("$.teamOne").isArray())
                .andExpect(jsonPath("$.teamTwo").isArray());
    }

    @Test
    void getAnyBattleForToday_verifiesBattleRetrieval() throws Exception {
        // First create a battle
        Battle battle = battleService.createNewBattle(campaignUserA.getId(), "userA");
        
        mockMvc.perform(get("/api/campaign/{campaignId}/battle", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(battle.getId()))
                .andExpect(jsonPath("$.campaignId").value(campaignUserA.getId()));
    }

    @Test
    void characterAttacks_performsTurnAndReturnsTurnResponse() throws Exception {
        Battle battle = battleService.createNewBattle(campaignUserA.getId(), "userA");
        
        // Reload battle to get the actual current state with teams loaded
        battle = battleService.getBattleByIdAndCampaignIdAndUserId(battle.getId(), campaignUserA.getId(), "userA");
        
        // Determine who should actually play based on battle logic (fastest character)
        // The battle determines who starts based on speed, so we need to check who the current character is
        CharacterInstance performingCharacter = battle.getCurrentCharacterToPlay();
        CharacterInstance targetCharacter;
        
        // Determine target: if performer is in team one, target is first in team two, and vice versa
        boolean performerInTeamOne = battle.getTeamOne().stream()
                .anyMatch(character -> character.getId().equals(performingCharacter.getId()));
        if (performerInTeamOne) {
            targetCharacter = battle.getTeamTwo().getFirst();
        } else {
            targetCharacter = battle.getTeamOne().getFirst();
        }
        
        TurnRequestDto turnRequest = new TurnRequestDto(
                performingCharacter.getId(),
                targetCharacter.getId(),
                ActionType.PHYSICAL_ATTACK,
                null
        );
        
        ObjectMapper mapper = new ObjectMapper();
        
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/{battleId}/action/attack", 
                        campaignUserA.getId(), battle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(turnRequest))
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.performingCharacter").exists())
                .andExpect(jsonPath("$.targetCharacter").exists())
                .andExpect(jsonPath("$.action").exists())
                .andExpect(jsonPath("$.action.actionType").value("PHYSICAL_ATTACK"))
                .andExpect(jsonPath("$.performingCharacter.id").value(performingCharacter.getId()))
                .andExpect(jsonPath("$.targetCharacter.id").value(targetCharacter.getId()));
    }

    @Test
    void userIsolation_userACannotAccessUserBBattle() throws Exception {
        Battle battleB = battleService.createNewBattle(campaignUserB.getId(), "userB");
        
        // When userA tries to access userB's battle, it throws InvalidBattle (409 Conflict)
        // because the battle doesn't exist for userA's campaign
        mockMvc.perform(get("/api/campaign/{campaignId}/battle", campaignUserB.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isConflict());
    }

    @Test
    void userIsolation_userACannotCreateBattleForUserBCampaign() throws Exception {
        // UserA doesn't have a hero for UserB's campaign, so it throws InvalidBattle (409 Conflict)
        // because isBattleAvailableForToday returns false (no hero exists)
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/new", campaignUserB.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isConflict());
    }

    @Test
    void userIsolation_userACannotPerformTurnOnUserBBattle() throws Exception {
        Battle battleB = battleService.createNewBattle(campaignUserB.getId(), "userB");
        
        TurnRequestDto turnRequest = new TurnRequestDto(
                heroUserB.getId(),
                enemyUserB.getId(),
                ActionType.PHYSICAL_ATTACK,
                null
        );
        
        ObjectMapper mapper = new ObjectMapper();
        
        // When userA tries to perform a turn on userB's battle, it throws InvalidTurn (409 Conflict)
        // because userA's character doesn't belong to that battle
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/{battleId}/action/attack", 
                        campaignUserB.getId(), battleB.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(turnRequest))
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isConflict());
    }

    @Test
    void createNewDailyBattle_returns409WhenBattleAlreadyOngoing() throws Exception {
        // Create first battle
        battleService.createNewBattle(campaignUserA.getId(), "userA");
        
        // Try to create another battle - should return 409
        mockMvc.perform(post("/api/campaign/{campaignId}/battle/new", campaignUserA.getId())
                        .with(jwt().jwt(j -> j.subject("userA"))))
                .andExpect(status().isConflict());
    }
}

