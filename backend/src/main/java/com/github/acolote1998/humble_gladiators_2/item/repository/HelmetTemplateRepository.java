package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HelmetTemplateRepository extends ListCrudRepository<HelmetTemplate, Long> {
    List<HelmetTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(
            value = """
                    SELECT ht.* 
                    FROM helmet_template ht
                    JOIN campaign c ON ht.campaign_id = c.id
                    WHERE c.id = :campaignId
                      AND c.user_id = :userId
                      AND ht.rarity = :rarity
                      AND ht.tier = :tier
                    ORDER BY RANDOM()
                    LIMIT 1
                    """,
            nativeQuery = true
    )
    HelmetTemplate findRandomByCampaignAndRarityAndTier(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId,
            @Param("rarity") Integer rarity,
            @Param("tier") Integer tier
    );

    List<HelmetTemplate> findAllByCampaign_Id(Long campaignId);

    List<HelmetTemplate> findAllByTierAndCampaign_Id(Integer tier, Long campaignId);
}
