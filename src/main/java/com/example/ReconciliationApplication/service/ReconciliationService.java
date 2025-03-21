package com.example.ReconciliationApplication.service;

import com.example.ReconciliationApplication.model.Settlement;
import com.example.ReconciliationApplication.model.SettlementRecord;
import com.example.ReconciliationApplication.model.Transaction;
import com.example.ReconciliationApplication.repository.SettlementRepository;
import com.example.ReconciliationApplication.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReconciliationService {

    private static final Logger logger = LoggerFactory.getLogger(ReconciliationService.class);
    private final TransactionRepository transactionRepository;
    private final SettlementRepository settlementRepository;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public ReconciliationService(TransactionRepository transactionRepository,
                                 SettlementRepository settlementRepository,
                                 KafkaProducerService kafkaProducerService) {
        this.transactionRepository = transactionRepository;
        this.settlementRepository = settlementRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Checks if a settlement already exists for a given transaction ID.
     *
     * @param transactionId The ID of the transaction to check
     * @return true if a settlement exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean settlementExists(String transactionId) {
        return settlementRepository.findByTransactionId(transactionId).isPresent();
    }

    /**
     * Processes a settlement record by validating the associated transaction.
     * If validation fails, the settlement is marked as FAILED.
     *
     * @param record the settlement record to process
     */
    @Transactional
    public void processSettlementRecord(SettlementRecord record) {
        try {
            logger.info("Processing settlement record for transaction: {}", record.getTransactionId());
            Settlement settlement = createSettlement(record);

            // Fetch the transaction from the database
            Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(record.getTransactionId());
            if (transactionOpt.isEmpty()) {
                handleFailedSettlement(settlement, "Transaction not found.");
                return;
            }

            Transaction transaction = transactionOpt.get();
            // Validate the transaction against the settlement record
            if (!isValidSettlement(transaction, record)) {
                handleFailedSettlement(settlement, "Validation failed: Status mismatch, amount mismatch, or bank account mismatch.");
                return;
            }

            // If validation passes, finalize the settlement as successful
            finalizeSuccessfulSettlement(settlement);
        } catch (Exception e) {
            logger.error("Error processing settlement record: {}", e.getMessage(), e);
        }
    }

    /**
     * Creates a new settlement entry based on the given settlement record.
     *
     * @param record the settlement record
     * @return a newly created settlement object
     */
    private Settlement createSettlement(SettlementRecord record) {
        Settlement settlement = new Settlement();
        settlement.setTransactionId(record.getTransactionId());
        settlement.setSettledAmount(record.getAmount());
        settlement.setUserBankAccountId(record.getUserBankAccountId());
        settlement.setPartnerBankAccountId(record.getPartnerBankAccountId());
        settlement.setBatchId(record.getBatchId());
        return settlement;
    }

    /**
     * Marks a settlement as FAILED and saves it with the given failure reason.
     *
     * @param settlement the settlement to mark as failed
     * @param reason the reason for failure
     */
    private void handleFailedSettlement(Settlement settlement, String reason) {
        logger.error("{} for settlement record: {}", reason, settlement.getTransactionId());
        settlement.setStatus(Settlement.SettlementStatus.FAILED);
        settlement.setComments(reason);
        settlementRepository.save(settlement);
    }

    /**
     * Marks a settlement as SUCCESS and queues it for payout.
     *
     * @param settlement the successfully processed settlement
     */
    private void finalizeSuccessfulSettlement(Settlement settlement) {
        settlement.setComments("Transaction validated successfully.");
        settlement.setStatus(Settlement.SettlementStatus.SUCCESS);
        settlementRepository.save(settlement);
        kafkaProducerService.sendPayoutMessage(settlement);
        logger.info("Successfully processed settlement record and queued for payout: {}", settlement.getTransactionId());
    }

    /**
     * Validates whether a settlement record matches the corresponding transaction.
     *
     * @param transaction the transaction to validate against
     * @param record the settlement record
     * @return true if the settlement record is valid, false otherwise
     */
    private boolean isValidSettlement(Transaction transaction, SettlementRecord record) {
        return transaction.getStatus() == Transaction.TransactionStatus.SUCCESS &&
                transaction.getAmount().compareTo(record.getAmount()) == 0 &&
                transaction.getUserBankAccountId().equals(record.getUserBankAccountId()) &&
                transaction.getPartnerBankAccountId().equals(record.getPartnerBankAccountId());
    }
}