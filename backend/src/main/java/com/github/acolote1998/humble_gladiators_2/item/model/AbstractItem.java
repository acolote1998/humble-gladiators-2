package com.github.acolote1998.humble_gladiators_2.item.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer rarity;
    private Integer tier;
    private Integer value;
    private Boolean discovered;
    private Integer quantity;
    private Boolean equipped;
}
