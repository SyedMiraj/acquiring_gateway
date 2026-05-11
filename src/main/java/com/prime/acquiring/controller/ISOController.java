package com.prime.acquiring.controller;

import com.prime.acquiring.jpa.entity.Transaction;
import com.prime.acquiring.jpa.repository.TransactionRepository;
import com.prime.acquiring.model.IsoRequest;
import com.prime.acquiring.service.TransactionService;
import com.prime.acquiring.util.RrnGenerator;
import com.prime.acquiring.util.StanGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/iso")
@RequiredArgsConstructor
public class ISOController {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final StanGenerator stanGenerator;
    private final RrnGenerator rrnGenerator;

    @PostMapping("/transaction")
    public ResponseEntity<?> createTransaction(@RequestBody IsoRequest request) {

        // 1. Generate identifiers
        String stan = stanGenerator.generate();
        String rrn = rrnGenerator.generate();

        // 2. Persist transaction BEFORE processing (banking style)
        Transaction transaction = Transaction.builder()
                .stan(stan)
                .rrn(rrn)
                .pan(request.getPan())
                .amount(request.getAmount())
                .terminalId(request.getTerminalId())
                .processingCode(request.getProcessingCode())
                .status("RECEIVED")
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        // 3. Send to core processing engine
        var response = transactionService.process(transaction);

        return ResponseEntity.ok(response);
    }
}
