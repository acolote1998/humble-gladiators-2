package com.github.acolote1998.humble_gladiators_2.booster.model;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Slf4j
public class CharacterBooster extends AbstractBooster {

    @ManyToMany
    private List<CharacterInstance> characters = new ArrayList<>();
}
