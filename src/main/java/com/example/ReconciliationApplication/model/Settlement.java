package com.example.ReconciliationApplication.model;


import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "settlements")
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String settlementId;

    private String transactionId;
    private BigDecimal settledAmount;
    private String userId;

    private String userBankAccountId;
    private String partnerBankAccountId;

    @Enumerated(EnumType.STRING)
    private SettlementStatus status;

    private LocalDateTime settledAt;
    private String batchId;
    private String comments;

    public enum SettlementStatus {
        PENDING, FAILED, SUCCESS
    }
}
