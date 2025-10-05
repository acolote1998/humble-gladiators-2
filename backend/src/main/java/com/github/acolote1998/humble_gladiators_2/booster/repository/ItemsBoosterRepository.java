package com.github.acolote1998.humble_gladiators_2.booster.repository;

import com.github.acolote1998.humble_gladiators_2.booster.model.ItemsBooster;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ItemsBoosterRepository extends ListCrudRepository<ItemsBooster, Long> {
    @Query(value = "SELECT * FROM items_booster " +
            "WHERE campaign_id = :campaignId " +
            "AND user_id = :userId " +
            "AND DATE(updated_at) = :today " +
            "LIMIT 1", nativeQuery = true)
    ItemsBooster findByCampaignIdAndUserIdAndUpdatedAtDate(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId,
            @Param("today") LocalDate today);

}
