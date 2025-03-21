package com.example.ReconciliationApplication.repository;

import com.example.ReconciliationApplication.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    Optional<Transaction> findByTransactionId(String transactionId);
}