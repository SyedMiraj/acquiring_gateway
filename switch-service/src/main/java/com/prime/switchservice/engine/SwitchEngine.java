package com.prime.switchservice.engine;

import com.prime.switchservice.client.IssuerClient;
import com.prime.switchservice.iso.IsoMessageParser;
import lombok.RequiredArgsConstructor;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SwitchOrchestrator {

    private final IsoMessageParser isoMessageParser;
    private final RoutingEngine routingEngine;
    private final IssuerClient issuerClient;

    public byte[] process(byte[] requestBytes) {

        try {
            System.out.println("Switch Orchestrator started");

            // 1. Parse ISO request
            ISOMsg isoRequest = isoMessageParser.unpack(requestBytes);
            System.out.println("MTI = " + isoRequest.getMTI());
            System.out.println("RAW = " + ISOUtil.hexString(isoRequest.pack()));

            // 2. Determine route
            String route = routingEngine.route(isoRequest);
            System.out.println("Routed to: " + route);

            // 3. Forward to issuer
            byte[] issuerResponse = issuerClient.send(route, requestBytes);
            System.out.println("Response received from Issuer");

            // 4. Return response back to Acquirer
            return issuerResponse;
        } catch (Exception e) {
            System.out.println("Error in Switch Orchestrator");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
