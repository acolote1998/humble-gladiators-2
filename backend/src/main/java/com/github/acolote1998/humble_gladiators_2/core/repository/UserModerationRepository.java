package com.github.acolote1998.humble_gladiators_2.core.repository;

import com.github.acolote1998.humble_gladiators_2.core.model.UserModeration;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface UserModerationRepository extends ListCrudRepository<UserModeration, Long> {

    List<UserModeration> findAllByUserIdAndBanned(String userId, Boolean banned);

    UserModeration findFirstByUserId(String userId);

    UserModeration findFirstByUserIdAndBanned(String userId, Boolean banned);
}
