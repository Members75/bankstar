package com.bankstar.recommendation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<Map<String, Object>> getRecommendations(@PathVariable String userIdStr) {
        UUID userId;
        try {
            userId = UUID.fromString(userIdStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        List<RecommendationDto> recommendations = service.getRecommendations(userId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("user_id", userIdStr);
        response.put("recommendations", recommendations);

        return ResponseEntity.ok(response);
    }
}