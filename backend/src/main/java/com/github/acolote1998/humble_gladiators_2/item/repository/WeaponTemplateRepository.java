package com.github.acolote1998.humble_gladiators_2.item.repository;

import com.github.acolote1998.humble_gladiators_2.item.templates.WeaponTemplate;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WeaponTemplateRepository extends ListCrudRepository<WeaponTemplate, Long> {
    List<WeaponTemplate> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);
}
