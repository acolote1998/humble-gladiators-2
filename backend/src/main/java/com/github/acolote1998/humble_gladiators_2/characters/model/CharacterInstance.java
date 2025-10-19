package com.github.acolote1998.humble_gladiators_2.characters.model;

import com.github.acolote1998.humble_gladiators_2.characters.exception.TargetHeroIsDead;
import com.github.acolote1998.humble_gladiators_2.item.instances.*;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Aliveable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Attacker;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Defendable;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Entity
@Getter
@Setter
@Slf4j
public class CharacterInstance extends AbstractCharacter implements Discoverable, Attacker, Defendable, Aliveable {

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
    public Integer casuePhysicalDamage() {
        Integer proposedDamage = 0;
        Integer strengthDmgModifier = Math.round((float) (this.getStats().getStrength() * this.getStats().getLevel()) / 2);
        Integer physicalDamage = this.getPhysicalDamage();
        proposedDamage += strengthDmgModifier + physicalDamage;
        return proposedDamage;
    }


    @Override
    public Integer defendPhysicalDamage(Integer incomingDamage) {
        Integer totalDamage = 0;
        if (incomingDamage > this.getPhysicalDamage()) {
            totalDamage = incomingDamage - this.getPhysicalDamage();
        }
        return totalDamage;
    }


    @Override
    public boolean isAlive() {
        return this.getStats().getCurrentHp() > 0;
    }

    @Override
    public void sufferDamage(Integer amountOfDamage) {
        if (!this.isAlive()) {
            throw new TargetHeroIsDead("The target hero cannot receive damage since it is already dead");
        }

        if (amountOfDamage == null || amountOfDamage <= 0) {
            return;
        }

        int currentHp = this.getStats().getCurrentHp();
        int newHp = Math.max(0, currentHp - amountOfDamage);
        this.getStats().setCurrentHp(newHp);
    }

}
