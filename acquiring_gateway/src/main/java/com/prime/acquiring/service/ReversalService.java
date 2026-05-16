package com.prime.acquiring.service;

import com.prime.acquiring.engine.SwitchClient;
import com.prime.acquiring.iso.IsoLogger;
import com.prime.acquiring.jpa.entity.TransactionJournal;
import com.prime.acquiring.jpa.repository.TransactionJournalRepository;
import com.prime.acquiring.model.ResponseCode;
import com.prime.acquiring.model.ReversalRequest;
import com.prime.acquiring.model.TransactionStatus;
import com.prime.acquiring.parser.IsoParser;
import lombok.RequiredArgsConstructor;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReversalService {

    private final TransactionJournalRepository repository;
    private final SwitchClient switchClient;
    private final IsoParser isoParser;

    public ISOMsg reverse(ReversalRequest request) throws Exception {

        TransactionJournal tx = repository.findByStanAndRrn(request.getStan(), request.getRrn())
                        .orElseThrow(() -> new RuntimeException("Original transaction not found"));

        ISOMsg reversal = new ISOMsg();
        reversal.setPackager(new ISO87APackager());
        reversal.setMTI("0400");
        reversal.set(2, tx.getPan());
        reversal.set(3, tx.getProcessingCode());
        reversal.set(4, tx.getAmount());
        reversal.set(11, tx.getStan());
        reversal.set(37, tx.getRrn());
        reversal.set(41, tx.getTerminalId());
        reversal.set(42, tx.getMerchantId());

        IsoLogger.log(reversal, "REVERSAL SENT TO SWITCH");

        byte[] responseBytes = switchClient.send(reversal.pack());

        ISOMsg response = isoParser.unpack(responseBytes);

        IsoLogger.log(response,"REVERSAL RESPONSE");

        if (ResponseCode.APPROVED.equals(response.getString(39))) {
            tx.setReversed(true);
            tx.setStatus(TransactionStatus.REVERSED);
            repository.save(tx);
        }
        return response;
    }
}