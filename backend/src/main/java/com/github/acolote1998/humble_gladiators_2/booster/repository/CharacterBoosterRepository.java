package com.github.acolote1998.humble_gladiators_2.booster.repository;

import com.github.acolote1998.humble_gladiators_2.booster.model.CharacterBooster;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface CharacterBoosterRepository extends ListCrudRepository<CharacterBooster, Long> {

    @Query(value = "SELECT * FROM character_booster " +
            "WHERE campaign_id = :campaignId " +
            "AND user_id = :userId " +
            "AND DATE(updated_at) = :today " +
            "LIMIT 1", nativeQuery = true)
    CharacterBooster findByCampaignIdAndUserIdAndUpdatedAtDate(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId,
            @Param("today") LocalDate today);
}
