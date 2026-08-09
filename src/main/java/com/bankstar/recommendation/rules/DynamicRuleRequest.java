package com.bankstar.recommendation.rules;

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

@Data
public class RuleCondition {
    private String query;
    private List<String> arguments;
    private boolean negate;
}

@Data
public class DynamicRuleResponse {
    private UUID id;
    private String productName;
    private UUID productId;
    private String productText;
    private List<RuleCondition> rule;
}