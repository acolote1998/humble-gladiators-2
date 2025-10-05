package com.github.acolote1998.humble_gladiators_2.booster.repository;

import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDate;

public interface ItemsBoosterRepository extends ListCrudRepository<ItemsBooster, Long> {
    ItemsBooster findByUpdatedAt_Date(LocalDate updatedAtDate);

    ItemsBooster findByUpdatedAt_DateAndCampaign_IdAndUserId(LocalDate updatedAtDate, Long campaignId, String userId);
}
