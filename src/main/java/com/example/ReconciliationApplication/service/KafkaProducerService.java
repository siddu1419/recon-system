package com.example.ReconciliationApplication.service;

import com.example.ReconciliationApplication.model.Settlement;
import com.example.ReconciliationApplication.model.SettlementRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending messages to Kafka topics.
 */
@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.settlements}")
    private String settlementsTopic;

    @Value("${kafka.topic.payouts}")
    private String payoutsTopic;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate,
                              ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a settlement record to the settlements topic.
     *
     * @param record The settlement record to send
     */
    public void sendSettlementMessage(SettlementRecord record) {
        try {
            String message = objectMapper.writeValueAsString(record);
            kafkaTemplate.send(settlementsTopic, record.getTransactionId(), message);
            logger.info("Sent settlement message for transaction: {}", record.getTransactionId());
        } catch (Exception e) {
            logger.error("Error sending settlement message: {}", e.getMessage(), e);
        }
    }

    /**
     * Sends a settlement to the payouts topic.
     *
     * @param settlement The settlement to send
     */
    public void sendPayoutMessage(Settlement settlement) {
        try {
            String message = objectMapper.writeValueAsString(settlement);
            kafkaTemplate.send(payoutsTopic, settlement.getTransactionId(), message);
            logger.info("Sent payout message for transaction: {}", settlement.getTransactionId());
        } catch (Exception e) {
            logger.error("Error sending payout message: {}", e.getMessage(), e);
        }
    }
} 