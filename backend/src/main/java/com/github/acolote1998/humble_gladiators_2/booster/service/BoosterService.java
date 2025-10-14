package com.github.acolote1998.humble_gladiators_2.booster.service;

import com.github.acolote1998.humble_gladiators_2.booster.enums.ItemTypesForBooster;
import com.github.acolote1998.humble_gladiators_2.booster.exception.DailyBoosterAlreadyOpened;
import com.github.acolote1998.humble_gladiators_2.booster.model.CharacterBooster;
import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import com.github.acolote1998.humble_gladiators_2.booster.repository.CharacterBoosterRepository;
import com.github.acolote1998.humble_gladiators_2.booster.repository.ItemsBoosterRepository;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.service.CharacterService;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import com.github.acolote1998.humble_gladiators_2.core.service.RunwareService;
import com.github.acolote1998.humble_gladiators_2.item.service.*;
import com.github.acolote1998.humble_gladiators_2.item.templates.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class BoosterService {

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
    private CharacterBoosterRepository characterBoosterRepository;
    private RunwareService runwareService;

    @Value("${UNLIMITED_BOOSTERS_ALLOWED}")
    private boolean UNLIMITED_BOOSTERS_ALLOWED;

    @Value("${REAL_RARITY_AND_TIER_RATE}")
    private static boolean REAL_RARITY_AND_TIER_RATE;

    @Value("${GENERATE_IMAGES}")
    private boolean IMAGE_GENERATION_ACTIVATED;

    public BoosterService(ArmorService armorService,
                          BootsService bootsService,
                          ConsumableService consumableService,
                          HelmetService helmetService,
                          ShieldService shieldService,
                          SpellService spellService,
                          WeaponService weaponService,
                          ItemsBoosterRepository itemsBoosterRepository,
                          CampaignService campaignService,
                          CharacterService characterService,
                          RunwareService runwareService,
                          CharacterBoosterRepository characterBoosterRepository) {
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
        this.runwareService = runwareService;
        this.characterBoosterRepository = characterBoosterRepository;
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

    private Boolean canTheUserOpenACharacterPack(Long campaignId, String userId) {
        if (!UNLIMITED_BOOSTERS_ALLOWED) {
            LocalDate today = LocalDate.now();
            CharacterBooster todaysBooster = characterBoosterRepository
                    .findByCampaignIdAndUserIdAndUpdatedAtDate(
                            campaignId,
                            userId,
                            today);

            return todaysBooster == null;
        }
        return UNLIMITED_BOOSTERS_ALLOWED;
    }

    public static Integer GetCalculatedTier() {
        if (!REAL_RARITY_AND_TIER_RATE) {
            return 1;
        }
        Random random = new Random();
        Integer chance = random.nextInt(1, 101);
        Integer tier = 0;
        //Tier 1 – 52%
        // Tier 2 – 32%
        // Tier 3 – 8%
        // Tier 4 – 5%
        // Tier 5 – 3%
        if (chance <= 52) {
            tier = 1;
        }
        if (chance > 52 && chance <= 84) {
            tier = 2;
        }
        if (chance > 84 && chance <= 92) {
            tier = 3;
        }
        if (chance > 92 && chance <= 97) {
            tier = 4;
        }
        if (chance > 97 && chance <= 100) {
            tier = 5;
        }
        return tier;
    }

    public static Integer GetCalculatedRarity() {
        if (!REAL_RARITY_AND_TIER_RATE) {
            return 1;
        }
        Random random = new Random();
        Integer chance = random.nextInt(1, 101);
        Integer rarity = 0;
        //Rarity 1 – 52%
        // Rarity 2 – 32%
        // Rarity 3 – 8%
        // Rarity 4 – 5%
        // Rarity 5 – 3%
        if (chance <= 52) {
            rarity = 1;
        }
        if (chance > 52 && chance <= 84) {
            rarity = 2;
        }
        if (chance > 84 && chance <= 92) {
            rarity = 3;
        }
        if (chance > 92 && chance <= 97) {
            rarity = 4;
        }
        if (chance > 97 && chance <= 100) {
            rarity = 5;
        }
        return rarity;
    }

    @Transactional
    public ItemsBooster getNewItemsBooster(Long campaignId, String userId) {
        if (!canTheUserOpenAnItemPack(campaignId, userId)) {
            log.warn(String.format("WARNING - %s - Campaign %s tried to open an item booster, but they had already opened one today", userId, campaignId));
            throw new DailyBoosterAlreadyOpened("The user already opened an item booster today");
        }
        Campaign campaign = campaignService.getCampaignByIdAndUserId(userId, campaignId);
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
                    ArmorTemplate armorTemplate = armorService.getRandomArmorTemplateForItemBooster(campaignId, userId);
                    if (IMAGE_GENERATION_ACTIVATED && armorTemplate.getImgBytes() == null) {
                        //Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateArmorTemplateImageToBytes(campaign, armorTemplate);
                        armorTemplate.setImgBytes(generatedImage);
                    }
                    armorTemplate.setDiscovered(true);
                    armorService.saveArmor(armorTemplate);
                    armorTemplates.add(armorTemplate);
                }
                case BOOTS -> {
                    BootsTemplate bootTemplate = bootsService.getRandomBootTemplateForItemBooster(campaignId, userId);
                    if (IMAGE_GENERATION_ACTIVATED && bootTemplate.getImgBytes() == null) {
                        //Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateBootsTemplateImageToBytes(campaign, bootTemplate);
                        bootTemplate.setImgBytes(generatedImage);
                    }
                    bootTemplate.setDiscovered(true);
                    bootsService.saveBoots(bootTemplate);
                    bootsTemplates.add(bootTemplate);
                }
                case CONSUMABLES -> {
                    ConsumableTemplate consumableTemplate = consumableService.getRandomConsumableTemplateForItemBooster(campaignId, userId);
                    if (IMAGE_GENERATION_ACTIVATED && consumableTemplate.getImgBytes() == null) {
                        //Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateConsumableTemplateImageToBytes(campaign, consumableTemplate);
                        consumableTemplate.setImgBytes(generatedImage);
                    }
                    consumableTemplate.setDiscovered(true);
                    consumableService.saveConsumable(consumableTemplate);
                    consumableTemplates.add(consumableTemplate);
                }
                case HELMETS -> {
                    HelmetTemplate helmetTemplate = helmetService.getRandomHelmetTemplateForItemBooster(campaignId, userId);
                    if (IMAGE_GENERATION_ACTIVATED && helmetTemplate.getImgBytes() == null) {
                        //Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateHelmetTemplateImageToBytes(campaign, helmetTemplate);
                        helmetTemplate.setImgBytes(generatedImage);
                    }
                    helmetTemplate.setDiscovered(true);
                    helmetService.saveHelmet(helmetTemplate);
                    helmetTemplates.add(helmetTemplate);
                }
                case SHIELDS -> {
                    ShieldTemplate shieldTemplate = shieldService.getRandomShieldTemplateForItemBooster(campaignId, userId);
                    if (IMAGE_GENERATION_ACTIVATED && shieldTemplate.getImgBytes() == null) {
                        //Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateShieldTemplateImageToBytes(campaign, shieldTemplate);
                        shieldTemplate.setImgBytes(generatedImage);
                    }
                    shieldTemplate.setDiscovered(true);
                    shieldService.saveShield(shieldTemplate);
                    shieldTemplates.add(shieldTemplate);
                }
                case SPELLS -> {
                    SpellTemplate spellTemplate = spellService.getRandomSpellTemplateForItemBooster(campaignId, userId);
                    if (IMAGE_GENERATION_ACTIVATED && spellTemplate.getImgBytes() == null) {
                        //Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateSpellTemplateImageToBytes(campaign, spellTemplate);
                        spellTemplate.setImgBytes(generatedImage);
                    }
                    spellTemplate.setDiscovered(true);
                    spellService.saveSpell(spellTemplate);
                    spellTemplates.add(spellTemplate);
                }
                case WEAPONS -> {
                    WeaponTemplate weaponTemplate = weaponService.getRandomWeaponTemplateForItemBooster(campaignId, userId);
                    if (IMAGE_GENERATION_ACTIVATED && weaponTemplate.getImgBytes() == null) {
                        //Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateWeaponTemplateImageToBytes(campaign, weaponTemplate);
                        weaponTemplate.setImgBytes(generatedImage);
                    }
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
        newBooster.setCampaign(campaign);
        log.info(String.format("%s - Campaign %s successfully opened an item booster", userId, campaignId));
        return itemsBoosterRepository.save(newBooster);
    }

    @Transactional
    public CharacterBooster getNewCharacterBooster(Long campaignId, String userId) {
        if (!canTheUserOpenACharacterPack(campaignId, userId)) {
            log.warn(String.format("WARNING - %s - Campaign %s tried to open a character booster, but they had already opened one today", userId, campaignId));
            throw new DailyBoosterAlreadyOpened("The user already opened a character booster today");
        }
        Campaign campaign = campaignService.getCampaignByIdAndUserId(userId, campaignId);
        CharacterBooster newBooster = new CharacterBooster();
        List<CharacterInstance> characterInstances = new ArrayList<>();

        //Gets one character
        for (int i = 0; i < 1; i++) {
            CharacterInstance characterInstance = characterService.getRandomCharacterInstanceForItemBooster(campaignId, userId);
            if (IMAGE_GENERATION_ACTIVATED && characterInstance.getImgBytes() == null) {
                //Image for this card does not exist, so we have to generate it
                byte[] generatedImage = runwareService.generateCharacterInstanceImageToBytes(campaign, characterInstance);
                characterInstance.setImgBytes(generatedImage);
            }
            characterInstance.setDiscovered(true);
            characterService.saveCharacter(characterInstance);
            characterInstances.add(characterInstance);
        }

        newBooster.setCharacters(characterInstances);
        newBooster.setUserId(userId);
        newBooster.setCampaign(campaign);
        log.info(String.format("%s - Campaign %s successfully opened a character booster", userId, campaignId));
        return characterBoosterRepository.save(newBooster);
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
