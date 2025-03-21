package com.example.ReconciliationApplication.controller;

import com.example.ReconciliationApplication.model.Transaction;
import com.example.ReconciliationApplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling transaction-related HTTP requests.
 * Provides endpoints for creating transactions and retrieving transactions by status.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * Constructor for TransactionController.
     * @param transactionService The service layer for transaction operations
     */
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Creates a new transaction.
     * 
     * @param transaction The transaction details to be created
     * @return ResponseEntity containing transaction ID and status
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.ok(Map.of(
            "transactionId", createdTransaction.getTransactionId(),
            "status", createdTransaction.getStatus().toString(),
            "message", "Transaction created successfully"
        ));
    }

    /**
     * Retrieves all transactions with the specified status.
     * 
     * @param status The status to filter transactions by (PENDING, FAILED, or SUCCESS)
     * @return List of transactions matching the specified status
     */
    @GetMapping("/status/{status}")
    public List<Transaction> getTransactionsByStatus(@PathVariable String status) {
        return transactionService.getTransactionsByStatus(Transaction.TransactionStatus.valueOf(status.toUpperCase()));
    }
}