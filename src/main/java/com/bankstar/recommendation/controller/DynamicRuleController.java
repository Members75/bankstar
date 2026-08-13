package com.bankstar.recommendation.controller;

import com.bankstar.recommendation.dto.ApiResponse;
import com.bankstar.recommendation.dto.DynamicRuleRequest;
import com.bankstar.recommendation.dto.DynamicRuleResponse;
import com.bankstar.recommendation.entity.DynamicRule;
import com.bankstar.recommendation.repository.DynamicRuleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
public class DynamicRuleController {

    private final DynamicRuleRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<DynamicRuleResponse> createRule(@RequestBody DynamicRuleRequest request) {
        DynamicRule rule = new DynamicRule();
        rule.setProductName(request.getProductName());
        rule.setProductId(request.getProductId());
        rule.setProductText(request.getProductText());

        rule.setRuleJson(objectMapper.valueToTree(request.getRule()));

        DynamicRule saved = repository.save(rule);
        return ResponseEntity.ok(mapToResponse(saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DynamicRuleResponse>>> listRules() {
        List<DynamicRule> rules = repository.findAll();
        List<DynamicRuleResponse> responses = rules.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(responses));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId) {
        repository.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }

    @SneakyThrows
    private DynamicRuleResponse mapToResponse(DynamicRule r) {
        DynamicRuleResponse resp = new DynamicRuleResponse();
        resp.setId(r.getId());
        resp.setProductName(r.getProductName());
        resp.setProductId(r.getProductId());
        resp.setProductText(r.getProductText());

        if (r.getRuleJson() != null) {
            resp.setRule(objectMapper.treeToValue(r.getRuleJson(), List.class));
        } else {
            resp.setRule(null);
        }
        return resp;
    }
}