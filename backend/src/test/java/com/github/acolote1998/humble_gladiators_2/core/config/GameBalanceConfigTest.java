package com.github.acolote1998.humble_gladiators_2.core.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class GameBalanceConfigTest {

    @Test
    void gameBalanceConfig_canBeInstantiated() {
        // Arrange & Act
        GameBalanceConfig config = new GameBalanceConfig();
        
        // Assert
        assertNotNull(config);
    }

    @Test
    void gameBalanceConfig_gettersReturnSetValues() {
        // Arrange
        GameBalanceConfig config = new GameBalanceConfig();
        ReflectionTestUtils.setField(config, "constitutionHpModifier", 5);
        ReflectionTestUtils.setField(config, "intelligenceMpModifier", 10);
        ReflectionTestUtils.setField(config, "levelDamageMultiplier", 0.5);
        ReflectionTestUtils.setField(config, "consumableHpMultiplier", 8);
        ReflectionTestUtils.setField(config, "consumableMpMultiplier", 12);
        ReflectionTestUtils.setField(config, "spellMpCostMultiplier", 2.5);
        ReflectionTestUtils.setField(config, "physicalDamageMultiplier", 15);
        ReflectionTestUtils.setField(config, "magicalDamageMultiplier", 15);
        
        // Act & Assert
        assertEquals(5, config.getConstitutionHpModifier());
        assertEquals(10, config.getIntelligenceMpModifier());
        assertEquals(0.5, config.getLevelDamageMultiplier());
        assertEquals(8, config.getConsumableHpMultiplier());
        assertEquals(12, config.getConsumableMpMultiplier());
        assertEquals(2.5, config.getSpellMpCostMultiplier());
        assertEquals(15, config.getPhysicalDamageMultiplier());
        assertEquals(15, config.getMagicalDamageMultiplier());
    }

    @Test
    void gameBalanceConfig_handlesNullValues() {
        // Arrange
        GameBalanceConfig config = new GameBalanceConfig();
        ReflectionTestUtils.setField(config, "constitutionHpModifier", null);
        ReflectionTestUtils.setField(config, "levelDamageMultiplier", null);
        
        // Act & Assert
        assertNull(config.getConstitutionHpModifier());
        assertNull(config.getLevelDamageMultiplier());
    }

    @Test
    void gameBalanceConfig_handlesDifferentValueTypes() {
        // Arrange
        GameBalanceConfig config = new GameBalanceConfig();
        ReflectionTestUtils.setField(config, "constitutionHpModifier", 100);
        ReflectionTestUtils.setField(config, "levelDamageMultiplier", 1.5);
        
        // Act & Assert
        assertEquals(100, config.getConstitutionHpModifier());
        assertEquals(1.5, config.getLevelDamageMultiplier());
    }
}

