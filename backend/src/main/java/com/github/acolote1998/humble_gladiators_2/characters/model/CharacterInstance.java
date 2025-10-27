package com.github.acolote1998.humble_gladiators_2.characters.model;

import com.github.acolote1998.humble_gladiators_2.core.enums.ActionType;
import com.github.acolote1998.humble_gladiators_2.core.enums.StateType;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidTurn;
import com.github.acolote1998.humble_gladiators_2.core.model.Action;
import com.github.acolote1998.humble_gladiators_2.item.instances.*;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Entity
@Getter
@Setter
@Slf4j
public class CharacterInstance extends AbstractCharacter implements Discoverable, Attacker, Defendable, Aliveable, Usable, Castable, Healable {

    private Boolean discovered;
    private Integer tier;
    private Integer rarity;

    @Override
    public void discover() {

    }

    public static String ObjectStructure(Long campaignId) {
        return String.format("""
                CharacterInstance{
                Stats stats
                CharacterCategory category (enum)
                characterType (must be "NPC")
                String name (character name generated based on the wanted themes)
                String description
                Long campaign_id (%s)
                Integer rarity (1 - 5)
                Integer tier (1 - 5)
                }""", campaignId.toString());
    }

    public static boolean isValidCharacter(CharacterInstance character) {
        if (character == null) {
            log.warn("CharacterInstance is null");
            return false;
        }

        if (character.getName() == null || character.getName().isBlank()) {
            log.warn("{} has invalid name", character);
            return false;
        }

        if (character.getDescription() == null || character.getDescription().isBlank()) {
            log.warn("{} has invalid description", character);
            return false;
        }

        if (character.getRarity() == null || character.getRarity() < 1 || character.getRarity() > 5) {
            log.warn("{} has invalid rarity (expected 1–5)", character);
            return false;
        }

        if (character.getTier() == null || character.getTier() < 1 || character.getTier() > 5) {
            log.warn("{} has invalid tier (expected 1-5)", character);
            return false;
        }

        if (character.getUserId() == null || character.getUserId().isBlank()) {
            log.warn("{} has invalid userId", character);
            return false;
        }

        if (character.getCampaign() == null) {
            log.warn("{} has no campaign assigned", character);
            return false;
        }

        if (character.getStats() == null) {
            log.warn("{} has no stats assigned", character);
            return false;
        }

        if (character.getGoldReward() == null || character.getGoldReward() < 0) {
            log.warn("{} has invalid gold reward", character);
            return false;
        }

        if (character.getExpReward() == null || character.getExpReward() < 0) {
            log.warn("{} has invalid exp reward", character);
            return false;
        }

        return true;
    }

    public static boolean areValidCharacters(List<CharacterInstance> characters, Integer expectedAmount) {
        if (characters.size() != expectedAmount) {
            return false;
        }
        for (CharacterInstance character : characters) {
            if (!CharacterInstance.isValidCharacter(character)) {
                return false;
            }
        }
        return true;
    }

    public Integer getPhysicalDefense() {
        Integer totalPhysicalDefense = 0;
        Inventory characterInventory = this.getInventory();
        ArmorInstance equippedArmor = characterInventory.getArmors().stream().filter(ArmorInstance::getEquipped).findFirst().orElse(null);
        if (equippedArmor != null) {
            totalPhysicalDefense += equippedArmor.getTemplate().getPhysicalDefense();
        }
        BootsInstance equippedBoots = characterInventory.getBoots().stream().filter(BootsInstance::getEquipped).findFirst().orElse(null);
        if (equippedBoots != null) {
            totalPhysicalDefense += equippedBoots.getTemplate().getPhysicalDefense();
        }
        HelmetInstance equippedHelmet = characterInventory.getHelmets().stream().filter(HelmetInstance::getEquipped).findFirst().orElse(null);
        if (equippedHelmet != null) {
            totalPhysicalDefense += equippedHelmet.getTemplate().getPhysicalDefense();
        }
        ShieldInstance equippedShield = characterInventory.getShields().stream().filter(ShieldInstance::getEquipped).findFirst().orElse(null);
        if (equippedShield != null) {
            totalPhysicalDefense += equippedShield.getTemplate().getPhysicalDefense();
        }
        return totalPhysicalDefense;
    }

    public Integer getMagicalDefense() {
        Integer totalMagicalDefense = 0;
        Inventory characterInventory = this.getInventory();

        ArmorInstance equippedArmor = characterInventory.getArmors().stream()
                .filter(ArmorInstance::getEquipped)
                .findFirst()
                .orElse(null);
        if (equippedArmor != null) {
            totalMagicalDefense += equippedArmor.getTemplate().getMagicalDefense();
        }

        BootsInstance equippedBoots = characterInventory.getBoots().stream()
                .filter(BootsInstance::getEquipped)
                .findFirst()
                .orElse(null);
        if (equippedBoots != null) {
            totalMagicalDefense += equippedBoots.getTemplate().getMagicalDefense();
        }

        HelmetInstance equippedHelmet = characterInventory.getHelmets().stream()
                .filter(HelmetInstance::getEquipped)
                .findFirst()
                .orElse(null);
        if (equippedHelmet != null) {
            totalMagicalDefense += equippedHelmet.getTemplate().getMagicalDefense();
        }

        ShieldInstance equippedShield = characterInventory.getShields().stream()
                .filter(ShieldInstance::getEquipped)
                .findFirst()
                .orElse(null);
        if (equippedShield != null) {
            totalMagicalDefense += equippedShield.getTemplate().getMagicalDefense();
        }

        return totalMagicalDefense;
    }

    public Integer getPhysicalDamage() {
        Integer totalPhysicalDamage = 0;
        Inventory characterInventory = this.getInventory();
        WeaponInstance equippedWeapon = characterInventory.getWeapons().stream().filter(WeaponInstance::getEquipped).findFirst().orElse(null);
        if (equippedWeapon != null) {
            totalPhysicalDamage += equippedWeapon.getTemplate().getPhysicalDamage();
        }
        return totalPhysicalDamage;
    }

    public Integer getMagicalDamage() {
        Integer totalMagicalDamage = 0;
        Inventory characterInventory = this.getInventory();
        WeaponInstance equippedWeapon = characterInventory.getWeapons().stream().filter(WeaponInstance::getEquipped).findFirst().orElse(null);
        if (equippedWeapon != null) {
            totalMagicalDamage += equippedWeapon.getTemplate().getMagicalDamage();
        }
        return totalMagicalDamage;
    }


    @Override
    public Integer causePhysicalDamage() {
        Integer proposedDamage = 0;
        Integer strengthDmgModifier = Math.round((float) (this.getStats().getStrength() * this.getStats().getLevel()) / 2);
        Integer physicalDamage = this.getPhysicalDamage();
        proposedDamage += strengthDmgModifier + physicalDamage;
        log.info("{} proposes {} physical damage", this.getName(), proposedDamage);
        return proposedDamage;
    }

    @Override
    public Integer causePhysicalSpellDamage(SpellInstance spellToUse) {
        Integer proposedDamage = 0;
        if (spellToUse.getTemplate().getPhysicalDamage() == 0) {
            return proposedDamage;
        }
        Integer strengthDmgModifier = Math.round((float) (this.getStats().getStrength() * this.getStats().getLevel()) / 2);
        Integer physicalDamage = spellToUse.getTemplate().getPhysicalDamage();
        proposedDamage += strengthDmgModifier + physicalDamage;
        log.info("{} proposes {} physical damage", this.getName(), proposedDamage);
        return proposedDamage;
    }

    @Override
    public Integer causeMagicalDamage(SpellInstance spellToUse) {
        Integer proposedDamage = 0;
        Integer intelligenceModifier = Math.round((float) (this.getStats().getIntelligence() * this.getStats().getLevel()) / 2);
        Integer magicalDamage = this.getMagicalDamage();
        proposedDamage += intelligenceModifier + magicalDamage + spellToUse.getTemplate().getMagicalDamage();
        log.info("'{}' proposes '{}' magical damage", this.getName(), proposedDamage);
        return proposedDamage;
    }


    @Override
    public Integer defendPhysicalDamage(Integer incomingDamage) {
        Integer totalDamage = 0;
        if (incomingDamage > this.getPhysicalDefense()) {
            totalDamage = incomingDamage - this.getPhysicalDefense();
        }
        log.info("'{}' had '{}' physical damage go through after defending", this.getName(), totalDamage);
        return totalDamage;
    }

    @Override
    public Integer defendMagicalDamage(Integer incomingDamage) {
        Integer totalDamage = 0;
        if (incomingDamage > this.getMagicalDefense()) {
            totalDamage = incomingDamage - this.getMagicalDefense();
        }
        log.info("'{}' had '{}' magical damage go through after defending", this.getName(), totalDamage);
        return totalDamage;
    }


    @Override
    public boolean isAlive() {
        boolean isAlive = this.getStats().getCurrentHp() > 0;
        if (isAlive) {
            log.info("'{}' is alive", this.getName());
        } else {
            log.info("'{}' is dead", this.getName());
        }
        return isAlive;
    }

    @Override
    public void sufferDamage(Integer amountOfDamage) {

        if (amountOfDamage == null || amountOfDamage <= 0) {
            return;
        }

        int currentHp = this.getStats().getCurrentHp();
        int newHp = Math.max(0, currentHp - amountOfDamage);
        log.info("'{}' suffered '{}' damage, they now have '{}' hp", this.getName(), amountOfDamage, newHp);
        this.getStats().setCurrentHp(newHp);
    }

    @Override
    public void consumeMp(Integer amountOfMp) {

        if (amountOfMp == null || amountOfMp <= 0) {
            return;
        }

        int currentMp = this.getStats().getCurrentMp();
        int newMp = Math.max(0, currentMp - amountOfMp);
        log.info("'{}' consumed '{}' mp, they now have '{}' mp", this.getName(), amountOfMp, newMp);
        this.getStats().setCurrentMp(newMp);
    }

    @Override
    public void heal(Integer amountOfHpToHeal) {
        if (amountOfHpToHeal == null || amountOfHpToHeal <= 0) {
            return;
        }
        int currentHp = this.getStats().getCurrentHp();
        int newHp = Math.min(this.getStats().getMaxHp(), currentHp + amountOfHpToHeal);
        log.info("'{}' healed '{}' hp, the new hp is '{}'", this.getName(), amountOfHpToHeal, newHp);
        this.getStats().setCurrentHp(newHp);
    }

    @Override
    public void recoverMp(Integer amountOfMp) {
        if (amountOfMp == null || amountOfMp <= 0) {
            return;
        }
        int currentMp = this.getStats().getCurrentMp();
        int newMp = Math.min(this.getStats().getMaxMp(), currentMp + amountOfMp);
        log.info("'{}' recovered '{}' mp, the new mp is '{}'", this.getName(), amountOfMp, newMp);
        this.getStats().setCurrentMp(newMp);
    }

    @Override
    public Action useConsumable(Long consumableId, CharacterInstance targetCharacter) {
        ConsumableInstance consumableToUseFromPerformerCharacterInventory = this
                .getInventory()
                .getConsumables()
                .stream()
                .filter(consumableInstance -> consumableInstance.getId().equals(consumableId))
                .findFirst()
                .orElse(null);
        if (consumableToUseFromPerformerCharacterInventory == null) {
            throw new InvalidTurn("Request to process consumable is not valid");
        }

        int restoreHp = consumableToUseFromPerformerCharacterInventory.getTemplate().getRestoreHp();
        int restoreMp = consumableToUseFromPerformerCharacterInventory.getTemplate().getRestoreMp();

        // Use the existing methods which handle clamping and logging
        if (restoreHp > 0) {
            targetCharacter.heal(restoreHp);
        }

        if (restoreMp > 0) {
            targetCharacter.recoverMp(restoreMp);
        }

        // Decrement consumable quantity
        consumableToUseFromPerformerCharacterInventory.setQuantity(consumableToUseFromPerformerCharacterInventory.getQuantity() - 1);

        log.info("'{}' used consumable '{}', remaining quantity: {}", this.getName(),
                consumableToUseFromPerformerCharacterInventory.getTemplate().getName(),
                consumableToUseFromPerformerCharacterInventory.getQuantity());
        if (consumableToUseFromPerformerCharacterInventory.getQuantity() == 0) {
            this.getInventory().getConsumables().remove(consumableToUseFromPerformerCharacterInventory);
            log.info("'{}' ran out of this consumable '{} - {}', removing card from their inventory",
                    this.getName(),
                    consumableToUseFromPerformerCharacterInventory.getId(),
                    consumableToUseFromPerformerCharacterInventory.getTemplate().getName());
        }
        Action actionPerformed = new Action();
        actionPerformed.setActionType(ActionType.CONSUMABLE);
        actionPerformed.setStateCaused(StateType.NONE);
        actionPerformed.setDamageCaused(0);
        actionPerformed.setHealingCaused(restoreHp);
        actionPerformed.setMpRecoverCaused(restoreMp);
        return actionPerformed;
    }


    @Override
    public Action castSpell(Long spellId, CharacterInstance targetCharacter) {
        SpellInstance spellToCastFromPerformerCharacterInventory = this
                .getInventory()
                .getSpells()
                .stream()
                .filter(spellInstance -> spellInstance.getId().equals(spellId))
                .findFirst()
                .orElse(null);
        if (spellToCastFromPerformerCharacterInventory == null) {
            throw new InvalidTurn("Request to process spell is not valid");
        }
        int mpCost = spellToCastFromPerformerCharacterInventory.getTemplate().getMpCost();

        // Deduct mana
        log.info("'{}' casts '{}' on '{}', costing {} MP (remaining MP: {})",
                this.getName(),
                spellToCastFromPerformerCharacterInventory.getName(),
                targetCharacter.getName(),
                mpCost,
                this.getStats().getCurrentMp());

        int totalDamageCaused = 0;

        // Physical damage
        if (spellToCastFromPerformerCharacterInventory.getTemplate().getPhysicalDamage() > 0) {
            int potentialPhysicalDamage = this.causePhysicalSpellDamage(spellToCastFromPerformerCharacterInventory);
            int damageAfterDefense = targetCharacter.defendPhysicalDamage(potentialPhysicalDamage);
            totalDamageCaused += damageAfterDefense;
            if (damageAfterDefense > 0) {
                log.info("'{}' deals {} physical damage to '{}'", this.getName(), damageAfterDefense, targetCharacter.getName());
            } else {
                log.info("'{}' attacks '{}' with physical spell, but damage was fully blocked", this.getName(), targetCharacter.getName());
            }
        }

        // Magical damage
        if (spellToCastFromPerformerCharacterInventory.getTemplate().getMagicalDamage() > 0) {
            int potentialMagicalDamage = this.causeMagicalDamage(spellToCastFromPerformerCharacterInventory);
            int damageAfterDefense = targetCharacter.defendMagicalDamage(potentialMagicalDamage);
            totalDamageCaused += damageAfterDefense;
            if (damageAfterDefense > 0) {
                log.info("'{}' deals {} magical damage to '{}'", this.getName(), damageAfterDefense, targetCharacter.getName());
            } else {
                log.info("'{}' attacks '{}' with magical spell, but damage was fully blocked", this.getName(), targetCharacter.getName());
            }
        }

        int totalHpRecovered = 0;

        // Healing spell
        if (spellToCastFromPerformerCharacterInventory.getTemplate().getRestoreHp() > 0) {
            int healAmount = spellToCastFromPerformerCharacterInventory.getTemplate().getRestoreHp();
            totalHpRecovered += healAmount;
            targetCharacter.heal(healAmount);
            log.info("'{}' heals '{}' for {} HP", this.getName(), targetCharacter.getName(), healAmount);
        }

        targetCharacter.sufferDamage(totalDamageCaused);
        this.consumeMp(mpCost);
        Action actionPerformed = new Action();
        actionPerformed.setActionType(ActionType.SPELL);
        actionPerformed.setStateCaused(StateType.NONE);
        actionPerformed.setDamageCaused(totalDamageCaused);
        actionPerformed.setHealingCaused(totalHpRecovered);
        actionPerformed.setMpUsage(mpCost);
        actionPerformed.setMpRecoverCaused(0);
        return actionPerformed;
    }


    @Override
    public Action usePhysicalAttack(CharacterInstance targetCharacter) {
        Integer causedDamage = 0;

        int potentialPhysicalDamage = this.causePhysicalDamage();
        int damageAfterDefense = targetCharacter.defendPhysicalDamage(potentialPhysicalDamage);

        if (damageAfterDefense > 0) {
            targetCharacter.sufferDamage(damageAfterDefense);
            causedDamage = damageAfterDefense;
            log.info("'{}' attacks '{}' with physical attack, dealing {} damage",
                    this.getName(),
                    targetCharacter.getName(),
                    damageAfterDefense);
        } else {
            log.info("'{}' attacks '{}' with physical attack, but damage was fully blocked",
                    this.getName(),
                    targetCharacter.getName());
        }
        Action actionPerformed = new Action();
        actionPerformed.setActionType(ActionType.PHYSICAL_ATTACK);
        actionPerformed.setStateCaused(StateType.NONE);
        actionPerformed.setDamageCaused(causedDamage);
        actionPerformed.setHealingCaused(0);
        actionPerformed.setMpRecoverCaused(0);
        return actionPerformed;
    }

}
