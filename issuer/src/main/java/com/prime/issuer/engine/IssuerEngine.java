package com.prime.issuer.engine;

import com.prime.issuer.iso.IsoResponseFactory;
import com.prime.issuer.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class IssuerOrchestrator {

    private final AccountService accountService;
    private final IsoResponseFactory responseFactory;

    public ISOMsg process(ISOMsg request) {
        try {
            String pan = request.getString(2);
            String amountField = request.getString(4);
            BigDecimal amount = new BigDecimal(amountField);

            boolean approved = accountService.authorize(pan, amount);
            if (approved) {
                return responseFactory.buildApprovedResponse(request);
            }

            return responseFactory.buildDeclinedResponse(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
