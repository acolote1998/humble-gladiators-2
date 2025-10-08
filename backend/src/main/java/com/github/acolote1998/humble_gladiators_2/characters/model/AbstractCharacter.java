package com.github.acolote1998.humble_gladiators_2.characters.model;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterCategory;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
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
public abstract class AbstractCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; //from Clerk

    @Embedded
    private Stats stats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Lob
    byte[] imgBytes;

    private String name;
    private String description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Inventory inventory;

    @Enumerated(EnumType.STRING)
    private CharacterType characterType;

    private Integer goldReward; //for NPC characters

    private Integer expReward; //for NPC characters

    @Enumerated(EnumType.STRING)
    private CharacterCategory category;

    @CreationTimestamp
    private LocalDateTime createdAt; // Auto-managed by JPA
    @UpdateTimestamp
    private LocalDateTime updatedAt; // Auto-managed by JPA
}
