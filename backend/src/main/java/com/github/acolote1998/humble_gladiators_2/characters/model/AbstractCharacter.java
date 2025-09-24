package com.github.acolote1998.humble_gladiators_2.characters.model;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.item.instances.SpellInstance;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Slf4j
@MappedSuperclass
public abstract class AbstractCharacter implements Discoverable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; //from Clerk

//  private  Campaign campaign

// private   Level level

    private String name;

    private Integer height;

    private Integer weight;

// private   CharClass charClass;

// private    CharRace charRace;

    private Integer constitution;

    private Integer intelligence;

    private Integer dexterity;

    private Integer strength;

    private Integer speed;

    private Integer luck;

    private Integer maxHp;

    private Integer currentHp;

//  private  List<Battle> victories;

//  private  List<Battle> defeats;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Inventory inventory;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SpellInstance> spells;

    private CharacterType characterType;

    private Integer goldReward; //for NPC characters

    private Integer expReward; //for NPC characters

    private Boolean discovered;

    @CreationTimestamp
    private LocalDateTime createdAt; // Auto-managed by JPA
    @UpdateTimestamp
    private LocalDateTime updatedAt; // Auto-managed by JPA
}
