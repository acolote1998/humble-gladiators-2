package com.github.acolote1998.humble_gladiators_2.core.repository;

import com.github.acolote1998.humble_gladiators_2.core.model.Battle;
import org.springframework.data.repository.ListCrudRepository;

public interface BattleRepository extends ListCrudRepository<Battle, Long> {
}
