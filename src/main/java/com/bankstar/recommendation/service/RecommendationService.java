package com.bankstar.recommendation.service;

import com.bankstar.recommendation.dto.ApiResponse;
import com.bankstar.recommendation.dto.RecommendationDto;
import com.bankstar.recommendation.dto.RecommendationResponse;
import com.bankstar.recommendation.rules.RecommendationRuleSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    public List<RecommendationDto> getRecommendationsForUser(UUID userId) {
        return ruleSets.stream()
                .map(ruleSet -> ruleSet.check(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public CompletableFuture<ApiResponse<List<RecommendationDto>>> getRecommendationsAsync(UUID userId) {
        return CompletableFuture.supplyAsync(() -> {
            var recommendations = getRecommendationsForUser(userId);
            return new ApiResponse<>(recommendations);
        });
    }

    public RecommendationResponse getRecommendations(UUID userId) {
        return null;
    }
}