package com.bankstar.recommendation.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class DynamicRuleResponse {
    private UUID id;
    private String productName;
    private UUID productId;
    private String productText;
    private List<RuleCondition> rule;
}