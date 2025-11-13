package com.github.acolote1998.humble_gladiators_2.core.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class GameBalanceConfig {
    
    @Value("${CONSTITUTION_HP_MODIFIER}")
    private Integer constitutionHpModifier;
    
    @Value("${INTELLIGENCE_MP_MODIFIER}")
    private Integer intelligenceMpModifier;
    
    @Value("${LEVEL_DAMAGE_MULTIPLIER}")
    private Double levelDamageMultiplier;
    
    @Value("${CONSUMABLE_HP_MULTIPLIER}")
    private Integer consumableHpMultiplier;
    
    @Value("${CONSUMABLE_MP_MULTIPLIER}")
    private Integer consumableMpMultiplier;
    
    @Value("${SPELL_MP_COST_MULTIPLIER}")
    private Double spellMpCostMultiplier;
    
    @Value("${PHYSICAL_DAMAGE_MULTIPLIER}")
    private Integer physicalDamageMultiplier;
    
    @Value("${MAGICAL_DAMAGE_MULTIPLIER}")
    private Integer magicalDamageMultiplier;
}

