package com.github.acolote1998.humble_gladiators_2.item.templates;

import com.github.acolote1998.humble_gladiators_2.item.model.AbstractItem;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Table(name = "shield_template")
@Slf4j
public class ShieldTemplate extends AbstractItem {
    private Integer physicalDefense;
    private Integer magicalDefense;
}
