package com.prime.acquiring.engine;

import com.prime.acquiring.jpa.entity.Transaction;
import com.prime.acquiring.jpa.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionOrchestrator {

    private final ValidationEngine validationEngine;
    private final RoutingEngine routingEngine;
    private final SwitchClient switchClient;
    private final IsoResponseFactory responseFactory;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public ISOMsg process(ISOMsg request) {

        long start = System.currentTimeMillis();

        try {

            // 1. Validate incoming ISO
            validationEngine.validate(request);

            // 2. Convert ISO → Entity
            Transaction transaction =
                    transactionMapper.fromIso(request);

            transaction.setStatus("RECEIVED");

            // 3. Persist initial transaction
            transactionRepository.save(transaction);

            // 4. Determine route
            String route = routingEngine.resolve(request);

            // 5. Send to switch
            ISOMsg switchResponse =
                    switchClient.send(route, request);

            // 6. Update transaction status
            transaction.setStatus("COMPLETED");
            transaction.setResponseCode(
                    switchResponse.getString(39)
            );

            transactionRepository.save(transaction);

            long end = System.currentTimeMillis();

            System.out.println("✅ Transaction completed in "
                    + (end - start) + " ms");

            return switchResponse;

        } catch (Exception ex) {

            ex.printStackTrace();

            return responseFactory.buildErrorResponse(
                    request,
                    "96"
            );
        }
    }
}