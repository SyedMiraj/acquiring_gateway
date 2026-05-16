package com.prime.acquiring.engine;

import com.prime.acquiring.jpa.entity.TransactionJournal;
import org.springframework.stereotype.Component;

@Component
public class ValidationEngine {

    public void validate(TransactionJournal transactionJournal) {

        if (transactionJournal.getMti() == null || transactionJournal.getMti().isBlank()) {
            throw new RuntimeException("Invalid MTI");
        }

        if (transactionJournal.getPan() == null || transactionJournal.getPan().isBlank()) {
            throw new RuntimeException("Missing PAN");
        }

        if (transactionJournal.getProcessingCode() == null || transactionJournal.getProcessingCode().isBlank()) {
            throw new RuntimeException("Missing Processing Code");
        }

        if (transactionJournal.getAmount() == null || transactionJournal.getAmount().isBlank()) {
            throw new RuntimeException("Missing Amount");
        }

        if (transactionJournal.getStan() == null || transactionJournal.getStan().isBlank()) {
            throw new RuntimeException("Missing STAN");
        }

        if (transactionJournal.getRrn() == null || transactionJournal.getRrn().isBlank()) {
            throw new RuntimeException("Missing RRN");
        }

        if (transactionJournal.getTerminalId() == null || transactionJournal.getTerminalId().isBlank()) {
            throw new RuntimeException("Missing Terminal ID");
        }

        if (transactionJournal.getMerchantId() == null || transactionJournal.getMerchantId().isBlank()) {
            throw new RuntimeException("Missing Merchant ID");
        }
    }
}