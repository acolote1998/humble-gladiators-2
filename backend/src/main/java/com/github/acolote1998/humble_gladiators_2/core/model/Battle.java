package com.github.acolote1998.humble_gladiators_2.core.model;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterSnapshot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Slf4j
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Turn> turns;

    @OneToMany
    private List<CharacterSnapshot> startingTeamOne;

    @OneToMany
    private List<CharacterSnapshot> startingTeamTwo;

    @OneToMany
    private List<CharacterInstance> winningTeam;

    @OneToMany
    private List<CharacterInstance> losingTeam;

    @ManyToOne
    private CharacterInstance currentCharacterToPlay;

    private boolean ongoing;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
