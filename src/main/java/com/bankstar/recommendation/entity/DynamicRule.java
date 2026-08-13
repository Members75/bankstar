package com.bankstar.recommendation.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "dynamic_rules")
@Data
public class DynamicRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Или UUID, если настроено в БД
    private UUID id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private UUID productId;

    @Column
    private String productText;

    @Column(columnDefinition = "JSONB")
    private JsonNode ruleJson;

    @Column(updatable = false)
    private Instant createdAt;
}