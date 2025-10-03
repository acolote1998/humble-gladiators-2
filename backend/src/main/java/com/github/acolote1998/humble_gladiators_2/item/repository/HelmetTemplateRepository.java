package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.HelmetTemplate;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface HelmetTemplateRepository extends ListCrudRepository<HelmetTemplate, Long> {
    List<HelmetTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);
}
