package com.github.acolote1998.humble_gladiators_2.item.model;

import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import jakarta.persistence.*;
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
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;
}
