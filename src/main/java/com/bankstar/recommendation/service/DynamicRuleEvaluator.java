package com.bankstar.recommendation.service;

import com.bankstar.recommendation.dto.RecommendationDto;
import com.bankstar.recommendation.dto.RuleCondition;
import com.bankstar.recommendation.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DynamicRuleEvaluator {

    private final RecommendationRepository repository;

    public RecommendationDto evaluate(UUID userId, String productName, UUID productId, String productText, List<RuleCondition> conditions) {
        var stats = repository.getStats(userId);

        for (RuleCondition condition : conditions) {
            boolean result = evaluateCondition(condition, stats);
            if (condition.isNegate()) {
                result = !result;
            }
            if (!result) {
                return null; // Правило не выполнено
            }
        }

        RecommendationDto dto = new RecommendationDto();
        dto.setName(productName);
        dto.setProductId(productId);
        dto.setText(productText);
        return dto;
    }

    private boolean evaluateCondition(RuleCondition condition, RecommendationRepository.UserStats stats) {
        String query = condition.getQuery();
        List<String> args = condition.getArguments();

        switch (query) {
            case "USER_OF":
                String productType1 = args.get(0);
                return stats.hasProductType(productType1);

            case "ACTIVE_USER_OF":
                String productType2 = args.get(0);
                // Логика: нужно посчитать количество транзакций по типу продукта
                // Для упрощения считаем, что stats.countByType содержит количество транзакций
                return stats.countByType.getOrDefault(productType2, 0L) >= 5;

            case "TRANSACTION_SUM_COMPARE":
                String productType3 = args.get(0);
                String txType = args.get(1);
                String operator = args.get(2);
                int value = Integer.parseInt(args.get(3));

                BigDecimal sum = "DEPOSIT".equals(txType)
                        ? stats.sumDeposit(productType3)
                        : stats.sumWithdrawal(productType3);

                return compare(sum, operator, value);

            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                String productType4 = args.get(0);
                String op2 = args.get(1);
                BigDecimal deposit = stats.sumDeposit(productType4);
                BigDecimal withdrawal = stats.sumWithdrawal(productType4);
                return compare(deposit.subtract(withdrawal), op2, 0); // Сравниваем разницу с 0

            default:
                throw new IllegalArgumentException("Unknown query type: " + query);
        }
    }

    private boolean compare(BigDecimal value, String operator, int target) {
        int cmp = value.compareTo(BigDecimal.valueOf(target));
        return switch (operator) {
            case ">" -> cmp > 0;
            case "<" -> cmp < 0;
            case "=" -> cmp == 0;
            case ">=" -> cmp >= 0;
            case "<=" -> cmp <= 0;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}