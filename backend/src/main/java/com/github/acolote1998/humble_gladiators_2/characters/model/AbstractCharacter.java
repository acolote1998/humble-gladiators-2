package com.github.acolote1998.humble_gladiators_2.characters.model;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.item.interfaces.Discoverable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Slf4j
@MappedSuperclass
public abstract class AbstractCharacter implements Discoverable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; //from Clerk

    @Embedded
    private Stats stats;

    @Embedded
    private Level level;

//  private  Campaign campaign

    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Inventory inventory;

    private CharacterType characterType;

    private Integer goldReward; //for NPC characters

    private Integer expReward; //for NPC characters

    private Boolean discovered;

    @CreationTimestamp
    private LocalDateTime createdAt; // Auto-managed by JPA
    @UpdateTimestamp
    private LocalDateTime updatedAt; // Auto-managed by JPA
}
