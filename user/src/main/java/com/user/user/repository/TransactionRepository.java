package com.user.user.repository;

import com.user.user.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository  extends JpaRepository<Transaction, Long> {
    boolean existsByIdempotencyKey(String key);
}
