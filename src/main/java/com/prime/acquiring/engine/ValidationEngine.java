package com.prime.acquiring.engine;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

@Component
public class ValidationEngine {

    public void validate(ISOMsg msg) throws ISOException {

        if (msg.getMTI() == null) {
            throw new RuntimeException("Invalid MTI");
        }

        if (!msg.hasField(2)) {
            throw new RuntimeException("Missing PAN");
        }

        if (!msg.hasField(4)) {
            throw new RuntimeException("Missing Amount");
        }

        if (!msg.hasField(11)) {
            throw new RuntimeException("Missing STAN");
        }

        if (!msg.hasField(41)) {
            throw new RuntimeException("Missing Terminal ID");
        }
    }
}
