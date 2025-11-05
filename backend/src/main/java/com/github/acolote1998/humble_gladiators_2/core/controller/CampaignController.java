package com.github.acolote1998.humble_gladiators_2.core.controller;

import com.github.acolote1998.humble_gladiators_2.core.dto.CampaignResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.dto.CardBackResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import com.github.acolote1998.humble_gladiators_2.core.util.BytesToBase64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/api/campaign")
public class CampaignController {
    CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public ResponseEntity<List<CampaignResponseDto>> getAllCampaignsForAUser(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        List<Campaign> campaignsForUser = campaignService.getAllCampainsForAUser(userId);
        return ResponseEntity.ok(CampaignResponseDto.mapCampaignEntityToResponseDtos(campaignsForUser));
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<CampaignResponseDto> getCampaignByUserAndId(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        Campaign campaign = campaignService.getCampaignByIdAndUserId(userId, campaignId);
        if (campaign == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(CampaignResponseDto.fromEntityToCampaignResponseDto(campaign));
    }

    @GetMapping("/{campaignId}/card-back")
    public ResponseEntity<CardBackResponseDto> getCardBackForCampaignByUserAndId(@AuthenticationPrincipal Jwt jwt, @PathVariable Long campaignId) {
        String userId = jwt.getSubject();
        byte[] cardBackImg = campaignService.getBackCardImgForCampaignAndUser(userId, campaignId);
        CardBackResponseDto dto = new CardBackResponseDto(BytesToBase64.bytesToBase64(cardBackImg));
        return ResponseEntity.ok(dto);
    }
}
