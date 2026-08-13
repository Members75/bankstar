package com.bankstar.recommendation.rules;

import com.bankstar.recommendation.dto.RecommendationDto;
import com.bankstar.recommendation.repository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private static final String ID = "c3d7e9f1-a2b3-4c5d-6e7f-8a9b0c1d2e3f";
    private static final BigDecimal THRESHOLD_HIGH_SAVING = new BigDecimal("5000");

    private final RecommendationRepository repository;

    public TopSavingRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        var stats = repository.getStats(userId);

        boolean hasSaving = stats.hasProductType("SAVING");
        BigDecimal savingSum = stats.sumDeposit("SAVING");

        if (hasSaving && savingSum.compareTo(THRESHOLD_HIGH_SAVING) > 0) {
            return Optional.of(new RecommendationDto(
                    ID,
                    "Топ-накопительный счёт",
                    "Вы активно копите — мы ценим это! Откройте наш премиальный накопительный счёт с повышенной ставкой и дополнительными бонусами для крупных накоплений. Максимальная доходность, гибкие условия пополнения и снятия, а также персональный менеджер, который поможет управлять вашими финансами."
            ));
        }
        return Optional.empty();
    }
}