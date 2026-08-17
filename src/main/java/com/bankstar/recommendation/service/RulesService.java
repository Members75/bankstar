package com.bankstar.recommendation.service;

import com.bankstar.recommendation.dto.DynamicRuleRequest;
import com.bankstar.recommendation.dto.RuleCondition;
import com.bankstar.recommendation.entity.RuleEntity;
import com.bankstar.recommendation.repository.RuleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional("rulesTransactionManager")
public class RulesService {

    private final RuleRepository ruleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RuleEntity saveRule(DynamicRuleRequest request) {
        RuleEntity entity = new RuleEntity();
        entity.setProductName(request.getProductName());
        entity.setProductId(request.getProductId());
        entity.setProductText(request.getProductText());

        try {
            entity.setRuleJson(objectMapper.writeValueAsString(request.getRule()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to serialize rule conditions", e);
        }
        return ruleRepository.save(entity);
    }

    public List<RuleEntity> findAllRules() {
        return ruleRepository.findAll();
    }

    public void deleteRule(UUID id) {
        ruleRepository.deleteById(id);
    }

    public List<RuleCondition> deserializeConditions(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to deserialize rule conditions", e);
        }
    }
}
