package com.prime.acquiring.controller;

import com.prime.acquiring.jpa.entity.TransactionJournal;
import com.prime.acquiring.jpa.repository.TransactionJournalRepository;
import com.prime.acquiring.model.IsoRequest;
import com.prime.acquiring.model.ResponseCode;
import com.prime.acquiring.model.TransactionStatus;
import com.prime.acquiring.service.TransactionService;
import com.prime.acquiring.util.RrnGenerator;
import com.prime.acquiring.util.StanGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/iso")
@RequiredArgsConstructor
public class IsoController {

    private final TransactionJournalRepository transactionJournalRepository;
    private final TransactionService transactionService;
    private final StanGenerator stanGenerator;
    private final RrnGenerator rrnGenerator;

    @PostMapping("/transaction")
    public ResponseEntity<?> createTransaction(@RequestBody IsoRequest request) {

        // 1. Generate identifiers
        String stan = stanGenerator.generate();
        String rrn = rrnGenerator.generate();

        TransactionJournal transactionJournal =
                TransactionJournal.builder()
                        .mti("0200")
                        .stan(stan)
                        .rrn(rrn)
                        .pan(request.getPan())
                        .processingCode(request.getProcessingCode())
                        .amount(request.getAmount())
                        .terminalId(request.getTerminalId())
                        .merchantId(request.getMerchantId())
                        .status(TransactionStatus.RECEIVED)
                        .reversed(false)
                        .build();

        // 3. Persist transaction
        transactionJournalRepository.save(transactionJournal);

        // 4. Send to processing engine
        var response = transactionService.process(transactionJournal);

        // 5. Update response info
        transactionJournal.setResponseCode(response.getResponseCode());

        transactionJournal.setStatus(
                response.getResponseCode().equals(ResponseCode.APPROVED)
                        ? TransactionStatus.COMPLETED
                        : TransactionStatus.FAILED
        );

        transactionJournalRepository.save(transactionJournal);
        return ResponseEntity.ok(response);
    }
}
