package com.github.acolote1998.humble_gladiators_2.core.model;

import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
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

    public static String RequirementStructure(Long campaignId) {
        return String.format("""
                Requirement{
                Long campaign_id (%s)
                RequirementEntry[] requirements
                }""", campaignId.toString());
    }

    public static Requirement cloneRequirement(Requirement original) {
        if (original == null) return null;

        Requirement cloned = new Requirement();
        cloned.setCampaign(original.getCampaign());
        cloned.setRequirements(RequirementEntry.cloneRequirementEntries(original.getRequirements()));
        cloned.getRequirements().forEach(requirementEntry -> requirementEntry.setCampaign(original.getCampaign()));
        return cloned;
    }

    public static boolean isValidRequirement(Requirement requirement) {
        if (requirement != null) {
            if (armor == null) {
                log.warn("ArmorTemplate is null");
                return false;
            }

            if (armor.getName() == null || armor.getName().isBlank()) {
                log.warn("{} has invalid name", armor);
                return false;
            }

            if (armor.getDescription() == null || armor.getDescription().isBlank()) {
                log.warn("{} has invalid description", armor);
                return false;
            }

            if (armor.getRarity() == null || armor.getRarity() < 1 || armor.getRarity() > 5) {
                log.warn("{} has invalid rarity (expected 1–5)", armor);
                return false;
            }

            if (armor.getTier() == null || armor.getTier() < 1 || armor.getTier() > 5) {
                log.warn("{} has invalid tier (expected 1-5)", armor);
                return false;
            }

            if (armor.getValue() == null || armor.getValue() < 0) {
                log.warn("{} has invalid value", armor);
                return false;
            }

            if (armor.getQuantity() == null || armor.getQuantity() < 0 || armor.getQuantity() > 1) {
                log.warn("{} has invalid quantity", armor);
                return false;
            }

            if (armor.getUserId() == null || armor.getUserId().isBlank()) {
                log.warn("{} has invalid userId", armor);
                return false;
            }

            if (armor.getCampaign() == null) {
                log.warn("{} has no campaign assigned", armor);
                return false;
            }

            if (armor.getRequirement() == null) {
                log.warn("{} has no requirement assigned", armor);
                return false;
            }

            if (armor.getPhysicalDefense() == null || armor.getPhysicalDefense() < 0) {
                log.warn("{} has invalid physical defense", armor);
                return false;
            }
            if (armor.getMagicalDefense() == null || armor.getMagicalDefense() < 0) {
                log.warn("{} has invalid magical defense", armor);
                return false;
            }
            if (armor.getPhysicalDefense() == 0 && armor.getMagicalDefense() == 0) {
                log.warn("{} has 0 in both physical and magical defense", armor);
                return false;
            }
            return true;
        }
    }
}
