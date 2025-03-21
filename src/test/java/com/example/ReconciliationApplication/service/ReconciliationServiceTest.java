package com.example.ReconciliationApplication.service;

import com.example.ReconciliationApplication.model.Settlement;
import com.example.ReconciliationApplication.model.SettlementRecord;
import com.example.ReconciliationApplication.model.Transaction;
import com.example.ReconciliationApplication.repository.SettlementRepository;
import com.example.ReconciliationApplication.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReconciliationServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SettlementRepository settlementRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private ReconciliationService reconciliationService;

    private Transaction testTransaction;
    private SettlementRecord testSettlementRecord;
    private Settlement testSettlement;

    @BeforeEach
    void setUp() {
        testTransaction = new Transaction();
        testTransaction.setTransactionId(UUID.randomUUID().toString());
        testTransaction.setUserId("user123");
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setStatus(Transaction.TransactionStatus.SUCCESS);
        testTransaction.setUserBankAccountId("userBank123");
        testTransaction.setPartnerBankAccountId("partnerBank123");

        testSettlementRecord = new SettlementRecord();
        testSettlementRecord.setTransactionId(testTransaction.getTransactionId());
        testSettlementRecord.setUserId(testTransaction.getUserId());
        testSettlementRecord.setAmount(testTransaction.getAmount());
        testSettlementRecord.setUserBankAccountId(testTransaction.getUserBankAccountId());
        testSettlementRecord.setPartnerBankAccountId(testTransaction.getPartnerBankAccountId());

        testSettlement = new Settlement();
        testSettlement.setTransactionId(testTransaction.getTransactionId());
        testSettlement.setSettledAmount(testTransaction.getAmount());
        testSettlement.setUserBankAccountId(testTransaction.getUserBankAccountId());
        testSettlement.setPartnerBankAccountId(testTransaction.getPartnerBankAccountId());
    }

    @Test
    void settlementExistsWhenSettlementExistsReturnsTrue() {
        when(settlementRepository.findByTransactionId(testTransaction.getTransactionId()))
            .thenReturn(Optional.of(testSettlement));

        boolean result = reconciliationService.settlementExists(testTransaction.getTransactionId());
        assertTrue(result);
    }

    @Test
    void settlementExistsWhenSettlementDoesNotExistReturnsFalse() {
        when(settlementRepository.findByTransactionId(testTransaction.getTransactionId()))
            .thenReturn(Optional.empty());

        boolean result = reconciliationService.settlementExists(testTransaction.getTransactionId());
        assertFalse(result);
    }
}