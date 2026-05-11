package com.prime.acquiring.engine;

import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SwitchClient {

    public ISOMsg send(String route, ISOMsg request)
            throws Exception {

        log.info("📡 Sending transaction to {}", route);

        // MOCK SWITCH RESPONSE
        ISOMsg response = (ISOMsg) request.clone();

        response.setMTI("0210");
        response.set(39, "00");
        response.set(38, "AUTH01");

        return response;
    }
}
