package com.example.ReconciliationApplication.repository;


import com.example.ReconciliationApplication.model.Payout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, String> {
    List<Payout> findByStatus(Payout.PayoutStatus status);
    List<Payout> findByBatchId(String batchId);

}