package com.example.ReconciliationApplication.controller;


import com.example.ReconciliationApplication.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reconciliation")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Endpoint to get statistics for a specific batch.
     *
     * @param batchId The batch ID to get statistics for.
     * @return A response containing the statistics.
     */
    @GetMapping("/statistics/{batchId}")
    public ResponseEntity<?> getBatchStatistics(@PathVariable String batchId) {
        try {
            return ResponseEntity.ok(reportService.getBatchStatistics(batchId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to fetch statistics: " + e.getMessage());
        }
    }


    /**
     * Endpoint to get the summary of reconciled and non-reconciled amounts for a specific batch.
     *
     * @param batchId The batch ID to get the summary for.
     * @return A response containing the summary.
     */
    @GetMapping("/summary/{batchId}")
    public ResponseEntity<?> getReconciliationSummary(@PathVariable String batchId) {
        try {
            return ResponseEntity.ok(reportService.getReconciliationSummary(batchId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to fetch summary: " + e.getMessage());
        }
    }


    /**
     * Endpoint to get agent-wise payout records for a specific batch.
     *
     * @param batchId The batch ID to get payout records for.
     * @return A response containing the agent-wise payout records.
     */
    @GetMapping("/payout-summary/{batchId}")
    public ResponseEntity<?> getAgentPayoutSummary(@PathVariable String batchId) {
        try {
            return ResponseEntity.ok(reportService.getAgentPayoutSummary(batchId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to fetch payout summary: " + e.getMessage());
        }
    }
}
