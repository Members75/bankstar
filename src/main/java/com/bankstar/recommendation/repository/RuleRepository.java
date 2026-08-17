package com.bankstar.recommendation.repository;

import com.bankstar.recommendation.entity.RuleEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional("rulesTransactionManager")
public interface RuleRepository extends JpaRepository<RuleEntity, UUID> {
}