package com.github.acolote1998.humble_gladiators_2.core.repository;

import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends ListCrudRepository<Campaign, Long> {
    List<Campaign> getCampaignsByUserId(String userId);

    List<Campaign> findAllByUserId(String userId);

    Campaign findByUserId(String userId);

    Campaign findByUserIdAndId(String userId, Long id);
}
