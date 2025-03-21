package com.example.ReconciliationApplication.model;


import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payouts")
public class Payout {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String payoutId;

    private String settlementId;
    private BigDecimal amountToBePaidForPartner;
    private LocalDateTime payoutTime;

    @Enumerated(EnumType.STRING)
    private PayoutStatus status;

    private int retryCount;
    private String partnerBankAccountId;
    private String batchId;

    public enum PayoutStatus {
        PENDING, FAILED, SUCCESS
    }
}