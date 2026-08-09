package com.bankstar.recommendation.service;

import com.bankstar.recommendation.dto.RecommendationDto;
import com.bankstar.recommendation.dto.RecommendationResponse;
import com.bankstar.recommendation.repository.DynamicRuleJdbcRepository;
import com.bankstar.recommendation.repository.RecommendationRepository;
import com.bankstar.recommendation.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository repository;
    private final List<RecommendationRuleSet> fixedRuleSets;
    private final DynamicRuleJdbcRepository rulesRepository;
    private final DynamicRuleEvaluator evaluator;

    public RecommendationResponse getRecommendations(UUID userId) {
        List<RecommendationDto> recommendations = new ArrayList<>();

        // 1. Проверка фиксированных правил
        for (RecommendationRuleSet rule : fixedRuleSets) {
            Optional<RecommendationDto> opt = rule.check(userId);
            if (opt.isPresent()) {
                recommendations.add(opt.get());
            }
        }

        var dynamicRules = rulesRepository.findAll();
        for (var rule : dynamicRules) {
            RecommendationDto dto = evaluator.evaluate(userId, rule.getProductName(), rule.getProductId(), rule.getProductText(), rule.getRule());
            if (dto != null) {
                recommendations.add(dto);
            }
        }

        return new RecommendationResponse(userId, recommendations);
    }
}