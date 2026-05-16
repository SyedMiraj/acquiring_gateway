package com.prime.acquiring.parser;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.stereotype.Component;

@Component
public class IsoParser {

    private final ISO87APackager packager = new ISO87APackager();

    public ISOMsg unpack(byte[] requestBytes) {
        try {
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(new ISO87APackager());
            isoMsg.unpack(requestBytes);
            logIsoMessage(isoMsg);
            return isoMsg;
        } catch (Exception e) {
            System.out.println("Failed to parse ISO8583 message");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void logIsoMessage(ISOMsg isoMsg) {
        try {
            System.out.println("================ ISO MESSAGE ================");
            System.out.println("MTI : " + isoMsg.getMTI());

            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.println("Field-" + i + " : " + isoMsg.getString(i));
                }
            }
            System.out.println("=============================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}