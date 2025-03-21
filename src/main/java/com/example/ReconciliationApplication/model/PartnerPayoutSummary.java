package com.example.ReconciliationApplication.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerPayoutSummary {
    private String partnerBankAccountId;
    private BigDecimal totalAmountPaid;
}
