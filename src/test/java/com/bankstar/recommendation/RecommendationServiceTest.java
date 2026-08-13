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
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        RecommendationResponse resp = service.getRecommendations(userId);

        assertNotNull(resp);
        assertEquals(userId, resp.getUser_id());
        assertNotNull(resp.getRecommendations());

        assertTrue(resp.getRecommendations() != null);

        System.out.println("Рекомендации для пользователя " + userId + ": " + resp.getRecommendations().size());
    }

    @Test
    void testContextLoads() {
        assertTrue(true);
    }
}