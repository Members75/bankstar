package com.bankstar.recommendation.rules;

import com.bankstar.recommendation.dto.RecommendationDto;
import com.bankstar.recommendation.repository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private static final String ID = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private static final BigDecimal THRESHOLD_SAVING = new BigDecimal("1000");

    private final RecommendationRepository repository;

    public Invest500RuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        var stats = repository.getStats(userId);

        boolean hasDebit = stats.hasProductType("DEBIT_CARD");
        boolean hasInvest = stats.hasProductType("INVESTMENT");
        BigDecimal sumSaving = stats.sumDeposit("SAVING");

        if (hasDebit && !hasInvest && sumSaving.compareTo(THRESHOLD_SAVING) > 0) {

            RecommendationDto dto = new RecommendationDto(
                    ID,
                    "Invest 500",
                    "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом."
            );

            UUID productId = UUID.fromString("33333333-3333-3333-3333-333333333333");
            dto.setProductId(productId);

            return Optional.of(dto);
        }

        return Optional.empty();
    }
}