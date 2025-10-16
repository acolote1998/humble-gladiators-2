package com.github.acolote1998.humble_gladiators_2.characters.repository;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CharacterInstanceRepository extends ListCrudRepository<CharacterInstance, Long> {
    List<CharacterInstance> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    CharacterInstance findFirstByCampaign_IdAndUserIdAndCharacterType(Long campaignId, String userId, CharacterType characterType);

    List<CharacterInstance> findAllByCampaign_IdAndCharacterType(Long campaignId, CharacterType characterType);

    List<CharacterInstance> findAllByTierAndCampaign_Id(Integer tier, Long campaignId);

    @Query(
            value = """
                    SELECT ci.* 
                    FROM character_instance ci
                    JOIN campaign c ON ci.campaign_id = c.id
                    WHERE c.id = :campaignId
                      AND c.user_id = :userId
                      AND ci.rarity = :rarity
                      AND ci.tier = :tier
                    ORDER BY RANDOM()
                    LIMIT 1
                    """,
            nativeQuery = true
    )
    CharacterInstance findRandomByCampaignAndRarityAndTier(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId,
            @Param("rarity") Integer rarity,
            @Param("tier") Integer tier
    );

    @Query(value = "SELECT * FROM character_instance " +
            "WHERE campaign_id = :campaignId " +
            "AND user_id = :userId " +
            "AND character_type = :characterType " +
            "AND DATE(updated_at) = :today " +
            "LIMIT 1", nativeQuery = true)
    CharacterInstance findEnemyByCampaignIdAndUserIdAndUpdatedAtDate(
            @Param("campaignId") Long campaignId,
            @Param("userId") String userId,
            @Param("today") LocalDate today,
            @Param("characterType") String characterType);
}
