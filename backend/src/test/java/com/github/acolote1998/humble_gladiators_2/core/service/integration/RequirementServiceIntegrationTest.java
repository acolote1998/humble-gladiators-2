package com.github.acolote1998.humble_gladiators_2.core.service.integration;

import com.github.acolote1998.humble_gladiators_2.core.dto.GameCreationDtoRequest;
import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryOperator;
import com.github.acolote1998.humble_gladiators_2.core.enums.RequirementEntryType;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.model.RequirementEntry;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import com.github.acolote1998.humble_gladiators_2.core.service.GeminiService;
import com.github.acolote1998.humble_gladiators_2.core.service.RequirementService;
import com.github.acolote1998.humble_gladiators_2.core.service.RunwareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class RequirementServiceIntegrationTest {

    @MockitoBean
    private GeminiService geminiService;

    @MockitoBean
    private RunwareService runwareService;

    @Autowired
    private CampaignService campaignService;

    private Campaign campaign;
    private String userId;

    @BeforeEach
    void setup() {
        userId = "test-user";
        GameCreationDtoRequest validDto = new GameCreationDtoRequest(
                "Test Campaign",
                new GameCreationDtoRequest.ThemeDtoRequest(
                        new ArrayList<>(List.of("fantasy", "medieval")),
                        new ArrayList<>(List.of("modern"))
                )
        );
        campaign = campaignService.createCampaign(validDto, userId);
    }

    @Test
    void mapRequirementFromGeminiItemDto_withValidRequirement_mapsCorrectly() {
        // Create a DTO with a requirement
        ItemFromGeminiDto.RequirementDto.RequirementEntry entryDto = 
            new ItemFromGeminiDto.RequirementDto.RequirementEntry(
                "LEVEL", 
                "MOREOREQUALTHAN", 
                "5", 
                campaign.getId()
            );
        
        ItemFromGeminiDto.RequirementDto requirementDto = 
            new ItemFromGeminiDto.RequirementDto(
                campaign.getId(),
                List.of(entryDto)
            );
        
        ItemFromGeminiDto itemDto = new ItemFromGeminiDto(
            "Test Item",
            "Test Description",
            1,
            1,
            100,
            false,
            0,
            false,
            null,
            requirementDto,
            "SWORD",
            null, null, null, null, null, null
        );

        Requirement requirement = RequirementService.mapRequirementFromGeminiItemDto(itemDto, campaign);

        assertThat(requirement).isNotNull();
        assertThat(requirement.getCampaign()).isEqualTo(campaign);
        assertThat(requirement.getRequirements()).hasSize(1);
        
        RequirementEntry entry = requirement.getRequirements().get(0);
        assertThat(entry.getCampaign()).isEqualTo(campaign);
        assertThat(entry.getRequirement()).isEqualTo(requirement);
        assertThat(entry.getRequirementType()).isEqualTo(RequirementEntryType.LEVEL);
        assertThat(entry.getOperator()).isEqualTo(RequirementEntryOperator.MOREOREQUALTHAN);
        assertThat(entry.getValue()).isEqualTo("5");
    }

    @Test
    void mapRequirementFromGeminiItemDto_withNullRequirement_returnsRequirementWithEmptyEntries() {
        ItemFromGeminiDto itemDto = new ItemFromGeminiDto(
            "Test Item",
            "Test Description",
            1,
            1,
            100,
            false,
            0,
            false,
            null,
            null,
            "SWORD",
            null, null, null, null, null, null
        );

        Requirement requirement = RequirementService.mapRequirementFromGeminiItemDto(itemDto, campaign);

        assertThat(requirement).isNotNull();
        assertThat(requirement.getCampaign()).isEqualTo(campaign);
        assertThat(requirement.getRequirements()).isEmpty();
    }

    @Test
    void mapRequirementFromGeminiItemDto_withEmptyRequirementEntries_returnsRequirementWithEmptyEntries() {
        ItemFromGeminiDto.RequirementDto requirementDto = 
            new ItemFromGeminiDto.RequirementDto(
                campaign.getId(),
                new ArrayList<>()
            );
        
        ItemFromGeminiDto itemDto = new ItemFromGeminiDto(
            "Test Item",
            "Test Description",
            1,
            1,
            100,
            false,
            0,
            false,
            null,
            requirementDto,
            "SWORD",
            null, null, null, null, null, null
        );

        Requirement requirement = RequirementService.mapRequirementFromGeminiItemDto(itemDto, campaign);

        assertThat(requirement).isNotNull();
        assertThat(requirement.getCampaign()).isEqualTo(campaign);
        assertThat(requirement.getRequirements()).isEmpty();
    }

    @Test
    void mapRequirementFromGeminiItemDto_withComplexRequirementChains_mapsAllEntries() {
        // Create a requirement with multiple entries
        List<ItemFromGeminiDto.RequirementDto.RequirementEntry> entries = List.of(
            new ItemFromGeminiDto.RequirementDto.RequirementEntry(
                "LEVEL", 
                "MOREOREQUALTHAN", 
                "5", 
                campaign.getId()
            ),
            new ItemFromGeminiDto.RequirementDto.RequirementEntry(
                "STRENGTH", 
                "MOREOREQUALTHAN", 
                "10", 
                campaign.getId()
            ),
            new ItemFromGeminiDto.RequirementDto.RequirementEntry(
                "INTELLIGENCE", 
                "LESSOREQUALTHAN", 
                "20", 
                campaign.getId()
            )
        );
        
        ItemFromGeminiDto.RequirementDto requirementDto = 
            new ItemFromGeminiDto.RequirementDto(
                campaign.getId(),
                entries
            );
        
        ItemFromGeminiDto itemDto = new ItemFromGeminiDto(
            "Test Item",
            "Test Description",
            1,
            1,
            100,
            false,
            0,
            false,
            null,
            requirementDto,
            "SWORD",
            null, null, null, null, null, null
        );

        Requirement requirement = RequirementService.mapRequirementFromGeminiItemDto(itemDto, campaign);

        assertThat(requirement).isNotNull();
        assertThat(requirement.getRequirements()).hasSize(3);
        
        RequirementEntry entry1 = requirement.getRequirements().get(0);
        assertThat(entry1.getRequirementType()).isEqualTo(RequirementEntryType.LEVEL);
        assertThat(entry1.getOperator()).isEqualTo(RequirementEntryOperator.MOREOREQUALTHAN);
        assertThat(entry1.getValue()).isEqualTo("5");
        
        RequirementEntry entry2 = requirement.getRequirements().get(1);
        assertThat(entry2.getRequirementType()).isEqualTo(RequirementEntryType.STRENGTH);
        assertThat(entry2.getOperator()).isEqualTo(RequirementEntryOperator.MOREOREQUALTHAN);
        assertThat(entry2.getValue()).isEqualTo("10");
        
        RequirementEntry entry3 = requirement.getRequirements().get(2);
        assertThat(entry3.getRequirementType()).isEqualTo(RequirementEntryType.INTELLIGENCE);
        assertThat(entry3.getOperator()).isEqualTo(RequirementEntryOperator.LESSOREQUALTHAN);
        assertThat(entry3.getValue()).isEqualTo("20");
    }

    @Test
    void mapRequirementFromGeminiItemDto_verifiesRequirementValidation() {
        ItemFromGeminiDto.RequirementDto.RequirementEntry entryDto = 
            new ItemFromGeminiDto.RequirementDto.RequirementEntry(
                "LEVEL", 
                "MOREOREQUALTHAN", 
                "5", 
                campaign.getId()
            );
        
        ItemFromGeminiDto.RequirementDto requirementDto = 
            new ItemFromGeminiDto.RequirementDto(
                campaign.getId(),
                List.of(entryDto)
            );
        
        ItemFromGeminiDto itemDto = new ItemFromGeminiDto(
            "Test Item",
            "Test Description",
            1,
            1,
            100,
            false,
            0,
            false,
            null,
            requirementDto,
            "SWORD",
            null, null, null, null, null, null
        );

        Requirement requirement = RequirementService.mapRequirementFromGeminiItemDto(itemDto, campaign);

        // Verify the requirement is valid
        assertThat(Requirement.isValidRequirement(requirement)).isTrue();
        assertThat(requirement.getRequirements()).hasSize(1);
        assertThat(RequirementEntry.areValidRequirementEntries(requirement.getRequirements())).isTrue();
    }
}

