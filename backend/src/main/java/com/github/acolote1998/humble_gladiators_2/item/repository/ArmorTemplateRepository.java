package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArmorTemplateRepository extends ListCrudRepository<ArmorTemplate, Long> {
    List<ArmorTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(
            value = """
                    SELECT at.* 
                    FROM armor_template at
                    JOIN campaign c ON at.campaign_id = c.id
                    WHERE c.id = :campaignId
                      AND c.user_id = :userId
                      AND at.rarity = :rarity
                      AND at.tier = :tier
                    ORDER BY RANDOM()
                    LIMIT 1
                    """,
            nativeQuery = true
    )
    ArmorTemplate findRandomByCampaignAndRarityAndTier(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId,
            @Param("rarity") Integer rarity,
            @Param("tier") Integer tier
    );


    List<ArmorTemplate> findAllByCampaign_Id(Long campaignId);

    List<ArmorTemplate> findAllByTierAndCampaign_Id(Integer tier, Long campaignId);
}
