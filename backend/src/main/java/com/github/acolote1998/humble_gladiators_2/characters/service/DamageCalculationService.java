package com.github.acolote1998.humble_gladiators_2.characters.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.core.config.GameBalanceConfig;
import com.github.acolote1998.humble_gladiators_2.item.instances.SpellInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DamageCalculationService {
    
    private final GameBalanceConfig balanceConfig;
    
    public DamageCalculationService(GameBalanceConfig balanceConfig) {
        this.balanceConfig = balanceConfig;
    }
    
    public Integer calculatePhysicalDamage(CharacterInstance performingCharacter) {
        Integer strength = performingCharacter.getStats().getStrength();
        Integer level = performingCharacter.getStats().getLevel();
        Integer weaponDamage = performingCharacter.getPhysicalDamage();
        
        Integer statDamage = Math.round(strength + (float)(level * balanceConfig.getLevelDamageMultiplier()));
        Integer totalDamage = statDamage + weaponDamage;
        
        log.info("{} proposes {} physical damage (stat: {}, weapon: {})", 
                performingCharacter.getName(), totalDamage, statDamage, weaponDamage);
        return totalDamage;
    }
    
    public Integer calculatePhysicalSpellDamage(CharacterInstance performingCharacter, SpellInstance spellToCast) {
        if (spellToCast.getTemplate().getPhysicalDamage() == 0) {
            return 0;
        }
        
        Integer strength = performingCharacter.getStats().getStrength();
        Integer level = performingCharacter.getStats().getLevel();
        Integer spellDamage = spellToCast.getTemplate().getPhysicalDamage();
        
        Integer statDamage = Math.round(strength + (float)(level * balanceConfig.getLevelDamageMultiplier()));
        Integer totalDamage = statDamage + spellDamage;
        
        log.info("{} proposes {} physical spell damage (stat: {}, spell: {})", 
                performingCharacter.getName(), totalDamage, statDamage, spellDamage);
        return totalDamage;
    }
    
    public Integer calculateMagicalDamage(CharacterInstance performingCharacter, SpellInstance spellToCast) {
        Integer intelligence = performingCharacter.getStats().getIntelligence();
        Integer level = performingCharacter.getStats().getLevel();
        Integer weaponDamage = performingCharacter.getMagicalDamage();
        Integer spellDamage = spellToCast.getTemplate().getMagicalDamage();
        
        Integer statDamage = Math.round(intelligence + (float)(level * balanceConfig.getLevelDamageMultiplier()));
        Integer totalDamage = statDamage + weaponDamage + spellDamage;
        
        log.info("{} proposes {} magical damage (stat: {}, weapon: {}, spell: {})", 
                performingCharacter.getName(), totalDamage, statDamage, weaponDamage, spellDamage);
        return totalDamage;
    }
}

