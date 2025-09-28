package com.github.acolote1998.humble_gladiators_2.characters.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.item.instances.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InventoryService {
    public static Inventory createBlankInventory() {
        Inventory newInventory = new Inventory();
        List<ArmorInstance> armors = new ArrayList<>();
        List<BootsInstance> boots = new ArrayList<>();
        List<ConsumableInstance> consumables = new ArrayList<>();
        List<HelmetInstance> helmets = new ArrayList<>();
        List<ShieldInstance> shields = new ArrayList<>();
        List<SpellInstance> spells = new ArrayList<>();
        List<WeaponInstance> weapons = new ArrayList<>();
        Integer gold = 0;
        newInventory.setArmors(armors);
        newInventory.setBoots(boots);
        newInventory.setConsumables(consumables);
        newInventory.setHelmets(helmets);
        newInventory.setShields(shields);
        newInventory.setSpells(spells);
        newInventory.setWeapons(weapons);
        newInventory.setGold(gold);
        return newInventory;
    }
}
