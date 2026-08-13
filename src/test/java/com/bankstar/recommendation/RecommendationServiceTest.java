package com.bankstar.recommendation;

import com.bankstar.recommendation.dto.RecommendationResponse;
import com.bankstar.recommendation.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecommendationServiceTest {

    @Autowired
    private RecommendationService service;

    @Test
    void testRecommendationLogicWithRealData() {
        // ВАЖНО: Здесь нужно подставить UUID пользователя, который ЕСТЬ в твоей базе transaction.mv.db
        // Зайди в H2 Console (http://localhost:8080/h2-console), подключись к jdbc:h2:file:./transaction
        // и выполни: SELECT DISTINCT user_id FROM transactions;
        // Скопируй один из UUID сюда.
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        RecommendationResponse resp = service.getRecommendations(userId);

        assertNotNull(resp);
        assertEquals(userId, resp.getUser_id());
        assertNotNull(resp.getRecommendations());

        // Тест проходит, если сервис не упал с ошибкой 500 и вернул список (даже пустой)
        // Если в базе у этого пользователя есть условия для правил, список не будет пустым.
        assertTrue(resp.getRecommendations() != null);

        System.out.println("Рекомендации для пользователя " + userId + ": " + resp.getRecommendations().size());
    }

    @Test
    void testContextLoads() {
        // Этот тест должен проходить всегда, если приложение стартует
        assertTrue(true);
    }
}