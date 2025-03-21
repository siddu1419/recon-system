package com.example.ReconciliationApplication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a settlement record from the CSV file.
 * This class is used for deserializing settlement data received from Kafka.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettlementRecord {
    /**
     * The unique identifier of the transaction
     */
    private String transactionId;

    /**
     * The ID of the user associated with the transaction
     */
    private String userId;

    /**
     * The amount of the settlement
     */
    private BigDecimal amount;

    /**
     * The ID of the user's bank account
     */
    private String userBankAccountId;

    /**
     * The ID of the partner's bank account
     */
    private String partnerBankAccountId;

    private String batchId;
}
