package com.github.acolote1998.humble_gladiators_2.booster.service;

import com.github.acolote1998.humble_gladiators_2.booster.enums.ItemTypesForBooster;
import com.github.acolote1998.humble_gladiators_2.booster.exception.DailyBoosterAlreadyOpened;
import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import com.github.acolote1998.humble_gladiators_2.booster.repository.ItemsBoosterRepository;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import com.github.acolote1998.humble_gladiators_2.item.service.*;
import com.github.acolote1998.humble_gladiators_2.item.templates.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ItemsBoosterService {

    private ArmorService armorService;
    private BootsService bootsService;
    private ConsumableService consumableService;
    private HelmetService helmetService;
    private ShieldService shieldService;
    private SpellService spellService;
    private WeaponService weaponService;
    private CampaignService campaignService;
    private ItemsBoosterRepository itemsBoosterRepository;
    private CharacterService characterService;

    @Value("${UNLIMITED_BOOSTERS_ALLOWED}")
    private boolean UNLIMITED_BOOSTERS_ALLOWED;

    public ItemsBoosterService(ArmorService armorService,
                               BootsService bootsService,
                               ConsumableService consumableService,
                               HelmetService helmetService,
                               ShieldService shieldService,
                               SpellService spellService,
                               WeaponService weaponService,
                               ItemsBoosterRepository itemsBoosterRepository,
                               CampaignService campaignService,
                               CharacterService characterService) {
        this.armorService = armorService;
        this.bootsService = bootsService;
        this.consumableService = consumableService;
        this.helmetService = helmetService;
        this.shieldService = shieldService;
        this.spellService = spellService;
        this.weaponService = weaponService;
        this.itemsBoosterRepository = itemsBoosterRepository;
        this.campaignService = campaignService;
        this.characterService = characterService;
    }

    private Boolean canTheUserOpenAnItemPack(Long campaignId, String userId) {
        if (!UNLIMITED_BOOSTERS_ALLOWED) {
            LocalDate today = LocalDate.now();
            ItemsBooster todaysBooster = itemsBoosterRepository
                    .findByCampaignIdAndUserIdAndUpdatedAtDate(
                            campaignId,
                            userId,
                            today);

            return todaysBooster == null;
        }
        return UNLIMITED_BOOSTERS_ALLOWED;
    }

    @Transactional
    public ItemsBooster getNewItemsBooster(Long campaignId, String userId) {
        if (!canTheUserOpenAnItemPack(campaignId, userId)) {
            log.warn(String.format("WARNING - %s - Campaign %s tried to open an item booster, but they had already opened one today", userId, campaignId));
            throw new DailyBoosterAlreadyOpened("The user already opened an item booster today");
        }
        ItemsBooster newBooster = new ItemsBooster();
        List<ArmorTemplate> armorTemplates = new ArrayList<>();
        List<BootsTemplate> bootsTemplates = new ArrayList<>();
        List<ConsumableTemplate> consumableTemplates = new ArrayList<>();
        List<HelmetTemplate> helmetTemplates = new ArrayList<>();
        List<ShieldTemplate> shieldTemplates = new ArrayList<>();
        List<SpellTemplate> spellTemplates = new ArrayList<>();
        List<WeaponTemplate> weaponTemplates = new ArrayList<>();

        //Gets three items
        for (int i = 0; i < 3; i++) {
            switch (getRandomItemType()) {
                case ARMORS -> {
                    ArmorTemplate armorTemplate = armorService.getRandomArmorTemplate(campaignId, userId);
                    armorTemplate.setDiscovered(true);
                    armorService.saveArmor(armorTemplate);
                    armorTemplates.add(armorTemplate);
                }
                case BOOTS -> {
                    BootsTemplate bootTemplate = bootsService.getRandomBootTemplate(campaignId, userId);
                    bootTemplate.setDiscovered(true);
                    bootsService.saveBoots(bootTemplate);
                    bootsTemplates.add(bootTemplate);
                }
                case CONSUMABLES -> {
                    ConsumableTemplate consumableTemplate = consumableService.getRandomConsumableTemplate(campaignId, userId);
                    consumableTemplate.setDiscovered(true);
                    consumableService.saveConsumable(consumableTemplate);
                    consumableTemplates.add(consumableTemplate);
                }
                case HELMETS -> {
                    HelmetTemplate helmetTemplate = helmetService.getRandomHelmetTemplate(campaignId, userId);
                    helmetTemplate.setDiscovered(true);
                    helmetService.saveHelmet(helmetTemplate);
                    helmetTemplates.add(helmetTemplate);
                }
                case SHIELDS -> {
                    ShieldTemplate shieldTemplate = shieldService.getRandomShieldTemplate(campaignId, userId);
                    shieldTemplate.setDiscovered(true);
                    shieldService.saveShield(shieldTemplate);
                    shieldTemplates.add(shieldTemplate);
                }
                case SPELLS -> {
                    SpellTemplate spellTemplate = spellService.getRandomSpellTemplate(campaignId, userId);
                    spellTemplate.setDiscovered(true);
                    spellService.saveSpell(spellTemplate);
                    spellTemplates.add(spellTemplate);
                }
                case WEAPONS -> {
                    WeaponTemplate weaponTemplate = weaponService.getRandomWeaponTemplate(campaignId, userId);
                    weaponTemplate.setDiscovered(true);
                    weaponService.saveWeapon(weaponTemplate);
                    weaponTemplates.add(weaponTemplate);
                }
            }
        }

        CharacterInstance hero = characterService.getHero(campaignId, userId);
        Inventory heroInventory = hero.getInventory();
        heroInventory.getArmors().addAll(armorService.instancesFromArmorTemplates(armorTemplates, heroInventory));
        heroInventory.getBoots().addAll(bootsService.instancesFromBootsTemplates(bootsTemplates, heroInventory));
        heroInventory.getConsumables().addAll(consumableService.instancesFromConsumableTemplates(consumableTemplates, heroInventory));
        heroInventory.getHelmets().addAll(helmetService.instancesFromHelmetTemplates(helmetTemplates, heroInventory));
        heroInventory.getShields().addAll(shieldService.instancesFromShieldTemplates(shieldTemplates, heroInventory));
        heroInventory.getSpells().addAll(spellService.instancesFromSpellTemplates(spellTemplates, heroInventory));
        heroInventory.getWeapons().addAll(weaponService.instancesFromWeaponTemplates(weaponTemplates, heroInventory));
        characterService.saveCharacter(hero);

        newBooster.setArmors(armorTemplates);
        newBooster.setBoots(bootsTemplates);
        newBooster.setConsumables(consumableTemplates);
        newBooster.setHelmets(helmetTemplates);
        newBooster.setShields(shieldTemplates);
        newBooster.setSpells(spellTemplates);
        newBooster.setWeapons(weaponTemplates);
        newBooster.setUserId(userId);
        newBooster.setCampaign(campaignService.getCampaignByIdAndUserId(userId, campaignId));
        log.info(String.format("%s - Campaign %s successfully opened an item booster", userId, campaignId));
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
