package com.github.acolote1998.humble_gladiators_2.core.dto;

import java.time.LocalDateTime;

public record ResponseBannedUser(String userId, Long campaignId, Boolean banned, LocalDateTime bannedUntil,
                                 Integer amountOfInvalidRequests) {
}
