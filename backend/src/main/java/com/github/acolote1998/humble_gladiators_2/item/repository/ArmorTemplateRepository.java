package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.ArmorTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ArmorTemplateRepository extends ListCrudRepository<ArmorTemplate, Long> {
    List<ArmorTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(value = "SELECT * FROM armor_template ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    ArmorTemplate findRandom();
}
