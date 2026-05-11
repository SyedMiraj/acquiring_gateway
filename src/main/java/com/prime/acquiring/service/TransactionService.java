package com.prime.acquiring.service;

import com.prime.acquiring.jpa.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    public Object process(Transaction transaction) {

        // This will later call:
        // 1. Validation Engine
        // 2. Routing Engine
        // 3. Switch Client
        // 4. Response Builder

        transaction.setStatus("PROCESSING");

        // MOCK response for now
        transaction.setResponseCode("00");
        transaction.setStatus("APPROVED");

        return transaction;
    }
}