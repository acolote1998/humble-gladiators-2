package com.github.acolote1998.humble_gladiators_2;

import com.github.acolote1998.humble_gladiators_2.characters.exception.DailyEnemyNotFound;
import com.github.acolote1998.humble_gladiators_2.characters.exception.HeroDoesNotExist;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidAttemptBattleOngoing;
import com.github.acolote1998.humble_gladiators_2.core.exception.InvalidTurn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HeroDoesNotExist.class)
    public ResponseEntity<String> handleHeroHasNotBeenCreated(HeroDoesNotExist ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DailyEnemyNotFound.class)
    public ResponseEntity<String> handleDailyEnemyNotFound() {
        return ResponseEntity.ok("Daily enemy not found");
    }

    @ExceptionHandler(InvalidAttemptBattleOngoing.class)
    public ResponseEntity<String> handleInvalidAttemptDueToBattleOngoing(InvalidAttemptBattleOngoing ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(ex.getMessage());
    }
}
