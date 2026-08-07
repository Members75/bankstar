package com.bankstar.recommendation.controller;

import com.bankstar.recommendation.dto.RecommendationResponse;
import com.bankstar.recommendation.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/recommendation/{user_id}")
    public RecommendationResponse getRecommendation(@PathVariable("user_id") UUID user_id) {
        return service.getRecommendations(user_id);
    }
}