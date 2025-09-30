package com.github.acolote1998.humble_gladiators_2.core.controller;

import com.github.acolote1998.humble_gladiators_2.core.service.CampaignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public
}
