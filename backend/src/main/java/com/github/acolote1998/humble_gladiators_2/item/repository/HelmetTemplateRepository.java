package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface HelmetTemplateRepository extends ListCrudRepository<HelmetTemplate, Long> {
    List<HelmetTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(value = "SELECT * FROM helmet_template ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    HelmetTemplate findRandom();
}
