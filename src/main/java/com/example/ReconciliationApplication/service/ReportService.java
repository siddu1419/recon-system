package com.example.ReconciliationApplication.service;


import com.example.ReconciliationApplication.model.*;
import com.example.ReconciliationApplication.repository.PayoutRepository;
import com.example.ReconciliationApplication.repository.SettlementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportService {

    private final SettlementRepository settlementRepository;
    private final PayoutRepository payoutRepository;

    @Autowired
    public ReportService(SettlementRepository settlementRepository, PayoutRepository payoutRepository) {
        this.settlementRepository = settlementRepository;
        this.payoutRepository = payoutRepository;
    }

    /**
     * Get statistics for a specific batch.
     *
     * @param batchId The batch ID to get statistics for.
     * @return A BatchStatistics object containing the statistics.
     */
    public BatchStatistics getBatchStatistics(String batchId) {
        List<Settlement> settlements = settlementRepository.findByBatchId(batchId);

        long totalRecords = settlements.size();
        BigDecimal totalAmountReceived = settlements.stream()
                .map(Settlement::getSettledAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long settledCount = settlements.stream()
                .filter(s -> s.getStatus() == Settlement.SettlementStatus.SUCCESS)
                .count();

        return new BatchStatistics(batchId, totalRecords, totalAmountReceived, settledCount);
    }

    /**
     * Get the summary of reconciled and non-reconciled amounts for a specific batch.
     *
     * @param batchId The batch ID to get the summary for.
     * @return A ReconciliationSummary object containing the summary.
     */
    public ReconciliationSummary getReconciliationSummary(String batchId) {
        List<Settlement> settlements = settlementRepository.findByBatchId(batchId);

        BigDecimal totalReconciled = settlements.stream()
                .filter(s -> s.getStatus() == Settlement.SettlementStatus.SUCCESS)
                .map(Settlement::getSettledAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalNonReconciled = settlements.stream()
                .filter(s -> s.getStatus() != Settlement.SettlementStatus.SUCCESS)
                .map(Settlement::getSettledAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReconciliationSummary(batchId, totalReconciled, totalNonReconciled);
    }

    /**
     * Get agent-wise payout records for a specific batch.
     *
     * @param batchId The batch ID to get payout records for.
     * @return A list of PartnerPayoutSummary objects.
     */
    public List<PartnerPayoutSummary> getAgentPayoutSummary(String batchId) {
        List<Payout> payouts = payoutRepository.findByBatchId(batchId);

        Map<String, BigDecimal> partnerPayouts = payouts.stream()
                .collect(Collectors.groupingBy(
                        Payout::getPartnerBankAccountId,
                        Collectors.reducing(BigDecimal.ZERO, Payout::getAmountToBePaidForPartner, BigDecimal::add)
                ));

        return partnerPayouts.entrySet().stream()
                .map(entry -> new PartnerPayoutSummary(entry.getKey(), entry.getValue()))
                .toList();
    }
}
