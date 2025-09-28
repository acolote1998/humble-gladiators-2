package com.github.acolote1998.humble_gladiators_2.characters.model;

import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CharacterInstance extends AbstractCharacter implements Discoverable {

    private Boolean discovered;
    private Integer tier;
    private Integer rarity;

    @Override
    public void discover() {

    }

    public static String ObjectStructure(Long campaignId) {
        return String.format("""
                Stats stats
                characterType (either "PLAYER" or "NPC")
                String name (character name generated based on the wanted themes)
                Boolean discovered (always false)
                Long campaign_id (%s)
                Integer rarity (1 - 5)
                Integer tier (1 - 5)
                Integer goldReward (level * 10 * rarity * tier)
                Integer expReward (level * 20 * rarity * tier)
                }""", campaignId.toString());
    }

}
