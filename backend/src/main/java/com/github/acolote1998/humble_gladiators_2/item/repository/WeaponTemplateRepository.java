package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeaponTemplateRepository extends ListCrudRepository<WeaponTemplate, Long> {
    List<WeaponTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(
            value = """
                      SELECT wt.* 
                      FROM weapon_template wt
                      JOIN campaign c ON wt.campaign_id = c.id
                      WHERE c.id = :campaignId
                        AND c.user_id = :userId
                      ORDER BY RANDOM() 
                      LIMIT 1
                    """,
            nativeQuery = true
    )
    WeaponTemplate findRandomByCampaignIdAndUserId(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId
    );

}
