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
                      ORDER BY RANDOM() 
                      LIMIT 1
                    """,
            nativeQuery = true
    )
    ShieldTemplate findRandomByCampaignIdAndUserId(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId
    );

}
