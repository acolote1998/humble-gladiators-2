package com.github.acolote1998.humble_gladiators_2.characters.repository;

import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterType;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CharacterInstanceRepository extends ListCrudRepository<CharacterInstance, Long> {
    List<CharacterInstance> findAllByUserIdAndCampaign_Id(String userId, Long campaignId);

    CharacterInstance findFirstByCampaign_IdAndUserIdAndCharacterType(Long campaignId, String userId, CharacterType characterType);
}
