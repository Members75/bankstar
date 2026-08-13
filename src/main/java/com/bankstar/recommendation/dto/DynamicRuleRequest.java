package com.bankstar.recommendation.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class DynamicRuleRequest {
    private String productName;
    private UUID productId;
    private String productText;
    private List<RuleCondition> rule;
}