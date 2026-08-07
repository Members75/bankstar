package com.bankstar.recommendation.rules;

import com.bankstar.recommendation.dto.RecommendationDto;
import com.bankstar.recommendation.repository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private static final String ID = "59efc529-2fff-41af-baff-90ccd7402925";
    private static final BigDecimal THRESHOLD = new BigDecimal("50000");

    private final RecommendationRepository repository;

    public TopSavingRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        var stats = repository.getStats(userId);

        boolean hasDebit = stats.hasProductType("DEBIT");
        BigDecimal sumDebitDeposit = stats.sumDeposit("DEBIT");
        BigDecimal sumSavingDeposit = stats.sumDeposit("SAVING");
        BigDecimal sumDebitWithdrawal = stats.sumWithdrawal("DEBIT");

        boolean cond1 = sumDebitDeposit.compareTo(THRESHOLD) >= 0
                || sumSavingDeposit.compareTo(THRESHOLD) >= 0;

        boolean cond2 = sumDebitDeposit.compareTo(sumDebitWithdrawal) > 0;

        if (hasDebit && cond1 && cond2) {
            return Optional.of(new RecommendationDto(
                    ID,
                    "Top Saving",
                    "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n\nПреимущества «Копилки»:\n\nНакопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n\nПрозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n\nБезопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n\nНачните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!"
            ));
        }
        return Optional.empty();
    }
}