package com.github.acolote1998.humble_gladiators_2.core.service;

import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryOperator;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryType;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidGeminiEnumException;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequirementServiceTest {

    private static final Long CAMPAIGN_ID = 1L;

    @Test
    void mapRequirementFromGeminiItemDto_WithRequirements_ShouldMapCorrectly() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);

        ItemFromGeminiDto.RequirementDto.RequirementEntry entryDto = 
                new ItemFromGeminiDto.RequirementDto.RequirementEntry(
                        "LEVEL", "MOREOREQUALTHAN", "5", CAMPAIGN_ID
                );
        ItemFromGeminiDto.RequirementDto requirementDto = 
                new ItemFromGeminiDto.RequirementDto(CAMPAIGN_ID, List.of(entryDto));
        
        ItemFromGeminiDto itemDto = new ItemFromGeminiDto(
                "Test Item", "Description", 1, 1, 1, true, 1, true, CAMPAIGN_ID, requirementDto, 
                "ARMOR", 1, 1, null, null, null, null
        );

        // Act
        Requirement result = RequirementService.mapRequirementFromGeminiItemDto(itemDto, campaign);

        // Assert
        assertNotNull(result);
        assertEquals(campaign, result.getCampaign());
        assertNotNull(result.getRequirements());
        assertEquals(1, result.getRequirements().size());
        assertEquals(RequirementEntryType.LEVEL, result.getRequirements().get(0).getRequirementType());
        assertEquals(RequirementEntryOperator.MOREOREQUALTHAN, result.getRequirements().get(0).getOperator());
        assertEquals("5", result.getRequirements().get(0).getValue());
    }

    @Test
    void mapRequirementFromGeminiItemDto_WithNullRequirements_ShouldReturnEmptyRequirement() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);

        ItemFromGeminiDto itemDto = new ItemFromGeminiDto(
                "Test Item", "Description", 1, 1, 1, true, 1, true, CAMPAIGN_ID, null, 
                "ARMOR", 1, 1, null, null, null, null
        );

        // Act
        Requirement result = RequirementService.mapRequirementFromGeminiItemDto(itemDto, campaign);

        // Assert
        assertNotNull(result);
        assertEquals(campaign, result.getCampaign());
        assertNotNull(result.getRequirements());
        assertTrue(result.getRequirements().isEmpty());
    }

    @Test
    void mapRequirementFromGeminiItemDto_WithEmptyRequirements_ShouldReturnEmptyRequirement() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);

        ItemFromGeminiDto.RequirementDto requirementDto = 
                new ItemFromGeminiDto.RequirementDto(CAMPAIGN_ID, Collections.emptyList());
        
        ItemFromGeminiDto itemDto = new ItemFromGeminiDto(
                "Test Item", "Description", 1, 1, 1, true, 1, true, CAMPAIGN_ID, requirementDto, 
                "ARMOR", 1, 1, null, null, null, null
        );

        // Act
        Requirement result = RequirementService.mapRequirementFromGeminiItemDto(itemDto, campaign);

        // Assert
        assertNotNull(result);
        assertEquals(campaign, result.getCampaign());
        assertNotNull(result.getRequirements());
        assertTrue(result.getRequirements().isEmpty());
    }

    @Test
    void mapRequirementFromGeminiItemDto_WithInvalidRequirementType_ShouldThrowException() {
        // Arrange
        Campaign campaign = new Campaign();
        campaign.setId(CAMPAIGN_ID);

        ItemFromGeminiDto.RequirementDto.RequirementEntry entryDto =
                new ItemFromGeminiDto.RequirementDto.RequirementEntry(
                        "MAGICALDAMAGE", "MOREOREQUALTHAN", "5", CAMPAIGN_ID
                );
        ItemFromGeminiDto.RequirementDto requirementDto =
                new ItemFromGeminiDto.RequirementDto(CAMPAIGN_ID, List.of(entryDto));

        ItemFromGeminiDto itemDto = new ItemFromGeminiDto(
                "Test Item", "Description", 1, 1, 1, true, 1, true, CAMPAIGN_ID, requirementDto,
                "ARMOR", 1, 1, null, null, null, null
        );

        // Act & Assert
        assertThrows(InvalidGeminiEnumException.class,
                () -> RequirementService.mapRequirementFromGeminiItemDto(itemDto, campaign));
    }
}

