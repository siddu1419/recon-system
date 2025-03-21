package com.example.ReconciliationApplication.service;

import com.example.ReconciliationApplication.model.Settlement;
import com.example.ReconciliationApplication.repository.SettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettlementService {

    private final SettlementRepository settlementRepository;

    @Autowired
    public SettlementService(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    /**
     * Retrieves all settlements with the specified status.
     *
     * @param status The status to filter settlements by
     * @return List of settlements matching the status
     */
    public List<Settlement> getSettlementsByStatus(Settlement.SettlementStatus status) {
        return settlementRepository.findByStatus(status);
    }

    /**
     * Retrieves all settlements for a specific batch ID.
     *
     * @param batchId The batch ID to filter settlements by
     * @return List of settlements for the specified batch
     */
    public List<Settlement> getSettlementsByBatchId(String batchId) {
        return settlementRepository.findByBatchId(batchId);
    }
} 