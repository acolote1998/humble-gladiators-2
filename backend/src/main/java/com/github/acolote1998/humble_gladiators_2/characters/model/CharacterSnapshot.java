package com.github.acolote1998.humble_gladiators_2.characters.model;

import com.github.acolote1998.humble_gladiators_2.item.instances.*;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
@Setter
public class CharacterSnapshot extends AbstractCharacter {
    private Boolean discovered;
    private Integer tier;
    private Integer rarity;
    private Integer physicalDefense;
    private Integer magicalDefense;
    private Integer physicalDamage;
    private Integer magicalDamage;

    public void setPhysicalDefense(Inventory inventoryToProcess) {
        Integer totalPhysicalDefense = 0;
        ArmorInstance equippedArmor = inventoryToProcess.getArmors().stream().filter(ArmorInstance::getEquipped).findFirst().orElse(null);
        if (equippedArmor != null) {
            totalPhysicalDefense += equippedArmor.getTemplate().getPhysicalDefense();
        }
        BootsInstance equippedBoots = inventoryToProcess.getBoots().stream().filter(BootsInstance::getEquipped).findFirst().orElse(null);
        if (equippedBoots != null) {
            totalPhysicalDefense += equippedBoots.getTemplate().getPhysicalDefense();
        }
        HelmetInstance equippedHelmet = inventoryToProcess.getHelmets().stream().filter(HelmetInstance::getEquipped).findFirst().orElse(null);
        if (equippedHelmet != null) {
            totalPhysicalDefense += equippedHelmet.getTemplate().getPhysicalDefense();
        }
        ShieldInstance equippedShield = inventoryToProcess.getShields().stream().filter(ShieldInstance::getEquipped).findFirst().orElse(null);
        if (equippedShield != null) {
            totalPhysicalDefense += equippedShield.getTemplate().getPhysicalDefense();
        }
        this.physicalDefense = totalPhysicalDefense;
    }

    public void setMagicalDefense(Inventory inventoryToProcess) {
        Integer totalMagicalDefense = 0;

        ArmorInstance equippedArmor = inventoryToProcess.getArmors().stream()
                .filter(ArmorInstance::getEquipped)
                .findFirst()
                .orElse(null);
        if (equippedArmor != null) {
            totalMagicalDefense += equippedArmor.getTemplate().getMagicalDefense();
        }

        BootsInstance equippedBoots = inventoryToProcess.getBoots().stream()
                .filter(BootsInstance::getEquipped)
                .findFirst()
                .orElse(null);
        if (equippedBoots != null) {
            totalMagicalDefense += equippedBoots.getTemplate().getMagicalDefense();
        }

        HelmetInstance equippedHelmet = inventoryToProcess.getHelmets().stream()
                .filter(HelmetInstance::getEquipped)
                .findFirst()
                .orElse(null);
        if (equippedHelmet != null) {
            totalMagicalDefense += equippedHelmet.getTemplate().getMagicalDefense();
        }

        ShieldInstance equippedShield = inventoryToProcess.getShields().stream()
                .filter(ShieldInstance::getEquipped)
                .findFirst()
                .orElse(null);
        if (equippedShield != null) {
            totalMagicalDefense += equippedShield.getTemplate().getMagicalDefense();
        }

        this.magicalDefense = totalMagicalDefense;
    }

    public void setPhysicalDamage(Inventory inventoryToProcess) {
        Integer totalPhysicalDamage = 0;
        WeaponInstance equippedWeapon = inventoryToProcess.getWeapons().stream().filter(WeaponInstance::getEquipped).findFirst().orElse(null);
        if (equippedWeapon != null) {
            totalPhysicalDamage += equippedWeapon.getTemplate().getPhysicalDamage();
        }
        this.physicalDamage = totalPhysicalDamage;
    }

    public void setMagicalDamage(Inventory inventoryToProcess) {
        Integer totalMagicalDamage = 0;
        WeaponInstance equippedWeapon = inventoryToProcess.getWeapons().stream().filter(WeaponInstance::getEquipped).findFirst().orElse(null);
        if (equippedWeapon != null) {
            totalMagicalDamage += equippedWeapon.getTemplate().getMagicalDamage();
        }
        this.magicalDamage = totalMagicalDamage;
    }


    public static CharacterSnapshot fromCharacterInstance(CharacterInstance charToClone) {
        CharacterSnapshot charSnapshot = new CharacterSnapshot();
        charSnapshot.setUserId(charToClone.getUserId());
        charSnapshot.setStats(Stats.cloneStats(charToClone.getStats()));
        charSnapshot.setCampaign(charToClone.getCampaign());
        charSnapshot.setImgBytes(charToClone.getImgBytes());
        charSnapshot.setName(charToClone.getName());
        charSnapshot.setDescription(charToClone.getDescription());
        charSnapshot.setMagicalDamage(charToClone.getInventory());
        charSnapshot.setMagicalDefense(charToClone.getInventory());
        charSnapshot.setPhysicalDamage(charToClone.getInventory());
        charSnapshot.setPhysicalDefense(charToClone.getInventory());
        // If I think correctly,
        // the char snapshot does not really need the char instances inventory?
        // We should be able to replicate the battle with the turns and actions
        charSnapshot.setInventory(null);
        // I don't think we need the gold reward in the snapshot
        charSnapshot.setGoldReward(null);
        // I don't think we need the exp reward in the snapshot
        charSnapshot.setExpReward(null);
        return charSnapshot;
    }
}
