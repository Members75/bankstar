package com.bankstar.recommendation.dto;

import lombok.Data;
import java.util.List;

@Data
public class RuleCondition {
    private String query;
    private List<String> arguments;
    private boolean negate;
}