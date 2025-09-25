package com.github.acolote1998.humble_gladiators_2.core.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Embeddable
@Getter
@Setter
public class Theme {
    private List<String> wantedThemes;
    private List<String> unwantedThemes;

    @Override
    public String toString() {
        return "Theme{" +
                "wantedThemes=" + wantedThemes +
                ", unwantedThemes=" + unwantedThemes +
                '}';
    }
}
