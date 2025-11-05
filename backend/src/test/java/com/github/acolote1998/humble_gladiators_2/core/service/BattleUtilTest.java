package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidBattle;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import com.github.acolote1998.humble_gladiators_2.core.repository.BattleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattleUtilTest {

    @Mock
    private BattleRepository battleRepository;

    @InjectMocks
    private BattleUtil battleUtil;

    private static final String USER_ID = "test-user-id";
    private static final Long CAMPAIGN_ID = 1L;

    @Test
    void getOnGoingBattleForTodayByCampaignAndUserId_WhenBattleExists_ShouldReturnBattle() {
        // Arrange
        Battle battle = new Battle();
        battle.setId(100L);
        when(battleRepository.findOnGoingByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(battle);

        // Act
        Battle result = battleUtil.getOnGoingBattleForTodayByCampaignAndUserId(CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(battle, result);
    }

    @Test
    void getOnGoingBattleForTodayByCampaignAndUserId_WhenBattleDoesNotExist_ShouldThrowException() {
        // Arrange
        when(battleRepository.findOnGoingByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidBattle.class, () -> {
            battleUtil.getOnGoingBattleForTodayByCampaignAndUserId(CAMPAIGN_ID, USER_ID);
        });
    }

    @Test
    void getFinishedBattleForTodayByCampaignAndUserId_WhenBattleExists_ShouldReturnBattle() {
        // Arrange
        Battle battle = new Battle();
        battle.setId(100L);
        when(battleRepository.findFinishedByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(battle);

        // Act
        Battle result = battleUtil.getFinishedBattleForTodayByCampaignAndUserId(CAMPAIGN_ID, USER_ID);

        // Assert
        assertEquals(battle, result);
    }

    @Test
    void getFinishedBattleForTodayByCampaignAndUserId_WhenBattleDoesNotExist_ShouldThrowException() {
        // Arrange
        when(battleRepository.findFinishedByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidBattle.class, () -> {
            battleUtil.getFinishedBattleForTodayByCampaignAndUserId(CAMPAIGN_ID, USER_ID);
        });
    }

    @Test
    void isThereOngoingBattleForToday_WhenBattleExists_ShouldReturnTrue() {
        // Arrange
        Battle battle = new Battle();
        when(battleRepository.findOnGoingByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(battle);

        // Act
        boolean result = battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID);

        // Assert
        assertTrue(result);
    }

    @Test
    void isThereOngoingBattleForToday_WhenBattleDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(battleRepository.findOnGoingByCampaignIdAndUserIdAndUpdatedAtDate(
                eq(CAMPAIGN_ID), eq(USER_ID), any(LocalDate.class))).thenReturn(null);

        // Act
        boolean result = battleUtil.isThereOngoingBattleForToday(CAMPAIGN_ID, USER_ID);

        // Assert
        assertFalse(result);
    }
}

