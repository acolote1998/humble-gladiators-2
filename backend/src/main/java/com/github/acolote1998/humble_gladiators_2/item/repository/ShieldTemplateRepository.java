package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.ShieldTemplate;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ShieldTemplateRepository extends ListCrudRepository<ShieldTemplate, Long> {
    List<ShieldTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);
}
