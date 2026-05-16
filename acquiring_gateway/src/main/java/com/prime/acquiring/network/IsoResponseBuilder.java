package com.prime.acquiring.network;

import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

@Component
public class IsoResponseBuilder {

    public byte[] pack(ISOMsg msg) throws Exception {
        return msg.pack();
    }
}
