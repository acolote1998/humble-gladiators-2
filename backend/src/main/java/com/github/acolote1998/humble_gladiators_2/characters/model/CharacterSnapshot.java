package com.github.acolote1998.humble_gladiators_2.characters.model;

import jakarta.persistence.Entity;

@Entity
public class CharacterSnapshot extends AbstractCharacter {
    private Boolean discovered;
    private Integer tier;
    private Integer rarity;
}
