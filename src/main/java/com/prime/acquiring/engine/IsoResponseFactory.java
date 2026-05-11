package com.prime.acquiring.engine;

import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

@Component
public class IsoResponseFactory {

    public ISOMsg buildErrorResponse(
            ISOMsg request,
            String responseCode
    ) {

        try {

            ISOMsg response = (ISOMsg) request.clone();

            response.setMTI("0210");
            response.set(39, responseCode);

            return response;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
