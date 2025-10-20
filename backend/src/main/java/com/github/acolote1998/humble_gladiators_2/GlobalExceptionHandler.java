package com.github.acolote1998.humble_gladiators_2;

import com.github.acolote1998.humble_gladiators_2.characters.exception.HeroDoesNotExist;
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
}
