package com.github.acolote1998.humble_gladiators_2.core.repository;

import com.github.acolote1998.humble_gladiators_2.booster.model.CharacterBooster;
import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BattleRepository extends ListCrudRepository<Battle, Long> {

    @Query(value = "SELECT * FROM battle " +
            "WHERE campaign_id = :campaignId " +
            "AND user_id = :userId " +
            "AND DATE(updated_at) = :today " +
            "LIMIT 1", nativeQuery = true)
    Battle findByCampaignIdAndUserIdAndUpdatedAtDate(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId,
            @Param("today") LocalDate today);

    Battle findByIdAndCampaign_IdAndUserId(Long id, Long campaignId, String userId);
}
