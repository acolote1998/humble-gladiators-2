package com.github.acolote1998.humble_gladiators_2.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> wantedThemes;

    @ElementCollection
    private List<String> unwantedThemes;

    @OneToOne(mappedBy = "theme")
    Campaign campaign;

    @Override
    public String toString() {
        return "Theme{" +
                "wantedThemes=" + wantedThemes +
                ", unwantedThemes=" + unwantedThemes +
                '}';
    }
}
