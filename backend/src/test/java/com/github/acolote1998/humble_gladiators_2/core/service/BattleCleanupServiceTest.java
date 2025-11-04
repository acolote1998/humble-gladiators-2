package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.repository.BattleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleCleanupServiceTest {

    @Mock
    private BattleRepository battleRepository;

    @Mock
    private BattleService battleService;

    @InjectMocks
    private BattleCleanupService battleCleanupService;

    @Test
    void consolidateBattle_ShouldSetOngoingToFalseAndClearTeams() {
        // Arrange
        Battle battle = new Battle();
        battle.setOngoing(true);
        battle.setTeamOne(new ArrayList<>());
        battle.setTeamTwo(new ArrayList<>());
        CharacterInstance char1 = new CharacterInstance();
        CharacterInstance char2 = new CharacterInstance();
        battle.getTeamOne().add(char1);
        battle.getTeamTwo().add(char2);
        
        doNothing().when(battleService).fullyRecoverBothTeams(any(Battle.class));

        // Act
        battleCleanupService.consolidateBattle(battle);

        // Assert
        assertFalse(battle.isOngoing());
        assertTrue(battle.getTeamOne().isEmpty());
        assertTrue(battle.getTeamTwo().isEmpty());
        verify(battleService).fullyRecoverBothTeams(battle);
    }

    @Test
    void consolidateBattle_WhenWinningTeamIsEmpty_ShouldAddFirstTeamTwoMember() {
        // Arrange
        Battle battle = new Battle();
        battle.setOngoing(true);
        battle.setTeamOne(new ArrayList<>());
        battle.setTeamTwo(new ArrayList<>());
        battle.setWinningTeam(new ArrayList<>());
        CharacterInstance teamTwoMember = new CharacterInstance();
        battle.getTeamTwo().add(teamTwoMember);
        
        doNothing().when(battleService).fullyRecoverBothTeams(any(Battle.class));

        // Act
        battleCleanupService.consolidateBattle(battle);

        // Assert
        assertTrue(battle.getWinningTeam().contains(teamTwoMember));
        verify(battleService).fullyRecoverBothTeams(battle);
    }

    @Test
    void consolidateBattle_WhenLosingTeamIsEmpty_ShouldAddFirstTeamOneMember() {
        // Arrange
        Battle battle = new Battle();
        battle.setOngoing(true);
        battle.setTeamOne(new ArrayList<>());
        battle.setTeamTwo(new ArrayList<>());
        battle.setLosingTeam(new ArrayList<>());
        CharacterInstance teamOneMember = new CharacterInstance();
        battle.getTeamOne().add(teamOneMember);
        
        doNothing().when(battleService).fullyRecoverBothTeams(any(Battle.class));

        // Act
        battleCleanupService.consolidateBattle(battle);

        // Assert
        assertTrue(battle.getLosingTeam().contains(teamOneMember));
        verify(battleService).fullyRecoverBothTeams(battle);
    }
}

