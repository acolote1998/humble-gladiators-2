package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.SpellTemplate;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SpellTemplateRepository extends ListCrudRepository<SpellTemplate, Long> {
    List<SpellTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);
}
