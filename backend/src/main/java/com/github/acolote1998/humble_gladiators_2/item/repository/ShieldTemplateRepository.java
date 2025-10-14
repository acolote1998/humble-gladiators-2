package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.ShieldTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShieldTemplateRepository extends ListCrudRepository<ShieldTemplate, Long> {
    List<ShieldTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(
            value = """
                    SELECT st.* 
                    FROM shield_template st
                    JOIN campaign c ON st.campaign_id = c.id
                    WHERE c.id = :campaignId
                      AND c.user_id = :userId
                      AND st.rarity = :rarity
                      AND st.tier = :tier
                    ORDER BY RANDOM()
                    LIMIT 1
                    """,
            nativeQuery = true
    )
    ShieldTemplate findRandomByCampaignAndRarityAndTier(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId,
            @Param("rarity") Integer rarity,
            @Param("tier") Integer tier
    );

    List<ShieldTemplate> findAllByCampaign_Id(Long campaignId);

    List<ShieldTemplate> findAllByTierAndCampaign_Id(Integer tier, Long campaignId);
}
