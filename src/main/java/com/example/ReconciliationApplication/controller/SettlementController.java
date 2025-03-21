package com.example.ReconciliationApplication.controller;

import com.example.ReconciliationApplication.model.Settlement;
import com.example.ReconciliationApplication.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    @Autowired
    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    /**
     * Retrieves all settlements with the specified status.
     *
     * @param status The status to filter settlements by (PENDING, RECONCILED, FAILED)
     * @return ResponseEntity containing a list of settlements matching the status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Settlement>> getSettlementsByStatus(@PathVariable Settlement.SettlementStatus status) {
        List<Settlement> settlements = settlementService.getSettlementsByStatus(status);
        return ResponseEntity.ok(settlements);
    }

    /**
     * Retrieves all settlements for a specific batch ID.
     *
     * @param batchId The batch ID to filter settlements by
     * @return ResponseEntity containing a list of settlements for the specified batch
     */
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<Settlement>> getSettlementsByBatchId(@PathVariable String batchId) {
        List<Settlement> settlements = settlementService.getSettlementsByBatchId(batchId);
        return ResponseEntity.ok(settlements);
    }
} 