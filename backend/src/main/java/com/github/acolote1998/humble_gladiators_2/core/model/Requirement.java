package com.github.acolote1998.humble_gladiators_2.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requirements")
@Getter
@Setter
@Slf4j
public class Requirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL)
    private List<RequirementEntry> requirements = new ArrayList<>();

    @CreationTimestamp
    LocalDateTime createdAt; // Auto-managed by JPA
    @UpdateTimestamp
    LocalDateTime updatedAt; // Auto-managed by JPA

    @Override
    public String toString() {
        return "Requirement{" +
                "requirements=" + requirements +
                ", campaign_id=" + "the provided campaign id" +
                '}';
    }

    public static String requirementStructure(Long campaignId) {
        return String.format("""
                Requirement{
                Long campaign_id (%s)
                RequirementEntry requirements : RequirementEntry[]
                }""", campaignId.toString());
    }
}
