package com.github.acolote1998.humble_gladiators_2.characters.exception;

public class HeroDoesNotExist extends RuntimeException {
    public HeroDoesNotExist(String message) {
        super(message);
    }
}
