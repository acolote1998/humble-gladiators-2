package com.github.acolote1998.humble_gladiators_2.core.model;

import com.github.acolote1998.humble_gladiators_2.core.enums.BattleResultEnum;
import com.github.acolote1998.humble_gladiators_2.item.instances.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class BattleReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer expReward;

    private Integer goldReward;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ArmorInstance> armorLoot;

    @OneToMany(cascade = CascadeType.ALL)
    private List<BootsInstance> bootsLoot;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ConsumableInstance> consumablesLoot;

    @OneToMany(cascade = CascadeType.ALL)
    private List<HelmetInstance> helmetsLoot;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ShieldInstance> shieldsLoot;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SpellInstance> spellsLoot;

    @OneToMany(cascade = CascadeType.ALL)
    private List<WeaponInstance> weaponsLoot;

    BattleResultEnum battleResult;
}