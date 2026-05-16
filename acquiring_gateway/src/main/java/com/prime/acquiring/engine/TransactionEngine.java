package com.prime.acquiring.engine;

import com.prime.acquiring.iso.IsoLogger;
import com.prime.acquiring.jpa.entity.TransactionJournal;
import com.prime.acquiring.jpa.repository.TransactionJournalRepository;
import com.prime.acquiring.model.ResponseCode;
import com.prime.acquiring.model.TransactionStatus;
import com.prime.acquiring.parser.IsoParser;
import lombok.RequiredArgsConstructor;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEngine {

    private final ValidationEngine validationEngine;
    private final RoutingEngine routingEngine;
    private final SwitchClient switchClient;
    private final IsoResponseFactory responseFactory;
    private final TransactionJournalRepository transactionJournalRepository;
    private final TransactionMapper transactionMapper;
    private final IsoParser isoParser;

    public ISOMsg process(ISOMsg request) {

        long start = System.currentTimeMillis();

        try {
            System.out.println("Acquirer Transaction Started");

            // 1. Validate ISO request
            validationEngine.validate(transactionMapper.fromIso(request));

            // 2. Convert ISO → Transaction Journal
            TransactionJournal transactionJournal = transactionMapper.fromIso(request);
            transactionJournal.setStatus(TransactionStatus.RECEIVED);

            // 3. Persist transaction
            transactionJournalRepository.save(transactionJournal);

            System.out.println("Transaction persisted");

            // 4. Resolve route
            String route = routingEngine.resolve(request);
            System.out.println("Route resolved: " + route);

            // 5. Log outgoing ISO
            IsoLogger.log(request, "ISO SENT TO SWITCH");

            // 6. Send request to switch
            byte[] switchResponse = switchClient.send(request.pack());
            System.out.println("Response received from Switch");

            // 7. Parse switch response
            ISOMsg isoResponse = isoParser.unpack(switchResponse);

            // 8. Log switch response
            IsoLogger.log(isoResponse, "ISO RESPONSE FROM SWITCH");

            // 9. Update transaction journal
            transactionJournal.setResponseCode(isoResponse.getString(39));

            // REVERSAL RESPONSE
            if ("0410".equals(isoResponse.getMTI())) {
                transactionJournal.setStatus(TransactionStatus.REVERSED);
                transactionJournal.setReversed(true);
            } else {
                transactionJournal.setStatus(ResponseCode.APPROVED.equals(isoResponse.getString(39)) ? TransactionStatus.COMPLETED : TransactionStatus.DECLINED);
                transactionJournalRepository.save(transactionJournal);
            }

            long end = System.currentTimeMillis();
            System.out.println("Transaction completed in " + (end - start) + " ms");
            return isoResponse;

        } catch (Exception ex) {
            System.out.println("Transaction failed");
            ex.printStackTrace();
            return responseFactory.buildErrorResponse(request, "96");
        }
    }
}