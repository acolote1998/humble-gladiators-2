package com.github.acolote1998.humble_gladiators_2.booster.service;

import com.github.acolote1998.humble_gladiators_2.booster.enums.IntentionTowardsBooster;
import com.github.acolote1998.humble_gladiators_2.booster.enums.ItemTypesForBooster;
import com.github.acolote1998.humble_gladiators_2.booster.exception.InvalidBooster;
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
import com.github.acolote1998.humble_gladiators_2.item.instances.ArmorInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.BootsInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.HelmetInstance;
import com.github.acolote1998.humble_gladiators_2.item.instances.ShieldInstance;
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
    private boolean REAL_RARITY_AND_TIER_RATE;

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

    public Boolean canOpenAValidItemBooster(Long campaignId, String userId, IntentionTowardsBooster intention) {
        if (intention == IntentionTowardsBooster.OPEN_BOOSTER) {
            if (!userHasDailyItemBoosterAvailable(campaignId, userId)) {
                log.warn(String.format("WARNING - %s - Campaign %s | ITEM BOOSTER | Already opened one today", userId,
                        campaignId));
            }
            if (!characterService.doesHeroExistForACampaign(campaignId, userId)) {
                log.warn(String.format("WARNING - %s - Campaign %s | ITEM BOOSTER | Hero does not exist", userId,
                        campaignId));
            }
        }
        return (userHasDailyItemBoosterAvailable(campaignId, userId) &&
                characterService.doesHeroExistForACampaign(campaignId, userId));
    }

    public Boolean canOpenAValidCharacterBooster(Long campaignId, String userId, IntentionTowardsBooster intention) {
        if (intention == IntentionTowardsBooster.OPEN_BOOSTER) {
            if (!userHasDailyCharacterBoosterAvailable(campaignId, userId)) {
                log.warn(String.format("WARNING - %s - Campaign %s | CHARACTER BOOSTER | Already opened one today",
                        userId, campaignId));
            }
            if (!characterService.doesHeroExistForACampaign(campaignId, userId)) {
                log.warn(String.format("WARNING - %s - Campaign %s | CHARACTER BOOSTER | Hero does not exist", userId,
                        campaignId));
            }
        }
        return (userHasDailyCharacterBoosterAvailable(campaignId, userId) &&
                characterService.doesHeroExistForACampaign(campaignId, userId));
    }

    public Boolean userHasDailyItemBoosterAvailable(Long campaignId, String userId) {
        if (!UNLIMITED_BOOSTERS_ALLOWED) {
            LocalDate today = LocalDate.now();
            ItemsBooster todaysBooster = itemsBoosterRepository
                    .findByCampaignIdAndUserIdAndUpdatedAtDate(
                            campaignId,
                            userId,
                            today);

            return todaysBooster == null;
        }
        return true;
    }

    public Boolean userHasDailyCharacterBoosterAvailable(Long campaignId, String userId) {
        if (!UNLIMITED_BOOSTERS_ALLOWED) {
            LocalDate today = LocalDate.now();
            CharacterBooster todaysBooster = characterBoosterRepository
                    .findByCampaignIdAndUserIdAndUpdatedAtDate(
                            campaignId,
                            userId,
                            today);
            return todaysBooster == null;
        }
        return true;
    }

    public Integer getCalculatedTier() {
        if (!REAL_RARITY_AND_TIER_RATE) {
            return 1;
        }
        Random random = new Random();
        Integer chance = random.nextInt(1, 101);
        Integer tier = 0;
        // Tier 1 – 52%
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

    public Integer getCalculatedRarity() {
        if (!REAL_RARITY_AND_TIER_RATE) {
            return 1;
        }
        Random random = new Random();
        Integer chance = random.nextInt(1, 101);
        Integer rarity = 0;
        // Rarity 1 – 52%
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
        if (!canOpenAValidItemBooster(campaignId, userId, IntentionTowardsBooster.OPEN_BOOSTER)) {
            throw new InvalidBooster("The attempt to open an item booster is not valid");
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

        // Gets three items
        for (int i = 0; i < 3; i++) {
            switch (getRandomItemType()) {
                case ARMORS -> {
                    ArmorTemplate armorTemplate = armorService.getRandomArmorTemplateForItemBooster(campaignId, userId,
                            getCalculatedRarity(), getCalculatedTier());
                    if (IMAGE_GENERATION_ACTIVATED && armorTemplate.getImgBytes() == null) {
                        // Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateArmorTemplateImageToBytes(campaign,
                                armorTemplate);
                        armorTemplate.setImgBytes(generatedImage);
                    }
                    armorService.saveArmor(armorTemplate);
                    armorTemplates.add(armorTemplate);
                }
                case BOOTS -> {
                    BootsTemplate bootTemplate = bootsService.getRandomBootTemplateForItemBooster(campaignId, userId,
                            getCalculatedRarity(), getCalculatedTier());
                    if (IMAGE_GENERATION_ACTIVATED && bootTemplate.getImgBytes() == null) {
                        // Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateBootsTemplateImageToBytes(campaign,
                                bootTemplate);
                        bootTemplate.setImgBytes(generatedImage);
                    }
                    bootsService.saveBoots(bootTemplate);
                    bootsTemplates.add(bootTemplate);
                }
                case CONSUMABLES -> {
                    ConsumableTemplate consumableTemplate = consumableService.getRandomConsumableTemplateForItemBooster(
                            campaignId, userId, getCalculatedRarity(), getCalculatedTier());
                    if (IMAGE_GENERATION_ACTIVATED && consumableTemplate.getImgBytes() == null) {
                        // Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateConsumableTemplateImageToBytes(campaign,
                                consumableTemplate);
                        consumableTemplate.setImgBytes(generatedImage);
                    }
                    consumableService.saveConsumable(consumableTemplate);
                    consumableTemplates.add(consumableTemplate);
                }
                case HELMETS -> {
                    HelmetTemplate helmetTemplate = helmetService.getRandomHelmetTemplateForItemBooster(campaignId,
                            userId, getCalculatedRarity(), getCalculatedTier());
                    if (IMAGE_GENERATION_ACTIVATED && helmetTemplate.getImgBytes() == null) {
                        // Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateHelmetTemplateImageToBytes(campaign,
                                helmetTemplate);
                        helmetTemplate.setImgBytes(generatedImage);
                    }
                    helmetService.saveHelmet(helmetTemplate);
                    helmetTemplates.add(helmetTemplate);
                }
                case SHIELDS -> {
                    ShieldTemplate shieldTemplate = shieldService.getRandomShieldTemplateForItemBooster(campaignId,
                            userId, getCalculatedRarity(), getCalculatedTier());
                    if (IMAGE_GENERATION_ACTIVATED && shieldTemplate.getImgBytes() == null) {
                        // Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateShieldTemplateImageToBytes(campaign,
                                shieldTemplate);
                        shieldTemplate.setImgBytes(generatedImage);
                    }
                    shieldService.saveShield(shieldTemplate);
                    shieldTemplates.add(shieldTemplate);
                }
                case SPELLS -> {
                    SpellTemplate spellTemplate = spellService.getRandomSpellTemplateForItemBooster(campaignId, userId,
                            getCalculatedRarity(), getCalculatedTier());
                    if (IMAGE_GENERATION_ACTIVATED && spellTemplate.getImgBytes() == null) {
                        // Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateSpellTemplateImageToBytes(campaign,
                                spellTemplate);
                        spellTemplate.setImgBytes(generatedImage);
                    }
                    spellService.saveSpell(spellTemplate);
                    spellTemplates.add(spellTemplate);
                }
                case WEAPONS -> {
                    WeaponTemplate weaponTemplate = weaponService.getRandomWeaponTemplateForItemBooster(campaignId,
                            userId, getCalculatedRarity(), getCalculatedTier());
                    if (IMAGE_GENERATION_ACTIVATED && weaponTemplate.getImgBytes() == null) {
                        // Image for this card does not exist, so we have to generate it
                        byte[] generatedImage = runwareService.generateWeaponTemplateImageToBytes(campaign,
                                weaponTemplate);
                        weaponTemplate.setImgBytes(generatedImage);
                    }
                    weaponService.saveWeapon(weaponTemplate);
                    weaponTemplates.add(weaponTemplate);
                }
            }
        }

        CharacterInstance hero = characterService.getHero(campaignId, userId);
        Inventory heroInventory = hero.getInventory();
        heroInventory.getArmors().addAll(armorService.instancesFromArmorTemplates(armorTemplates, heroInventory));
        heroInventory.getBoots().addAll(bootsService.instancesFromBootsTemplates(bootsTemplates, heroInventory));
        heroInventory.getConsumables()
                .addAll(consumableService.instancesFromConsumableTemplates(consumableTemplates, heroInventory));
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

    public Boolean doesCharacterGenerateThisItem() {
        Random randomChance = new Random();
        return randomChance.nextInt(1, 101) >= 51;
    }

    @Transactional
    public CharacterBooster getNewCharacterBooster(Long campaignId, String userId) {
        if (!canOpenAValidCharacterBooster(campaignId, userId, IntentionTowardsBooster.OPEN_BOOSTER)) {
            throw new InvalidBooster("The attempt to open an character booster is not valid");
        }
        Campaign campaign = campaignService.getCampaignByIdAndUserId(userId, campaignId);
        CharacterBooster newBooster = new CharacterBooster();
        List<CharacterInstance> characterInstances = new ArrayList<>();

        // Gets one character
        for (int i = 0; i < 1; i++) {
            CharacterInstance characterInstance = characterService.getRandomCharacterInstanceForCharacterBooster(
                    campaignId, userId, getCalculatedRarity(), getCalculatedTier());
            Inventory characterInventory = characterInstance.getInventory();
            characterInventory.setArmors(new ArrayList<>());
            characterInventory.setHelmets(new ArrayList<>());
            characterInventory.setShields(new ArrayList<>());
            characterInventory.setSpells(new ArrayList<>());
            characterInventory.setWeapons(new ArrayList<>());
            characterInventory.setBoots(new ArrayList<>());
            characterInventory.setConsumables(new ArrayList<>());
            if (doesCharacterGenerateThisItem()) {
                ArmorTemplate armorTemplate = armorService.getRandomArmorByTierAndRarityAndCampaignAndUserId(
                        characterInstance.getTier(), characterInstance.getRarity(), campaignId, userId);
                if (armorTemplate != null) {
                    ArmorInstance armorToEquip = armorService.instanceFromArmorTemplate(armorTemplate,
                            characterInventory);
                    if (armorToEquip != null) {
                        characterInventory.getArmors().add(armorToEquip);
                        armorToEquip.equip();
                    }
                }
            }
            if (doesCharacterGenerateThisItem()) {
                BootsTemplate bootsTemplate = bootsService.getRandomBootsByTierAndRarityAndCampaignAndUserId(
                        characterInstance.getTier(), characterInstance.getRarity(), campaignId, userId);
                if (bootsTemplate != null) {
                    BootsInstance bootsToEquip = bootsService.instanceFromBootsTemplate(bootsTemplate,
                            characterInventory);
                    if (bootsToEquip != null) {
                        characterInventory.getBoots().add(bootsToEquip);
                        bootsToEquip.equip();
                    }
                }
            }
            if (doesCharacterGenerateThisItem()) {
                HelmetTemplate helmetTemplate = helmetService.getRandomHelmetByTierAndRarityAndCampaignAndUserId(
                        characterInstance.getTier(), characterInstance.getRarity(), campaignId, userId);
                if (helmetTemplate != null) {
                    HelmetInstance helmetToEquip = helmetService.instanceFromHelmetTemplate(helmetTemplate,
                            characterInventory);
                    if (helmetToEquip != null) {
                        characterInventory.getHelmets().add(helmetToEquip);
                        helmetToEquip.equip();
                    }
                }
            }
            if (doesCharacterGenerateThisItem()) {
                ShieldTemplate shieldTemplate = shieldService.getRandomShieldByTierAndRarityAndCampaignAndUserId(
                        characterInstance.getTier(), characterInstance.getRarity(), campaignId, userId);
                if (shieldTemplate != null) {
                    ShieldInstance shieldToEquip = shieldService.instanceFromShieldTemplate(shieldTemplate,
                            characterInventory);
                    if (shieldToEquip != null) {
                        characterInventory.getShields().add(shieldToEquip);
                        shieldToEquip.equip();
                    }
                }
            }
            if (IMAGE_GENERATION_ACTIVATED && characterInstance.getImgBytes() == null) {
                // Image for this card does not exist, so we have to generate it
                byte[] generatedImage = runwareService.generateCharacterInstanceImageToBytes(campaign,
                        characterInstance);
                characterInstance.setImgBytes(generatedImage);
            }
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

    public void discoverContentOfItemBooster(ItemsBooster booster) {
        List<ArmorTemplate> armorTemplates = booster.getArmors();
        List<BootsTemplate> bootsTemplates = booster.getBoots();
        List<ConsumableTemplate> consumableTemplates = booster.getConsumables();
        List<HelmetTemplate> helmetTemplates = booster.getHelmets();
        List<ShieldTemplate> shieldTemplates = booster.getShields();
        List<SpellTemplate> spellTemplates = booster.getSpells();
        List<WeaponTemplate> weaponTemplates = booster.getWeapons();

        armorTemplates.forEach(armorTemplate -> {
            if (!armorTemplate.getDiscovered()) {
                armorTemplate.setDiscovered(true);
                armorService.saveArmor(armorTemplate);
                log.info(String.format("%s discovered and persisted", armorTemplate.getName()));
            }
        });

        bootsTemplates.forEach(bootsTemplate -> {
            if (!bootsTemplate.getDiscovered()) {
                bootsTemplate.setDiscovered(true);
                bootsService.saveBoots(bootsTemplate);
                log.info(String.format("%s discovered and persisted", bootsTemplate.getName()));
            }
        });

        consumableTemplates.forEach(consumableTemplate -> {
            if (!consumableTemplate.getDiscovered()) {
                consumableTemplate.setDiscovered(true);
                consumableService.saveConsumable(consumableTemplate);
                log.info(String.format("%s discovered and persisted", consumableTemplate.getName()));
            }
        });

        helmetTemplates.forEach(helmetTemplate -> {
            if (!helmetTemplate.getDiscovered()) {
                helmetTemplate.setDiscovered(true);
                helmetService.saveHelmet(helmetTemplate);
                log.info(String.format("%s discovered and persisted", helmetTemplate.getName()));
            }
        });

        shieldTemplates.forEach(shieldTemplate -> {
            if (!shieldTemplate.getDiscovered()) {
                shieldTemplate.setDiscovered(true);
                shieldService.saveShield(shieldTemplate);
                log.info(String.format("%s discovered and persisted", shieldTemplate.getName()));
            }
        });

        spellTemplates.forEach(spellTemplate -> {
            if (!spellTemplate.getDiscovered()) {
                spellTemplate.setDiscovered(true);
                spellService.saveSpell(spellTemplate);
                log.info(String.format("%s discovered and persisted", spellTemplate.getName()));
            }
        });

        weaponTemplates.forEach(weaponTemplate -> {
            if (!weaponTemplate.getDiscovered()) {
                weaponTemplate.setDiscovered(true);
                weaponService.saveWeapon(weaponTemplate);
                log.info(String.format("%s discovered and persisted", weaponTemplate.getName()));
            }
        });
    }

    public void discoverContentOfCharacterBooster(CharacterBooster booster) {
        List<CharacterInstance> characterInstances = booster.getCharacters();
        characterInstances.forEach(characterInstance -> {
            if (!characterInstance.getDiscovered()) {
                characterInstance.setDiscovered(true);
                characterService.saveCharacter(characterInstance);
                log.info(String.format("%s discovered and persisted", characterInstance.getName()));
            }
        });
    }
}
