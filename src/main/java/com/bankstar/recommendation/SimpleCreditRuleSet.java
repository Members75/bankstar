package com.bankstar.recommendation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private static final String ID = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
    private static final BigDecimal THRESHOLD_WITHDRAWAL = new BigDecimal("100000");

    private final RecommendationRepository repository;

    public SimpleCreditRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        var stats = repository.getStats(userId);

        boolean hasCredit = stats.hasProductType("CREDIT");
        BigDecimal sumDebitDeposit = stats.sumDeposit("DEBIT");
        BigDecimal sumDebitWithdrawal = stats.sumWithdrawal("DEBIT");

        boolean cond1 = !hasCredit;
        boolean cond2 = sumDebitDeposit.compareTo(sumDebitWithdrawal) > 0;
        boolean cond3 = sumDebitWithdrawal.compareTo(THRESHOLD_WITHDRAWAL) > 0;

        if (cond1 && cond2 && cond3) {
            return Optional.of(new RecommendationDto(
                    ID,
                    "Простой кредит",
                    "Откройте мир выгодных кредитов с нами!\n\nИщете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n\nПочему выбирают нас:\n\nБыстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n\nУдобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n\nШирокий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.\n\nНе упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!"
            ));
        }
        return Optional.empty();
    }
}