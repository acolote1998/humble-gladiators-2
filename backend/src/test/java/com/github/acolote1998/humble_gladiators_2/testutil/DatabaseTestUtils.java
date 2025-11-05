package com.github.acolote1998.humble_gladiators_2.testutil;

import jakarta.persistence.EntityManager;

public final class DatabaseTestUtils {

    private DatabaseTestUtils() {}

    public static void flushAndClear(EntityManager entityManager) {
        entityManager.flush();
        entityManager.clear();
    }
}


