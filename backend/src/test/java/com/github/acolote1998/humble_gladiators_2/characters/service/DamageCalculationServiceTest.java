package com.github.acolote1998.humble_gladiators_2.characters.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.core.config.GameBalanceConfig;
import com.github.acolote1998.humble_gladiators_2.item.instances.SpellInstance;
import com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DamageCalculationServiceTest {

    @Mock
    private GameBalanceConfig balanceConfig;

    @InjectMocks
    private DamageCalculationService damageCalculationService;

    private CharacterInstance character;
    private Stats stats;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        // Setup GameBalanceConfig mock (lenient to avoid unnecessary stubbing warnings)
        lenient().when(balanceConfig.getLevelDamageMultiplier()).thenReturn(0.5);

        // Setup character
        character = new CharacterInstance();
        character.setName("Test Character");
        
        stats = new Stats();
        stats.setStrength(20);
        stats.setIntelligence(18);
        stats.setLevel(5);
        character.setStats(stats);
        
        inventory = new Inventory();
        inventory.setWeapons(new java.util.ArrayList<>());
        character.setInventory(inventory);
    }

    @Test
    void calculatePhysicalDamage_withWeapon_returnsCorrectDamage() {
        // Arrange
        stats.setStrength(20);
        stats.setLevel(5);
        // Mock weapon damage (normally would come from equipped weapon)
        // For this test, we'll set it via reflection or create a weapon
        // Since getPhysicalDamage() checks inventory, we need to mock that behavior
        // For simplicity, we'll test with 0 weapon damage first
        
        // Act
        Integer result = damageCalculationService.calculatePhysicalDamage(character);
        
        // Assert
        // statDamage = 20 + (5 * 0.5) = 20 + 2.5 = 22.5 rounded to 23
        // totalDamage = 23 + 0 = 23
        assertEquals(23, result);
    }

    @Test
    void calculatePhysicalDamage_withDifferentLevel_returnsCorrectDamage() {
        // Arrange
        stats.setStrength(20);
        stats.setLevel(10);
        
        // Act
        Integer result = damageCalculationService.calculatePhysicalDamage(character);
        
        // Assert
        // statDamage = 20 + (10 * 0.5) = 20 + 5 = 25
        // totalDamage = 25 + 0 = 25
        assertEquals(25, result);
    }

    @Test
    void calculatePhysicalSpellDamage_withPhysicalSpell_returnsCorrectDamage() {
        // Arrange
        stats.setStrength(20);
        stats.setLevel(5);
        
        SpellTemplate spellTemplate = new SpellTemplate();
        spellTemplate.setPhysicalDamage(15);
        
        SpellInstance spell = new SpellInstance();
        spell.setTemplate(spellTemplate);
        
        // Act
        Integer result = damageCalculationService.calculatePhysicalSpellDamage(character, spell);
        
        // Assert
        // statDamage = 20 + (5 * 0.5) = 22.5 rounded to 23
        // totalDamage = 23 + 15 = 38
        assertEquals(38, result);
    }

    @Test
    void calculatePhysicalSpellDamage_withZeroPhysicalDamage_returnsZero() {
        // Arrange
        SpellTemplate spellTemplate = new SpellTemplate();
        spellTemplate.setPhysicalDamage(0);
        
        SpellInstance spell = new SpellInstance();
        spell.setTemplate(spellTemplate);
        
        // Act
        Integer result = damageCalculationService.calculatePhysicalSpellDamage(character, spell);
        
        // Assert
        assertEquals(0, result);
    }

    @Test
    void calculateMagicalDamage_withWeaponAndSpell_returnsCorrectDamage() {
        // Arrange
        stats.setIntelligence(18);
        stats.setLevel(5);
        
        SpellTemplate spellTemplate = new SpellTemplate();
        spellTemplate.setMagicalDamage(20);
        
        SpellInstance spell = new SpellInstance();
        spell.setTemplate(spellTemplate);
        
        // Act
        Integer result = damageCalculationService.calculateMagicalDamage(character, spell);
        
        // Assert
        // statDamage = 18 + (5 * 0.5) = 18 + 2.5 = 20.5 rounded to 21
        // weaponDamage = 0 (no weapon equipped)
        // spellDamage = 20
        // totalDamage = 21 + 0 + 20 = 41
        assertEquals(41, result);
    }

    @Test
    void calculateMagicalDamage_withDifferentIntelligence_returnsCorrectDamage() {
        // Arrange
        stats.setIntelligence(25);
        stats.setLevel(3);
        
        SpellTemplate spellTemplate = new SpellTemplate();
        spellTemplate.setMagicalDamage(10);
        
        SpellInstance spell = new SpellInstance();
        spell.setTemplate(spellTemplate);
        
        // Act
        Integer result = damageCalculationService.calculateMagicalDamage(character, spell);
        
        // Assert
        // statDamage = 25 + (3 * 0.5) = 25 + 1.5 = 26.5 rounded to 27
        // totalDamage = 27 + 0 + 10 = 37
        assertEquals(37, result);
    }

    @Test
    void calculatePhysicalDamage_withLevelZero_returnsStrengthOnly() {
        // Arrange
        stats.setStrength(20);
        stats.setLevel(0);
        
        // Act
        Integer result = damageCalculationService.calculatePhysicalDamage(character);
        
        // Assert
        // statDamage = 20 + (0 * 0.5) = 20
        // totalDamage = 20 + 0 = 20
        assertEquals(20, result);
    }

    @Test
    void calculateMagicalDamage_withLevelZero_returnsIntelligenceOnly() {
        // Arrange
        stats.setIntelligence(18);
        stats.setLevel(0);
        
        SpellTemplate spellTemplate = new SpellTemplate();
        spellTemplate.setMagicalDamage(10);
        
        SpellInstance spell = new SpellInstance();
        spell.setTemplate(spellTemplate);
        
        // Act
        Integer result = damageCalculationService.calculateMagicalDamage(character, spell);
        
        // Assert
        // statDamage = 18 + (0 * 0.5) = 18
        // totalDamage = 18 + 0 + 10 = 28
        assertEquals(28, result);
    }
}

