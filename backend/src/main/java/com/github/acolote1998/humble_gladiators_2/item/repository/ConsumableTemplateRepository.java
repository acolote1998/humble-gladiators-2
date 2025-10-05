package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.ConsumableTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ConsumableTemplateRepository extends ListCrudRepository<ConsumableTemplate, Long> {
    List<ConsumableTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(value = "SELECT * FROM consumable_template ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    ConsumableTemplate findRandom();
}
