package com.prime.acquiring.parser;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.stereotype.Component;

@Component
public class IsoParser {

    private final ISO87APackager packager = new ISO87APackager();

    public ISOMsg unpack(byte[] data) throws Exception {

        ISOMsg msg = new ISOMsg();
        msg.setPackager(packager);
        msg.unpack(data);

        return msg;
    }
}