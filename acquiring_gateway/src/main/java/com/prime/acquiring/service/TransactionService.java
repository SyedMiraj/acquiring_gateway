package com.prime.acquiring.service;

import com.prime.acquiring.engine.SwitchClient;
import com.prime.acquiring.engine.ValidationEngine;
import com.prime.acquiring.iso.IsoLogger;
import com.prime.acquiring.iso.IsoMessageBuilder;
import com.prime.acquiring.iso.IsoMessageParser;
import com.prime.acquiring.jpa.entity.TransactionJournal;
import com.prime.acquiring.model.ResponseCode;
import com.prime.acquiring.model.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final ValidationEngine validationEngine;
    private final IsoMessageBuilder isoMessageBuilder;
    private final IsoMessageParser isoMessageParser;
    private final SwitchClient switchClient;

    public TransactionJournal process(TransactionJournal transactionJournal) {

        long start = System.currentTimeMillis();

        try {
            transactionJournal.setStatus(TransactionStatus.PROCESSING);
            validationEngine.validate(transactionJournal);
            ISOMsg isoRequest = isoMessageBuilder.build(transactionJournal);

            IsoLogger.log(isoRequest, "ISO SENT TO SWITCH");

            byte[] switchResponse = switchClient.send(isoRequest.pack());
            ISOMsg isoResponse = isoMessageParser.unpack(switchResponse);

            IsoLogger.log(isoResponse, "ISO RESPONSE FROM SWITCH");

            transactionJournal.setResponseCode(isoResponse.getString(39));
            transactionJournal.setStatus(
                    ResponseCode.APPROVED.equals(isoResponse.getString(39))
                            ? TransactionStatus.COMPLETED
                            : TransactionStatus.DECLINED
            );

            long end = System.currentTimeMillis();

            System.out.println("Transaction completed in " + (end - start)+ " ms");
            return transactionJournal;
        } catch (Exception e) {
            e.printStackTrace();
            transactionJournal.setResponseCode("96");
            transactionJournal.setStatus(TransactionStatus.FAILED);
            return transactionJournal;
        }
    }
}