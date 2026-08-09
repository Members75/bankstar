package com.bankstar.recommendation.repository;

import com.bankstar.recommendation.cache.UserStatsCache;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class RecommendationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserStatsCache cache;

    public RecommendationRepository(JdbcTemplate jdbcTemplate, UserStatsCache cache) {
        this.jdbcTemplate = jdbcTemplate;
        this.cache = cache;
    }

    public UserStats getStats(UUID userId) {
        return cache.get(userId);
    }

    public UserStats loadStats(UUID userId) {
        String sql = """
            SELECT
                p.type AS product_type,
                t.type AS tx_type,
                COUNT(*) AS cnt,
                COALESCE(SUM(t.amount), 0) AS amount_sum
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ?
            GROUP BY p.type, t.type
            """;

        Map<String, Long> countByType = new HashMap<>();
        Map<String, BigDecimal> sumDepositByType = new HashMap<>();
        Map<String, BigDecimal> sumWithdrawalByType = new HashMap<>();

        jdbcTemplate.query(sql, (rs, rowNum) -> {
            String productType = rs.getString("product_type");
            String txType = rs.getString("tx_type"); // В БД это колонка type, содержит WITHDRAW/DEPOSIT
            long cnt = rs.getLong("cnt");
            BigDecimal amount = rs.getBigDecimal("amount_sum");
            if (amount == null) amount = BigDecimal.ZERO;

            // 1. Считаем количество транзакций по типу продукта (для правила ACTIVE_USER_OF)
            countByType.merge(productType, cnt, Long::sum);

            // 2. Считаем суммы. ВАЖНО: в БД тип операции WITHDRAW, а не WITHDRAWAL
            if ("DEPOSIT".equals(txType)) {
                sumDepositByType.merge(productType, amount, BigDecimal::add);
            } else if ("WITHDRAW".equals(txType)) {
                sumWithdrawalByType.merge(productType, amount, BigDecimal::add);
            }
            return null;
        }, userId);

        return new UserStats(countByType, sumDepositByType, sumWithdrawalByType);
    }

    public static class UserStats {
        private final Map<String, Long> countByType;
        private final Map<String, BigDecimal> sumDepositByType;
        private final Map<String, BigDecimal> sumWithdrawalByType;

        public UserStats(Map<String, Long> countByType,
                         Map<String, BigDecimal> sumDepositByType,
                         Map<String, BigDecimal> sumWithdrawalByType) {
            this.countByType = countByType;
            this.sumDepositByType = sumDepositByType;
            this.sumWithdrawalByType = sumWithdrawalByType;
        }

        public boolean hasProductType(String type) {
            return countByType.getOrDefault(type, 0L) > 0;
        }

        public boolean isActiveUserOf(String type) {
            return countByType.getOrDefault(type, 0L) >= 5;
        }

        public BigDecimal sumDeposit(String type) {
            return sumDepositByType.getOrDefault(type, BigDecimal.ZERO);
        }

        public BigDecimal sumWithdrawal(String type) {
            return sumWithdrawalByType.getOrDefault(type, BigDecimal.ZERO);
        }
    }
}