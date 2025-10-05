package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.ShieldTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ShieldTemplateRepository extends ListCrudRepository<ShieldTemplate, Long> {
    List<ShieldTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(value = "SELECT * FROM shield_template ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    ShieldTemplate findRandom();
}
