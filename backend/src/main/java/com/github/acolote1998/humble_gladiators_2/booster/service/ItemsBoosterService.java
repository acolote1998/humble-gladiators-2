package com.github.acolote1998.humble_gladiators_2.booster.service;

import com.github.acolote1998.humble_gladiators_2.booster.enums.ItemTypesForBooster;
import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import com.github.acolote1998.humble_gladiators_2.booster.repository.ItemsBoosterRepository;
import com.github.acolote1998.humble_gladiators_2.item.service.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Getter
@Setter
@Slf4j
public class ItemsBoosterService {

    private ArmorService armorService;
    private BootsService bootsService;
    private ConsumableService consumableService;
    private HelmetService helmetService;
    private ShieldService shieldService;
    private SpellService spellService;
    private WeaponService weaponService;
    private ItemsBoosterRepository itemsBoosterRepository;


    public ItemsBoosterService(ArmorService armorService,
                               BootsService bootsService,
                               ConsumableService consumableService,
                               HelmetService helmetService,
                               ShieldService shieldService,
                               SpellService spellService,
                               WeaponService weaponService,
                               ItemsBoosterRepository itemsBoosterRepository) {
        this.armorService = armorService;
        this.bootsService = bootsService;
        this.consumableService = consumableService;
        this.helmetService = helmetService;
        this.shieldService = shieldService;
        this.spellService = spellService;
        this.weaponService = weaponService;
        this.itemsBoosterRepository = itemsBoosterRepository;
    }

    ItemsBooster getItemsBooster(Long campaignId, String userId) {
        ItemsBooster newBooster = new ItemsBooster();
    }

    private ItemTypesForBooster getRandomItemType() {
        List<ItemTypesForBooster> itemTypes = new ArrayList<>(Arrays.asList(ItemTypesForBooster.ARMORS,
                ItemTypesForBooster.BOOTS,
                ItemTypesForBooster.CONSUMABLES,
                ItemTypesForBooster.HELMETS,
                ItemTypesForBooster.SHIELDS,
                ItemTypesForBooster.SPELLS,
                ItemTypesForBooster.WEAPONS));
        Collections.shuffle(itemTypes);
        return itemTypes.getFirst();
    }
}
