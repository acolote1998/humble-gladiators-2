package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.BootsTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BootsTemplateRepository extends ListCrudRepository<BootsTemplate, Long> {
    List<BootsTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(
            value = """
                      SELECT bt.* 
                      FROM boots_template bt
                      JOIN campaign c ON bt.campaign_id = c.id
                      WHERE c.id = :campaignId
                        AND c.user_id = :userId
                      ORDER BY RANDOM() 
                      LIMIT 1
                    """,
            nativeQuery = true
    )
    BootsTemplate findRandomByCampaignIdAndUserId(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId
    );


}
