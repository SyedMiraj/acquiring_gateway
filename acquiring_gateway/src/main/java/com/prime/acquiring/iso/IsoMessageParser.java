package com.prime.acquiring.iso;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.stereotype.Component;

@Component
public class IsoMessageParser {

    public ISOMsg unpack(byte[] bytes) {
        try {
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(new ISO87APackager());
            isoMsg.unpack(bytes);
            return isoMsg;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}