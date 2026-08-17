package com.bankstar.recommendation.dto;

import java.util.List;
import java.util.UUID;

public class RecommendationResponse {
    private UUID user_id;
    private List<RecommendationDto> recommendations;

    public RecommendationResponse(UUID user_id, List<RecommendationDto> recommendations) {
        this.user_id = user_id;
        this.recommendations = recommendations;
    }

    public UUID getUser_id() { return user_id; }
    public List<RecommendationDto> getRecommendations() { return recommendations; }
}
