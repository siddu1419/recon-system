package com.example.ReconciliationApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BatchStatistics {
    private String batchId;
    private long totalRecords;
    private BigDecimal totalAmountReceived;
    private long settledCount;
}
