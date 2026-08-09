package com.bankstar.recommendation.controller;

import com.bankstar.recommendation.dto.DynamicRuleRequest;
import com.bankstar.recommendation.dto.DynamicRuleResponse;
import com.bankstar.recommendation.repository.DynamicRuleJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
public class DynamicRuleController {

    private final DynamicRuleJdbcRepository repository;

    @PostMapping
    public ResponseEntity<DynamicRuleResponse> createRule(@RequestBody DynamicRuleRequest request) {
        repository.save(request);
        // Возвращаем объект с ID (нужно получить ID из БД, здесь упрощено)
        DynamicRuleResponse resp = new DynamicRuleResponse();
        resp.setProductName(request.getProductName());
        resp.setProductId(request.getProductId());
        resp.setProductText(request.getProductText());
        resp.setRule(request.getRule());
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<DynamicRuleResponse>> listRules() {
        List<DynamicRuleResponse> rules = repository.findAll();
        return ResponseEntity.ok(rules);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId) {
        repository.delete(productId);
        return ResponseEntity.noContent().build();
    }
}