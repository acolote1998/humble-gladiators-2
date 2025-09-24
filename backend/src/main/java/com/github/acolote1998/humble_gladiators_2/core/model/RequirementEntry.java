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

    private RequirementEntryType requirementType;

    private RequirementEntryOperator operator;

    private String value;

    @ManyToOne
    @JoinColumn(name = "requirements_id")
    Requirement requirement;

    @CreationTimestamp
    LocalDateTime createdAt; // Auto-managed by JPA
    @UpdateTimestamp
    LocalDateTime updatedAt; // Auto-managed by JPA
}
