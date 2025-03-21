package com.example.ReconciliationApplication.service;

import com.example.ReconciliationApplication.model.Settlement;
import com.example.ReconciliationApplication.model.SettlementRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final ObjectMapper objectMapper;
    private final ReconciliationService reconciliationService;
    private final PayoutService payoutService;

    @Autowired
    public KafkaConsumerService(ObjectMapper objectMapper, 
                              ReconciliationService reconciliationService,
                              PayoutService payoutService) {
        this.objectMapper = objectMapper;
        this.reconciliationService = reconciliationService;
        this.payoutService = payoutService;
    }

    /**
     * Listens for settlement record messages from the Kafka topic and processes them.
     * First checks if a settlement already exists for the transaction.
     * If no settlement exists, processes the record using reconciliation logic.
     *
     * @param message JSON string representing the settlement record details.
     */
    @KafkaListener(topics = "${kafka.topic.settlements}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSettlement(String message) {
        try {
            logger.info("Received settlement record message: {}", message);

            // Deserialize the received JSON message into a SettlementRecord object
            SettlementRecord record = objectMapper.readValue(message, SettlementRecord.class);

            // Check if settlement already exists for this transaction
            if (reconciliationService.settlementExists(record.getTransactionId())) {
                logger.info("Settlement already exists for transaction: {}, skipping processing", record.getTransactionId());
                return;
            }

            // Process the settlement record using reconciliation logic
            reconciliationService.processSettlementRecord(record);

            logger.info("Successfully processed settlement record for transaction: {}", record.getTransactionId());
        } catch (Exception e) {
            logger.error("Error processing settlement record message: {}", e.getMessage(), e);
        }
    }

    /**
     * Listens for payout messages from the Kafka topic and processes them.
     *
     * @param message JSON string representing the payout details.
     */
    @KafkaListener(topics = "${kafka.topic.payouts}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePayout(String message) {
        try {
            logger.info("Received payout message: {}", message);

            // Deserialize the received JSON message into a Settlement object
            Settlement settlement = objectMapper.readValue(message, Settlement.class);

            // Process the payout using PayoutService
            payoutService.processPayout(settlement);

            logger.info("Successfully processed payout for transaction: {}", settlement.getTransactionId());
        } catch (Exception e) {
            logger.error("Error processing payout message: {}", e.getMessage(), e);
        }
    }
} 