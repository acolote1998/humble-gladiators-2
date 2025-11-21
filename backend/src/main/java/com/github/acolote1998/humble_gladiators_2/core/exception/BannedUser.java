package com.github.acolote1998.humble_gladiators_2.core.exception;

import com.github.acolote1998.humble_gladiators_2.core.dto.ResponseBannedUser;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import lombok.Getter;

@Getter
public class BannedUser extends RuntimeException {
    private final ResponseBannedUser responseBannedUser;

    public BannedUser(String message, Campaign campaign) {
        super(message);
        this.responseBannedUser = new ResponseBannedUser(
                campaign.getUserId(),
                campaign.getId(),
                campaign.getUserModeration().getBanned(),
                campaign.getUserModeration().getBannedUntil(),
                campaign.getUserModeration().getAmountOfInvalidRequests());
    }
}
