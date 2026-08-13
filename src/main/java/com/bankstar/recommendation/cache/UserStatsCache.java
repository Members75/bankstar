package com.bankstar.recommendation.cache;

import com.bankstar.recommendation.repository.RecommendationRepository;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class UserStatsCache {

    private final LoadingCache<UUID, RecommendationRepository.UserStats> statsCache;

    public UserStatsCache(RecommendationRepository repository) {
        this.statsCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(repository::loadStats);
    }

    public RecommendationRepository.UserStats get(UUID userId) {
        return statsCache.get(userId);
    }
}