package com.github.acolote1998.humble_gladiators_2.characters.service;

import com.github.acolote1998.humble_gladiators_2.characters.dto.CreateHeroRequestDto;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.characters.exception.DailyEnemyNotFound;
import com.github.acolote1998.humble_gladiators_2.characters.exception.HeroAlreadyCreated;
import com.github.acolote1998.humble_gladiators_2.characters.exception.HeroDoesNotExist;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.characters.repository.CharacterInstanceRepository;
import com.github.acolote1998.humble_gladiators_2.core.dto.CharacterFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidAttemptBattleOngoing;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.BattleService;
import com.github.acolote1998.humble_gladiators_2.core.service.BattleUtil;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.instances.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class CharacterService {
    GeminiService geminiService;
    CharacterInstanceRepository characterInstanceRepository;
    BattleUtil battleUtil;

    @Value("${SKIP_REQUIREMENTS}")
    private boolean SKIP_REQUIREMENTS;

    public CharacterService(GeminiService geminiService,
                            CharacterInstanceRepository characterInstanceRepository,
                            BattleUtil battleUtil) {
        this.geminiService = geminiService;
        this.characterInstanceRepository = characterInstanceRepository;
        this.battleUtil = battleUtil;
    }

    public ArmorInstance equipArmor(CharacterInstance hero, Long armorToEquipId, String userId) {
        if (battleUtil.isThereOngoingBattleForToday(hero.getCampaign().getId(), userId)) {
            log.warn("Character '{} {}' is trying to equip an armor there is a battle ongoing so they cannot"
                    , hero.getId(), hero.getName());
            throw new InvalidAttemptBattleOngoing("Trying to equip an armor but there is a battle ongoing");
        }
        ArmorInstance armorToEquip = hero.getInventory()
                .getArmors()
                .stream()
                .filter(
                        armorInstance -> Objects.equals(armorInstance.getId(), armorToEquipId))
                .findFirst()
                .orElseThrow();
        ArmorInstance alreadyEquippedArmor = getEquippedArmorForAHero(hero);
        if (alreadyEquippedArmor != null) {
            alreadyEquippedArmor.setEquipped(false);
            log.info("Hero {} already had '{} - {}' equipped. Unequipping it", hero.getName(),
                    alreadyEquippedArmor.getName(), alreadyEquippedArmor.getId());
        } else {
            log.info("Hero {} did not have any armor equipped", hero.getName());
        }
        if (!SKIP_REQUIREMENTS) {
            // HERE IN THE FUTURE CAN DO VALIDATIONS TO SEE IF THE HERO MEETS THE
            // REQUIREMENTS TO EQUIP / USE THE ITEM
        }
        armorToEquip.equip();
        saveCharacter(hero);
        log.info("Equipping armor '{}' to hero '{}'", armorToEquip.getName(), hero.getName());
        return armorToEquip;
    }

    public ArmorInstance getEquippedArmorForAHero(CharacterInstance hero) {
        ArmorInstance equippedArmor = hero
                .getInventory()
                .getArmors()
                .stream()
                .filter(
                        armorInstance -> armorInstance
                                .getEquipped())
                .findFirst()
                .orElse(null);
        return equippedArmor;

    }

    public BootsInstance equipBoots(CharacterInstance hero, Long bootsToEquipId, String userId) {
        if (battleUtil.isThereOngoingBattleForToday(hero.getCampaign().getId(), userId)) {
            log.warn("Character '{} {}' is trying to equip boots but there is a battle ongoing so they cannot"
                    , hero.getId(), hero.getName());
            throw new InvalidAttemptBattleOngoing("Trying to equip boots but there is a battle ongoing");
        }
        BootsInstance bootsToEquip = hero.getInventory()
                .getBoots()
                .stream()
                .filter(
                        bootsInstance -> Objects.equals(bootsInstance.getId(), bootsToEquipId))
                .findFirst()
                .orElseThrow();
        BootsInstance alreadyEquippedBoots = getEquippedBootsForAHero(hero);
        if (alreadyEquippedBoots != null) {
            alreadyEquippedBoots.setEquipped(false);
            log.info("Hero {} already had '{} - {}' equipped. Unequipping it", hero.getName(),
                    alreadyEquippedBoots.getName(), alreadyEquippedBoots.getId());
        } else {
            log.info("Hero {} did not have any boots equipped", hero.getName());
        }
        if (!SKIP_REQUIREMENTS) {
            // HERE IN THE FUTURE CAN DO VALIDATIONS TO SEE IF THE HERO MEETS THE
            // REQUIREMENTS TO EQUIP / USE THE ITEM
        }
        bootsToEquip.equip();
        saveCharacter(hero);
        log.info("Equipping boots '{}' to hero '{}'", bootsToEquip.getName(), hero.getName());
        return bootsToEquip;
    }

    public BootsInstance getEquippedBootsForAHero(CharacterInstance hero) {
        BootsInstance equippedBoots = hero
                .getInventory()
                .getBoots()
                .stream()
                .filter(
                        bootsInstance -> bootsInstance
                                .getEquipped())
                .findFirst()
                .orElse(null);
        return equippedBoots;

    }

    public HelmetInstance equipHelmet(CharacterInstance hero, Long helmetToEquipId, String userId) {
        if (battleUtil.isThereOngoingBattleForToday(hero.getCampaign().getId(), userId)) {
            log.warn("Character '{} {}' is trying to equip a helmet but there is a battle ongoing so they cannot"
                    , hero.getId(), hero.getName());
            throw new InvalidAttemptBattleOngoing("Trying to equip a helmet but there is a battle ongoing");
        }
        HelmetInstance helmetToEquip = hero.getInventory()
                .getHelmets()
                .stream()
                .filter(
                        helmetInstance -> Objects.equals(helmetInstance.getId(), helmetToEquipId))
                .findFirst()
                .orElseThrow();
        HelmetInstance alreadyEquippedHelmet = getEquippedHelmetForAHero(hero);
        if (alreadyEquippedHelmet != null) {
            alreadyEquippedHelmet.setEquipped(false);
            log.info("Hero {} already had '{} - {}' equipped. Unequipping it", hero.getName(),
                    alreadyEquippedHelmet.getName(), alreadyEquippedHelmet.getId());
        } else {
            log.info("Hero {} did not have any helmet equipped", hero.getName());
        }
        if (!SKIP_REQUIREMENTS) {
            // HERE IN THE FUTURE CAN DO VALIDATIONS TO SEE IF THE HERO MEETS THE
            // REQUIREMENTS TO EQUIP / USE THE ITEM
        }
        helmetToEquip.equip();
        saveCharacter(hero);
        log.info("Equipping helmet '{}' to hero '{}'", helmetToEquip.getName(), hero.getName());
        return helmetToEquip;
    }

    public HelmetInstance getEquippedHelmetForAHero(CharacterInstance hero) {
        HelmetInstance equippedHelmet = hero
                .getInventory()
                .getHelmets()
                .stream()
                .filter(
                        helmetInstance -> helmetInstance
                                .getEquipped())
                .findFirst()
                .orElse(null);
        return equippedHelmet;

    }

    public ShieldInstance equipShield(CharacterInstance hero, Long shieldToEquipId, String userId) {
        if (battleUtil.isThereOngoingBattleForToday(hero.getCampaign().getId(), userId)) {
            log.warn("Character '{} {}' is trying to equip a shield but there is a battle ongoing so they cannot"
                    , hero.getId(), hero.getName());
            throw new InvalidAttemptBattleOngoing("Trying to equip a shield but there is a battle ongoing");
        }
        ShieldInstance shieldToEquip = hero.getInventory()
                .getShields()
                .stream()
                .filter(
                        shieldInstance -> Objects.equals(shieldInstance.getId(), shieldToEquipId))
                .findFirst()
                .orElseThrow();
        ShieldInstance alreadyEquippedShield = getEquippedShieldForAHero(hero);
        if (alreadyEquippedShield != null) {
            alreadyEquippedShield.setEquipped(false);
            log.info("Hero {} already had '{} - {}' equipped. Unequipping it", hero.getName(),
                    alreadyEquippedShield.getName(), alreadyEquippedShield.getId());
        } else {
            log.info("Hero {} did not have any shield equipped", hero.getName());
        }
        if (!SKIP_REQUIREMENTS) {
            // HERE IN THE FUTURE CAN DO VALIDATIONS TO SEE IF THE HERO MEETS THE
            // REQUIREMENTS TO EQUIP / USE THE ITEM
        }
        shieldToEquip.equip();
        saveCharacter(hero);
        log.info("Equipping shield '{}' to hero '{}'", shieldToEquip.getName(), hero.getName());
        return shieldToEquip;
    }

    public ShieldInstance getEquippedShieldForAHero(CharacterInstance hero) {
        ShieldInstance equippedShield = hero
                .getInventory()
                .getShields()
                .stream()
                .filter(
                        shieldInstance -> shieldInstance
                                .getEquipped())
                .findFirst()
                .orElse(null);
        return equippedShield;

    }

    public WeaponInstance equipWeapon(CharacterInstance hero, Long weaponToEquipId, String userId) {
        if (battleUtil.isThereOngoingBattleForToday(hero.getCampaign().getId(), userId)) {
            log.warn("Character '{} {}' is trying to equip a weapon but there is a battle ongoing so they cannot"
                    , hero.getId(), hero.getName());
            throw new InvalidAttemptBattleOngoing("Trying to equip a weapon but there is a battle ongoing");
        }
        WeaponInstance weaponToEquip = hero.getInventory()
                .getWeapons()
                .stream()
                .filter(
                        weaponInstance -> Objects.equals(weaponInstance.getId(), weaponToEquipId))
                .findFirst()
                .orElseThrow();
        WeaponInstance alreadyEquippedWeapon = getEquippedWeaponForAHero(hero);
        if (alreadyEquippedWeapon != null) {
            alreadyEquippedWeapon.setEquipped(false);
            log.info("Hero {} already had '{} - {}' equipped. Unequipping it", hero.getName(),
                    alreadyEquippedWeapon.getName(), alreadyEquippedWeapon.getId());
        } else {
            log.info("Hero {} did not have any weapon equipped", hero.getName());
        }
        if (!SKIP_REQUIREMENTS) {
            // HERE IN THE FUTURE CAN DO VALIDATIONS TO SEE IF THE HERO MEETS THE
            // REQUIREMENTS TO EQUIP / USE THE ITEM
        }
        weaponToEquip.equip();
        saveCharacter(hero);
        log.info("Equipping weapon '{}' to hero '{}'", weaponToEquip.getName(), hero.getName());
        return weaponToEquip;
    }

    public WeaponInstance getEquippedWeaponForAHero(CharacterInstance hero) {
        WeaponInstance equippedWeapon = hero
                .getInventory()
                .getWeapons()
                .stream()
                .filter(
                        weaponInstance -> weaponInstance
                                .getEquipped())
                .findFirst()
                .orElse(null);
        return equippedWeapon;

    }

    public Map<String, Object> getShortAIGeneratedReport(Long campaignId) {
        List<CharacterInstance> allCharacters = characterInstanceRepository
                .findAllByCampaign_IdAndCharacterType(campaignId, CharacterType.NPC);
        // Sort by Tier (highest first) then Rarity (highest first)
        allCharacters.sort((c1, c2) -> {
            int tierComparison = Integer.compare(c2.getTier(), c1.getTier());
            if (tierComparison != 0) {
                return tierComparison;
            }
            return Integer.compare(c2.getRarity(), c1.getRarity());
        });

        Map<String, Object> characterValues = new HashMap<>();
        Map<String, String> namesAndDescriptions = new HashMap<>();
        allCharacters.forEach(characterInstance -> {
            String name = characterInstance.getName();
            String description = "Tier: " + characterInstance.getTier() + ", Rarity: " + characterInstance.getRarity()
                    + ", Category: " + characterInstance.getCategory();
            ;
            namesAndDescriptions.put(name, description);
        });
        characterValues.put("CharacterInstances", namesAndDescriptions);
        return characterValues;
    }

    public CharacterInstance getRandomCharacterInstanceForCharacterBooster(
            Long campaignId,
            String userId,
            Integer rarity,
            Integer tier) {
        return characterInstanceRepository.findRandomByCampaignAndRarityAndTier(
                campaignId,
                userId,
                rarity,
                tier);
    }

    public Map<String, String> getTier5NpcsContextForCampaignCover(Campaign campaign) {
        List<CharacterInstance> npcs = characterInstanceRepository.findAllByTierAndCampaign_Id(5, campaign.getId());

        Map<String, String> context = new HashMap<>();
        for (CharacterInstance npc : npcs) {
            if (npc.getName() != null && !npc.getName().isBlank()) {
                context.put(
                        npc.getName(),
                        npc.getDescription() != null ? npc.getDescription() : "");
            }
        }

        return context;
    }

    public List<CharacterInstance> createTenNPCsOfDesiredTier(Campaign campaign, Integer tier) {
        List<CharacterInstance> existingCharactersForContext = characterInstanceRepository.findAll();
        List<CharacterFromGeminiDto> generatedDtos = geminiService.generateTenNpcsOfDesiredTier(campaign,
                existingCharactersForContext, tier);
        List<CharacterInstance> savedCharacterInstances = new ArrayList<>();

        generatedDtos.forEach(characterFromGeminiDto -> {
            CharacterInstance characterInstance = new CharacterInstance();
            characterInstance.setUserId(campaign.getUserId());
            characterInstance.setStats(Stats.mapStatsFromCharacterFromGeminiDto(characterFromGeminiDto));
            characterInstance.setCharacterType(characterFromGeminiDto.characterType());
            characterInstance.setCategory(characterFromGeminiDto.category());
            characterInstance.setName(characterFromGeminiDto.name());
            characterInstance.setDescription(characterFromGeminiDto.description());
            characterInstance.setDiscovered(false);
            characterInstance.setCampaign(campaign);
            characterInstance.setRarity(characterFromGeminiDto.rarity());
            characterInstance.setTier(characterFromGeminiDto.tier());
            characterInstance.setGoldReward(characterFromGeminiDto.stats().level() * 10
                    * characterFromGeminiDto.rarity() * characterFromGeminiDto.tier());
            characterInstance.setExpReward(characterFromGeminiDto.stats().level() * 20 * characterFromGeminiDto.rarity()
                    * characterFromGeminiDto.tier());
            Inventory inventory = InventoryService.createBlankInventory();
            characterInstance.setInventory(inventory);
            savedCharacterInstances.add(characterInstance);
        });

        if (!CharacterInstance.areValidCharacters(savedCharacterInstances, 10)) {
            log.warn(String.format("Campaign %s - Generated characters not valid -> Generating again",
                    campaign.getId()));
            return createTenNPCsOfDesiredTier(campaign, tier);
        }

        characterInstanceRepository.saveAll(savedCharacterInstances);
        log.info(savedCharacterInstances.size() + " characters tier " + tier + " successfully created and persisted");
        return savedCharacterInstances;
    }

    public List<CharacterInstance> getAllCharacterInstancesForACampaignAndUser(String userId, Long campaignId) {
        return characterInstanceRepository.findAllByUserIdAndCampaign_Id(userId, campaignId);
    }

    public CharacterInstance findHeroOrNull(Long campaignId, String userId) {
        return characterInstanceRepository.findFirstByCampaign_IdAndUserIdAndCharacterType(
                campaignId, userId, CharacterType.PLAYER);
    }

    public CharacterInstance createHero(Campaign campaign, String userId, CreateHeroRequestDto dto) {
        CharacterInstance doesAHeroExist = findHeroOrNull(campaign.getId(), userId);
        if (doesAHeroExist != null) {
            throw new HeroAlreadyCreated("A hero already exists for this campaign");
        }
        CharacterInstance model = new CharacterInstance();
        model.setUserId(userId);
        model.setCampaign(campaign);
        model.setName(dto.heroName());
        model.setInventory(InventoryService.createBlankInventory());
        model.setStats(Stats.createRandomInitialStats());
        model.setCharacterType(CharacterType.PLAYER);
        log.info(String.format("Hero Created - Campaign %s - %s", campaign.getId(), userId));
        return characterInstanceRepository.save(model);
    }

    public CharacterInstance getHero(Long campaignId, String userId) {
        CharacterInstance hero = characterInstanceRepository.findFirstByCampaign_IdAndUserIdAndCharacterType(campaignId,
                userId, CharacterType.PLAYER);
        if (hero != null) {
            return hero;
        } else {
            throw new HeroDoesNotExist("A hero has not been created for this campaign yet");
        }
    }

    public CharacterInstance getDailyEnemy(Long campaignId, String userId) {
        LocalDate today = LocalDate.now();
        CharacterInstance enemy = characterInstanceRepository.findEnemyByCampaignIdAndUserIdAndUpdatedAtDate(
                campaignId,
                userId,
                today,
                CharacterType.NPC.name());
        if (enemy != null) {
            return enemy;
        } else {
            throw new DailyEnemyNotFound("Enemy not found for today. Booster not opened?");
        }
    }

    public boolean doesHeroExistForACampaign(Long campaignId, String userId) {
        CharacterInstance hero = characterInstanceRepository.findFirstByCampaign_IdAndUserIdAndCharacterType(campaignId,
                userId, CharacterType.PLAYER);
        return hero != null;
    }

    public CharacterInstance getCharacterByIdAndCampaignIdAndUserId(Long characterId, Long campaignId, String userId) {
        return characterInstanceRepository.findFirstByIdAndCampaign_IdAndUserId(characterId, campaignId, userId);
    }

    public boolean isCharacterNotNull(CharacterInstance charToCheck) {
        return charToCheck != null;
    }

    public CharacterInstance saveCharacter(CharacterInstance model) {
        return characterInstanceRepository.save(model);
    }

    public boolean canTheCharacterUseConsumable(CharacterInstance characterToCheck, Long consumableToCheckId) {
        ConsumableInstance consumableToCheck = characterToCheck
                .getInventory()
                .getConsumables()
                .stream()
                .filter(
                        consumableInstance -> consumableInstance
                                .getId()
                                .equals(consumableToCheckId))
                .findFirst()
                .orElse(null);
        if (consumableToCheck == null) {
            return false;
        }
        if (consumableToCheck.getQuantity() < 1) {
            return false;
        }
        return true;
    }

    public boolean canTheCharacterUseSpell(CharacterInstance characterToCheck, Long spellToCheckId) {
        SpellInstance spellToCheck = characterToCheck
                .getInventory()
                .getSpells()
                .stream()
                .filter(
                        spellInstance -> spellInstance
                                .getId()
                                .equals(spellToCheckId))
                .findFirst()
                .orElse(null);
        if (spellToCheck == null) {
            return false;
        }
        if (spellToCheck.getQuantity() < 1) {
            return false;
        }
        if (characterToCheck.getStats().getCurrentMp() < spellToCheck.getTemplate().getMpCost()) {
            return false;
        }
        return true;
    }
}
