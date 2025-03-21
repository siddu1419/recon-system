package com.example.ReconciliationApplication.service;

import com.example.ReconciliationApplication.model.Settlement;
import com.example.ReconciliationApplication.model.SettlementRecord;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service responsible for processing reconciliation settlement files.
 * It reads CSV files row by row, extracts settlement records, and sends them for further processing.
 */
@Service
public class FileProcessingService {

    private final KafkaProducerService kafkaProducerService;

    /**
     * Constructor-based dependency injection.
     *
     * @param kafkaProducerService The Kafka producer service to send settlement records for processing.
     */
    @Autowired
    public FileProcessingService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Processes the uploaded CSV file row by row.
     * Extracts settlement records and sends them for processing via Kafka.
     *
     * @param file    The uploaded CSV file (MultipartFile).
     * @param batchId A unique identifier for tracking settlements in the batch.
     * @throws RuntimeException If an error occurs while reading or processing the file.
     */
    public void processFileRowByRow(MultipartFile file, String batchId) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            boolean isHeader = true;

            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip header row
                    continue;
                }

                // Parse and send settlement record directly
                SettlementRecord record = parseSettlementRecord(line,batchId);
                kafkaProducerService.sendSettlementMessage(record);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error processing file: " + e.getMessage(), e);
        }
    }

    /**
     * Parses a row from the CSV file into a SettlementRecord object.
     *
     * @param line A single row from the CSV file as a string array.
     * @return A populated SettlementRecord object.
     */
    private SettlementRecord parseSettlementRecord(String[] line,String batchId) {
        SettlementRecord record = new SettlementRecord();
        record.setTransactionId(line[0].trim());
        record.setUserId(line[1].trim());
        record.setAmount(new BigDecimal(line[2].trim()));
        record.setUserBankAccountId(line[3].trim());
        record.setPartnerBankAccountId(line[4].trim());
        record.setBatchId(batchId);
        return record;
    }
}
