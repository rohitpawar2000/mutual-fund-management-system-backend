package com.user.user.repository;

import com.user.user.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRepository  extends JpaRepository<Transaction, Long> {
    boolean existsByIdempotencyKey(String key);

    Optional <Transaction> findByUser_Id(Long id);

    Optional<Transaction> findTopByUser_IdAndTypeOrderByCreatedAtDesc(Long userId, String type);
}
