package com.bankstar.recommendation.controller;

import com.bankstar.recommendation.dto.ApiResponse;
import com.bankstar.recommendation.dto.DynamicRuleRequest;
import com.bankstar.recommendation.dto.DynamicRuleResponse;
import com.bankstar.recommendation.entity.RuleEntity;
import com.bankstar.recommendation.service.RulesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
@Transactional("rulesTransactionManager")
public class RulesController {

    private final RulesService rulesService;

    @PostMapping
    public ResponseEntity<DynamicRuleResponse> createRule(
            @Valid @RequestBody DynamicRuleRequest request) {

        RuleEntity entity = rulesService.saveRule(request);
        DynamicRuleResponse resp = toResponse(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ApiResponse<List<DynamicRuleResponse>> getAllRules() {
        List<RuleEntity> entities = rulesService.findAllRules();
        List<DynamicRuleResponse> responses = entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRule(@PathVariable UUID id) {
        rulesService.deleteRule(id);
        return ResponseEntity.noContent().build(); // 204
    }

    private DynamicRuleResponse toResponse(RuleEntity e) {
        DynamicRuleResponse r = new DynamicRuleResponse();
        r.setId(e.getId());
        r.setProductName(e.getProductName());
        r.setProductId(e.getProductId());
        r.setProductText(e.getProductText());
        r.setRule(rulesService.deserializeConditions(e.getRuleJson()));
        return r;
    }
}