package com.example.ReconciliationApplication.service;

import com.example.ReconciliationApplication.model.Transaction;
import com.example.ReconciliationApplication.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testTransaction = new Transaction();
        testTransaction.setTransactionId(UUID.randomUUID().toString());
        testTransaction.setUserId("user123");
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setStatus(Transaction.TransactionStatus.PENDING);
        testTransaction.setUserBankAccountId("userBank123");
        testTransaction.setPartnerBankAccountId("partnerBank123");
    }

    @Test
    void createTransactionSuccess() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        Transaction result = transactionService.createTransaction(testTransaction);

        assertNotNull(result);
        assertNotNull(result.getTransactionId());
        assertEquals("user123", result.getUserId());
        assertEquals(new BigDecimal("100.00"), result.getAmount());
        assertEquals(Transaction.TransactionStatus.PENDING, result.getStatus());
        assertEquals("userBank123", result.getUserBankAccountId());
        assertEquals("partnerBank123", result.getPartnerBankAccountId());
    }

    @Test
    void getTransactionsByStatusSuccess() {
        List<Transaction> transactions = Collections.singletonList(testTransaction);
        when(transactionRepository.findByStatus(Transaction.TransactionStatus.PENDING)).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactionsByStatus(Transaction.TransactionStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTransaction.getTransactionId(), result.getFirst().getTransactionId());
    }
} 