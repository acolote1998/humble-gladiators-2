package com.github.acolote1998.humble_gladiators_2.core.exception;

public class BannedUser extends RuntimeException {
    public BannedUser(String message) {
        super(message);
    }
}
