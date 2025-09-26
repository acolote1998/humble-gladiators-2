package com.github.acolote1998.humble_gladiators_2.core.model;

import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryOperator;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryType;
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
@Entity
@Table(name = "requirement_entries")
public class RequirementEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequirementEntryType requirementType;

    @Enumerated(EnumType.STRING)
    private RequirementEntryOperator operator;

    private String value;

    // Examples:
    // {requirementType: LEVEL, operator: MOREOREQUALTHAN, value: "10"} -> character.level >= 10
    // {requirementType: CLASSID, operator: IN, value: "WARRIOR,MAGE"} -> character.class IN (WARRIOR, MAGE)
    // {requirementType: QUEST, operator: EQUALTHAN, value: "DragonSlayer"} -> character.completedQuests.contains("DragonSlayer")


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "requirements_id")
    Requirement requirement;

    @CreationTimestamp
    LocalDateTime createdAt; // Auto-managed by JPA
    @UpdateTimestamp
    LocalDateTime updatedAt; // Auto-managed by JPA


    @Override
    public String toString() {
        return "RequirementEntry{" +
                "requirementType=" + requirementType +
                ", operator=" + operator +
                ", value='" + value + '\'' +
                '}';
    }
}
