package com.prime.acquiring.iso;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

public class IsoLogger {

    public static void log(ISOMsg isoMsg, String title) {
        try {
            System.out.println();
            System.out.println("====================================");
            System.out.println(title);
            System.out.println("====================================");

            System.out.println("MTI : " + isoMsg.getMTI());
            // Print fields
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.println("Field-" + i + " : " + isoMsg.getString(i));
                }
            }

            // Print raw HEX
            System.out.println();
            System.out.println("RAW ISO HEX : " + ISOUtil.hexString(isoMsg.pack()));
            System.out.println("====================================");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}