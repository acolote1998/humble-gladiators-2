package com.github.acolote1998.humble_gladiators_2.core.controller;

import com.github.acolote1998.humble_gladiators_2.core.dto.CardBackResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/api/campaign")
public class BattleController {

    @GetMapping("/{campaignId}/")
    public ResponseEntity<CardBackResponseDto> getCardBackForCampaignByUserAndId(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        byte[] cardBackImg = campaignService.getBackCardImgForCampaignAndUser(userId, campaignId);
        CardBackResponseDto dto = new CardBackResponseDto(BytesToBase64.bytesToBase64(cardBackImg));
        return ResponseEntity.ok(dto);
    }
}
