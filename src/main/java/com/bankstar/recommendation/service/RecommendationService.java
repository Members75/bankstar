package com.bankstar.recommendation.service;

import com.bankstar.recommendation.dto.ApiResponse;
import com.bankstar.recommendation.dto.RecommendationDto;
import com.bankstar.recommendation.dto.RecommendationResponse;
import com.bankstar.recommendation.rules.RecommendationRuleSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;
    private final RulesService rulesService;
    private final DynamicRuleEvaluator dynamicRuleEvaluator;

    public List<RecommendationDto> getRecommendationsForUser(UUID userId) {
        var recommendations = new ArrayList<RecommendationDto>();

        recommendations.addAll(
                ruleSets.stream()
                        .map(r -> r.check(userId))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList()
        );

        var dynamicRules = rulesService.findAllRules();
        for (var rule : dynamicRules) {
            var conditions = rulesService.deserializeConditions(rule.getRuleJson());
            var dto = dynamicRuleEvaluator.evaluate(
                    userId,
                    rule.getProductName(),
                    rule.getProductId(),
                    rule.getProductText(),
                    conditions
            );
            if (dto != null) {
                recommendations.add(dto);
            }
        }

        return recommendations;
    }
}