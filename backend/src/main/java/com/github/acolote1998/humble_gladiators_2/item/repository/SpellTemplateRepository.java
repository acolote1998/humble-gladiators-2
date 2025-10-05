package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpellTemplateRepository extends ListCrudRepository<SpellTemplate, Long> {
    List<SpellTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(
            value = """
                      SELECT sp.* 
                      FROM spell_template sp
                      JOIN campaign c ON sp.campaign_id = c.id
                      WHERE c.id = :campaignId
                        AND c.user_id = :userId
                      ORDER BY RANDOM() 
                      LIMIT 1
                    """,
            nativeQuery = true
    )
    SpellTemplate findRandomByCampaignIdAndUserId(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId
    );

}
