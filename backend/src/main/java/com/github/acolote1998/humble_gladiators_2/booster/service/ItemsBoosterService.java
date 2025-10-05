package com.github.acolote1998.humble_gladiators_2.booster.service;

import com.github.acolote1998.humble_gladiators_2.booster.enums.ItemTypesForBooster;
import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import com.github.acolote1998.humble_gladiators_2.booster.repository.ItemsBoosterRepository;
import com.github.acolote1998.humble_gladiators_2.item.service.*;
import com.github.acolote1998.humble_gladiators_2.item.templates.*;
import jakarta.transaction.Transactional;
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

    @Transactional
    ItemsBooster getItemsBooster(Long campaignId, String userId) {
        ItemsBooster newBooster = new ItemsBooster();
        List<ArmorTemplate> armors = new ArrayList<>();
        List<BootsTemplate> boots = new ArrayList<>();
        List<ConsumableTemplate> consumables = new ArrayList<>();
        List<HelmetTemplate> helmets = new ArrayList<>();
        List<ShieldTemplate> shields = new ArrayList<>();
        List<SpellTemplate> spells = new ArrayList<>();
        List<WeaponTemplate> weapons = new ArrayList<>();

        //Gets three items
        for (int i = 0; i < 3; i++) {
            switch (getRandomItemType()) {
                case ARMORS -> {
                    ArmorTemplate armorItem = armorService.getRandomArmorTemplate(campaignId, userId);
                    armorItem.setDiscovered(true);
                    armorService.saveArmor(armorItem);
                    armors.add(armorItem);
                }
                case BOOTS -> {
                    BootsTemplate bootsItem = bootsService.getRandomBootTemplate(campaignId, userId);
                    bootsItem.setDiscovered(true);
                    bootsService.saveBoots(bootsItem);
                    boots.add(bootsItem);
                }
                case CONSUMABLES -> {
                    ConsumableTemplate consumable = consumableService.getRandomConsumableTemplate(campaignId, userId);
                    consumable.setDiscovered(true);
                    consumableService.saveConsumable(consumable);
                    consumables.add(consumable);
                }
                case HELMETS -> {
                    HelmetTemplate helmet = helmetService.getRandomHelmetTemplate(campaignId, userId);
                    helmet.setDiscovered(true);
                    helmetService.saveHelmet(helmet);
                    helmets.add(helmet);
                }
                case SHIELDS -> {
                    ShieldTemplate shield = shieldService.getRandomShieldTemplate(campaignId, userId);
                    shield.setDiscovered(true);
                    shieldService.saveShield(shield);
                    shields.add(shield);
                }
                case SPELLS -> {
                    SpellTemplate spell = spellService.getRandomSpellTemplate(campaignId, userId);
                    spell.setDiscovered(true);
                    spellService.saveSpell(spell);
                    spells.add(spell);
                }
                case WEAPONS -> {
                    WeaponTemplate weapon = weaponService.getRandomWeaponTemplate(campaignId, userId);
                    weapon.setDiscovered(true);
                    weaponService.saveWeapon(weapon);
                    weapons.add(weapon);
                }
            }
        }

        newBooster.setArmors(armors);
        newBooster.setBoots(boots);
        newBooster.setConsumables(consumables);
        newBooster.setHelmets(helmets);
        newBooster.setShields(shields);
        newBooster.setSpells(spells);
        newBooster.setWeapons(weapons);
        return itemsBoosterRepository.save(newBooster);
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
