package com.github.acolote1998.humble_gladiators_2.core.model;

import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryOperator;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryType;
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

    public static String RequirementEntryStructure(Long campaignId) {
        return String.format("""
                        RequirementEntry{
                        RequirementEntryType requirementType (%s)
                        RequirementEntryOperator operator (%s)
                        String value
                        Long campaign_id (%s)
                        Long requirements_id (use null, it will be assigned later on in the persistence layer);
                        
                        //Examples:
                        // {requirementType: LEVEL, operator: MOREOREQUALTHAN, value: "10"} -> character.level >= 10
                        // {requirementType: GOLD, operator: LESSOREQUALTHAN, value: "50"} -> character.gold => 50
                        }
                        """,
                RequirementEntryType.AllRequirementEntryTypeToString(),
                RequirementEntryOperator.AllRequirementEntryOperatorToString(),
                campaignId.toString()
        );
    }

    public static List<RequirementEntry> cloneRequirementEntries(List<RequirementEntry> originalEntries) {
        List<RequirementEntry> clonedEntries = new ArrayList<>();
        originalEntries.forEach(requirementEntry -> {
            RequirementEntry newEntry = new RequirementEntry();
            newEntry.setRequirementType(requirementEntry.getRequirementType());
            newEntry.setOperator(requirementEntry.getOperator());
            newEntry.setValue(requirementEntry.getValue());
            newEntry.setCampaign(requirementEntry.getCampaign());
        });
        return clonedEntries;
    }

    public static boolean isValidRequirementEntry(RequirementEntry requirementEntry) {
        if (requirementEntry != null) {
            if (requirementEntry.getRequirementType() == null) {
                log.warn("requirementEntry is null");
                return false;
            }
            try {
                RequirementEntryType.valueOf(String.valueOf(requirementEntry.getRequirementType()));
            } catch (Exception e) {
                log.warn("RequirementEntryType not valid");
                return false;
            }
            try {
                RequirementEntryOperator.valueOf(String.valueOf(requirementEntry.getOperator()));
            } catch (Exception e) {
                log.warn("requirement entry value not valid");
                return false;
            }
            if (requirementEntry.getValue() == null || requirementEntry.getValue().isBlank()) {
                log.warn("RequirementEntryOperator not valid");
                return false;
            }
        }
        return true;
    }

    public static boolean areValidRequirementEntries(List<RequirementEntry> requirementEntries) {
        for (RequirementEntry entry : requirementEntries) {
            if (!RequirementEntry.isValidRequirementEntry(entry)) {
                return false;
            }
        }
        return true;
    }
}
