package com.github.acolote1998.humble_gladiators_2.core.model;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Turn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Battle battle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne
    private CharacterInstance performingCharacter;

    @ManyToOne
    private CharacterInstance targetCharacter;

    @Embedded
    private Action action;

    @CreationTimestamp
    private LocalDateTime createdAt; // Auto-managed by JPA
    @UpdateTimestamp
    private LocalDateTime updatedAt; // Auto-managed by JPA
}
