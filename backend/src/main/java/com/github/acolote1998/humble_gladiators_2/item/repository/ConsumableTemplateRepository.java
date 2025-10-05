package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.ConsumableTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsumableTemplateRepository extends ListCrudRepository<ConsumableTemplate, Long> {
    List<ConsumableTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(
            value = """
                      SELECT ct.* 
                      FROM consumable_template ct
                      JOIN campaign c ON ct.campaign_id = c.id
                      WHERE c.id = :campaignId
                        AND c.user_id = :userId
                      ORDER BY RANDOM() 
                      LIMIT 1
                    """,
            nativeQuery = true
    )
    ConsumableTemplate findRandomByCampaignIdAndUserId(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId
    );

}
