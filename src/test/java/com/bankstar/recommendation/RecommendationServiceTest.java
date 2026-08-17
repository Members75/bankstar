package com.bankstar.recommendation;

import com.bankstar.recommendation.dto.RecommendationDto;
import com.bankstar.recommendation.dto.RecommendationResponse;
import com.bankstar.recommendation.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecommendationServiceTest {

    @Autowired
    private RecommendationService service;

    @Test
    void testRecommendationLogicWithRealData() {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        List<RecommendationDto> recommendations = service.getRecommendationsForUser(userId);

        assertNotNull(recommendations, "Список рекомендаций не должен быть null");
        assertFalse(recommendations.isEmpty(), "Для тестового пользователя должны быть рекомендации");

        System.out.println("Рекомендации для пользователя " + userId + ": " + recommendations.size());

        RecommendationDto first = recommendations.get(0);
        assertNotNull(first.getName(), "У рекомендации должно быть productName");

    }

    @Test
    void testContextLoads() {
        assertTrue(true);
    }
}