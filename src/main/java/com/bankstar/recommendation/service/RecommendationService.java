package com.bankstar.recommendation.service;

import com.bankstar.recommendation.dto.RecommendationDto;
import com.bankstar.recommendation.dto.RecommendationResponse;
import com.bankstar.recommendation.repository.RecommendationRepository;
import com.bankstar.recommendation.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final RecommendationRepository repository;
    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationService(RecommendationRepository repository, List<RecommendationRuleSet> ruleSets) {
        this.repository = repository;
        this.ruleSets = ruleSets;
    }

    public RecommendationResponse getRecommendations(UUID userId) {
        List<RecommendationDto> recommendations = new ArrayList<>();

        for (RecommendationRuleSet rule : ruleSets) {
            Optional<RecommendationDto> opt = rule.check(userId);
            if (opt.isPresent()) {
                recommendations.add(opt.get());
            }
        }

        return new RecommendationResponse(userId, recommendations);
    }
}