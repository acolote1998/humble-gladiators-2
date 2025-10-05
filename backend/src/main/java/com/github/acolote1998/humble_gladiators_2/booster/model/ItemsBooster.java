package com.github.acolote1998.humble_gladiators_2.booster.model;

import com.github.acolote1998.humble_gladiators_2.item.templates.*;
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
public class ItemsBooster extends AbstractBooster {

    @ManyToMany
    private List<ArmorTemplate> armors = new ArrayList<>();
    @ManyToMany
    private List<BootsTemplate> boots = new ArrayList<>();
    @ManyToMany
    private List<ConsumableTemplate> consumables = new ArrayList<>();
    @ManyToMany
    private List<HelmetTemplate> helmets = new ArrayList<>();
    @ManyToMany
    private List<ShieldTemplate> shields = new ArrayList<>();
    @ManyToMany
    private List<SpellTemplate> spells = new ArrayList<>();
    @ManyToMany
    private List<WeaponTemplate> weapons = new ArrayList<>();
}
