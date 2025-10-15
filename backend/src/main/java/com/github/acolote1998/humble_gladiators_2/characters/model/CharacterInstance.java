package com.github.acolote1998.humble_gladiators_2.characters.model;

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
public class CharacterInstance extends AbstractCharacter implements Discoverable {

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

}
