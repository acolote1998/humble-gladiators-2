package com.github.acolote1998.humble_gladiators_2.characters.repository;

import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import org.springframework.data.repository.ListCrudRepository;

public interface CharacterInstanceRepository extends ListCrudRepository<CharacterInstance, Long> {
}
