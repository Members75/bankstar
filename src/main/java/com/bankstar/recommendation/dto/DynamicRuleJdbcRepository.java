package com.bankstar.recommendation.dto;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DynamicRuleJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public DynamicRuleJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(DynamicRuleRequest request) {
        String sql = "INSERT INTO dynamic_rules (product_name, product_id, product_text, rule_json) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, request.getProductName(), request.getProductId(), request.getProductText());
    }

    public List<DynamicRuleResponse> findAll() {
        String sql = "SELECT id, product_name, product_id, product_text, rule_json FROM dynamic_rules";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DynamicRuleResponse resp = new DynamicRuleResponse();
            resp.setId(rs.getObject("id", UUID.class));
            resp.setProductName(rs.getString("product_name"));
            resp.setProductId(rs.getObject("product_id", UUID.class));
            resp.setProductText(rs.getString("product_text"));
            return resp;
        });
    }

    public void delete(UUID productId) {
        String sql = "DELETE FROM dynamic_rules WHERE product_id = ?";
        jdbcTemplate.update(sql, productId);
    }
}