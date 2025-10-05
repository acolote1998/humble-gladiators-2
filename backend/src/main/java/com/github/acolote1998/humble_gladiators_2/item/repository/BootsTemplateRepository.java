package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.BootsTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface BootsTemplateRepository extends ListCrudRepository<BootsTemplate, Long> {
    List<BootsTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    @Query(value = "SELECT * FROM boots_template ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    BootsTemplate findRandom();
}
