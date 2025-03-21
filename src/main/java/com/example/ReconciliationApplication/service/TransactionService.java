package com.example.ReconciliationApplication.service;

import com.example.ReconciliationApplication.model.Transaction;
import com.example.ReconciliationApplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for handling transaction-related business logic.
 * Provides methods for creating and retrieving transactions.
 */
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    /**
     * Constructor for TransactionService.
     * @param transactionRepository The repository for transaction data access
     */
    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Creates a new transaction in the system.
     * The transaction will be saved with a generated UUID and timestamps.
     * 
     * @param transaction The transaction to be created
     * @return The saved transaction with generated ID and timestamps
     */
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Retrieves all transactions that match the specified status.
     * 
     * @param status The status to filter transactions by (PENDING, FAILED, or SUCCESS)
     * @return List of transactions matching the specified status
     */
    public List<Transaction> getTransactionsByStatus(Transaction.TransactionStatus status) {
        return transactionRepository.findByStatus(status);
    }
}