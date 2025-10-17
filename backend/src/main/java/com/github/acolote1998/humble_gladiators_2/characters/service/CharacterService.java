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
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.item.instances.ArmorInstance;
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

    @Value("${SKIP_REQUIREMENTS}")
    private boolean SKIP_REQUIREMENTS;

    public CharacterService(GeminiService geminiService, CharacterInstanceRepository characterInstanceRepository) {
        this.geminiService = geminiService;
        this.characterInstanceRepository = characterInstanceRepository;
    }

    public ArmorInstance equipArmor(CharacterInstance hero, Long armorToEquipId) {
        ArmorInstance armorToEquip =
                hero.getInventory()
                        .getArmors()
                        .stream()
                        .filter(
                                armorInstance ->
                                        Objects.equals(armorInstance.getId(), armorToEquipId)
                        )
                        .findFirst()
                        .orElseThrow();
        ArmorInstance alreadyEquippedArmor = getEquippedArmorForAHero(hero);
        if (alreadyEquippedArmor != null) {
            alreadyEquippedArmor.setEquipped(false);
            log.info("Hero {} already had '{} - {}' equipped. Unequipping it", hero.getName(), alreadyEquippedArmor.getName(), alreadyEquippedArmor.getId());
        } else {
            log.info("Hero {} did not have any armor equipped", hero.getName());
        }
        if (!SKIP_REQUIREMENTS) {
            // HERE IN THE FUTURE CAN DO VALIDATIONS TO SEE IF THE HERO MEETS THE REQUIREMENTS TO EQUIP / USE THE ITEM
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
                                .getEquipped()
                )
                .findFirst()
                .orElse(null);
        return equippedArmor;

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

    public CharacterInstance saveCharacter(CharacterInstance model) {
        return characterInstanceRepository.save(model);
    }
}
