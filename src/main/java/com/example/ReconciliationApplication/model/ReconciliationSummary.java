package com.example.ReconciliationApplication.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReconciliationSummary {
    private String batchId;
    private BigDecimal totalReconciled;
    private BigDecimal totalNonReconciled;

}
