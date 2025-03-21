package com.example.ReconciliationApplication.repository;

import com.example.ReconciliationApplication.model.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    
    /**
     * Finds all settlements with the specified status.
     *
     * @param status The status to filter settlements by
     * @return List of settlements matching the status
     */
    List<Settlement> findByStatus(Settlement.SettlementStatus status);

    /**
     * Finds all settlements for a specific batch ID.
     *
     * @param batchId The batch ID to filter settlements by
     * @return List of settlements for the specified batch
     */
    List<Settlement> findByBatchId(String batchId);

    Optional<Settlement> findByTransactionId(String transactionId);
}