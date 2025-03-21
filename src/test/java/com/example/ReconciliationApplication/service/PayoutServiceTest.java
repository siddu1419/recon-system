package com.example.ReconciliationApplication.service;

import com.example.ReconciliationApplication.model.Payout;
import com.example.ReconciliationApplication.model.Settlement;
import com.example.ReconciliationApplication.repository.PayoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PayoutServiceTest {

    @Mock
    private PayoutRepository payoutRepository;

    @InjectMocks
    private PayoutService payoutService;

    private Settlement testSettlement;
    private Payout testPayout;

    @BeforeEach
    void setUp() {
        testSettlement = new Settlement();
        testSettlement.setSettlementId(UUID.randomUUID().toString());
        testSettlement.setTransactionId(UUID.randomUUID().toString());
        testSettlement.setSettledAmount(new BigDecimal("100.00"));
        testSettlement.setUserBankAccountId("userBank123");
        testSettlement.setPartnerBankAccountId("partnerBank123");
        testSettlement.setBatchId("batch123");

        testPayout = new Payout();
        testPayout.setPayoutId(UUID.randomUUID().toString());
        testPayout.setSettlementId(testSettlement.getSettlementId());
        testPayout.setAmountToBePaidForPartner(testSettlement.getSettledAmount());
        testPayout.setPayoutTime(LocalDateTime.now());
        testPayout.setStatus(Payout.PayoutStatus.SUCCESS);
        testPayout.setPartnerBankAccountId(testSettlement.getPartnerBankAccountId());
        testPayout.setBatchId(testSettlement.getBatchId());
    }

    @Test
    void processPayoutSuccess() {
        when(payoutRepository.save(any(Payout.class))).thenReturn(testPayout);

        payoutService.processPayout(testSettlement);

        assertEquals(Payout.PayoutStatus.SUCCESS, testPayout.getStatus());
        assertEquals(testSettlement.getSettlementId(), testPayout.getSettlementId());
        assertEquals(testSettlement.getSettledAmount(), testPayout.getAmountToBePaidForPartner());
        assertEquals(testSettlement.getPartnerBankAccountId(), testPayout.getPartnerBankAccountId());
        assertEquals(testSettlement.getBatchId(), testPayout.getBatchId());
    }
}