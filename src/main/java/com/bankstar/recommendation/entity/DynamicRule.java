package com.bankstar.recommendation.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "dynamic_rules")
@Data
public class DynamicRule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private UUID productId;

    @Column
    private String productText;

    @Column(columnDefinition = "JSONB", nullable = false)
    private String ruleJson;

    @Column(updatable = false)
    private Instant createdAt;
}