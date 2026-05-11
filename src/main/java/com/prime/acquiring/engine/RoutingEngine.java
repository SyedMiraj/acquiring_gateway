package com.prime.acquiring.engine;

import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

@Component
public class RoutingEngine {

    public String resolve(ISOMsg request) {

        String pan = request.getString(2);

        if (pan.startsWith("4")) {
            return "VISA_SWITCH";
        }

        if (pan.startsWith("5")) {
            return "MASTERCARD_SWITCH";
        }

        return "LOCAL_SWITCH";
    }
}