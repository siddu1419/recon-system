package com.example.ReconciliationApplication.controller;

import com.example.ReconciliationApplication.service.FileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/reconciliation")
public class ReconciliationController {

    private final FileProcessingService fileProcessingService;

    @Autowired
    public ReconciliationController(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    /**
     * Handles the upload of a settlement file, and initiates the reconciliation process.
     *
     * @param file The CSV file containing settlement records.
     * @return ResponseEntity containing the batch ID and success message.
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadSettlementFile(@RequestParam("file") MultipartFile file) {
        try {
            // Generate batch ID
            String batchId = file.getOriginalFilename() + "_" + LocalDateTime.now();

            // Process file directly (without saving)
            fileProcessingService.processFileRowByRow(file, batchId);

            return ResponseEntity.ok(Map.of(
                    "batchId", batchId,
                    "message", "File processed successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Failed to process file: " + e.getMessage()
            ));
        }
    }
}