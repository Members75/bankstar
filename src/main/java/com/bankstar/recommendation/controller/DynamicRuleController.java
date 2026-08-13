package com.bankstar.recommendation.controller;

import com.bankstar.recommendation.dto.DynamicRuleRequest;
import com.bankstar.recommendation.dto.DynamicRuleResponse;
import com.bankstar.recommendation.entity.DynamicRule;
import com.bankstar.recommendation.repository.DynamicRuleRepository;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
public class DynamicRuleController {

    private final DynamicRuleRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // один экземпляр

    @PostMapping
    public ResponseEntity<DynamicRuleResponse> createRule(@RequestBody DynamicRuleRequest request) {
        DynamicRule rule = new DynamicRule();
        rule.setProductName(request.getProductName());
        rule.setProductId(request.getProductId());
        rule.setProductText(request.getProductText());

        try {
            rule.setRuleJson(objectMapper.valueToTree(request.getRule()));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сериализации rule", e);
        }

        DynamicRule saved = repository.save(rule);

        DynamicRuleResponse response = new DynamicRuleResponse();
        response.setId(saved.getId());
        response.setProductName(saved.getProductName());
        response.setProductId(saved.getProductId());
        response.setProductText(saved.getProductText()); // было: saved.productText

        try {
            response.setRule(objectMapper.treeToValue(saved.getRuleJson(), List.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации rule", e);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DynamicRuleResponse>> listRules() {
        List<DynamicRule> rules = repository.findAll();

        List<DynamicRuleResponse> responses = rules.stream().map(r -> {
            DynamicRuleResponse resp = new DynamicRuleResponse();
            resp.setId(r.getId());
            resp.setProductName(r.getProductName());
            resp.setProductId(r.getProductId());
            resp.setProductText(r.getProductText());

            try {
                resp.setRule(objectMapper.treeToValue(r.getRuleJson(), List.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Ошибка десериализации rule", e);
            }
            return resp;
        }).toList();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId) {
        repository.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}