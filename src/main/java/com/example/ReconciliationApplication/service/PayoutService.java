package com.example.ReconciliationApplication.service;

import com.example.ReconciliationApplication.model.Payout;
import com.example.ReconciliationApplication.model.Settlement;
import com.example.ReconciliationApplication.repository.PayoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service responsible for handling payout processing.
 * Since the project does not require actual partner API calls,
 * we simulate a successful payout and store the payout details.
 */
@Service
public class PayoutService {
    private static final Logger logger = LoggerFactory.getLogger(PayoutService.class);

    private final PayoutRepository payoutRepository;

    @Autowired
    public PayoutService(PayoutRepository payoutRepository) {
        this.payoutRepository = payoutRepository;
    }

    /**
     * Processes a payout for a successfully reconciled settlement.
     * The payout is assumed to be successful and recorded in the database.
     *
     * @param settlement The settlement record that needs to be processed for payout.
     */
    @Transactional
    public void processPayout(Settlement settlement) {
        try {
            logger.info("Processing payout for settlement: {}", settlement.getTransactionId());

            // Simulate a payout API call (Always returns success)
            boolean payoutSuccess = callPayoutApi(settlement);

            // Create a new payout record with settlement details
            Payout payout = new Payout();
            payout.setSettlementId(settlement.getSettlementId());
            payout.setAmountToBePaidForPartner(settlement.getSettledAmount());
            payout.setPayoutTime(LocalDateTime.now());
            payout.setStatus(payoutSuccess ? Payout.PayoutStatus.SUCCESS : Payout.PayoutStatus.FAILED);
            payout.setPartnerBankAccountId(settlement.getPartnerBankAccountId());
            payout.setBatchId(settlement.getBatchId());

            // Save the payout details in the database
            payoutRepository.save(payout);

            logger.info("Payout successfully recorded for settlement: {}", settlement.getSettlementId());
        } catch (Exception e) {
            logger.error("Error while processing payout for settlement: {}", settlement.getSettlementId(), e);
            throw new RuntimeException("Failed to process payout", e);
        }
    }

    /**
     * Simulates an external payout API call.
     * In this project, we assume all payouts are successful.
     *
     * @param settlement The settlement for which payout is being initiated.
     * @return Always returns true, indicating a successful payout.
     */
    private boolean callPayoutApi(Settlement settlement) {
        // No actual API call is made, assuming all payouts are successful.
        return true;
    }
}
