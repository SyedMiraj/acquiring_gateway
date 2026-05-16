package com.prime.acquiring.simulator;

import com.prime.acquiring.util.RrnGenerator;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class IsoTestClient {

    public static void main(String[] args) throws Exception {

        // Connect to Acquirer TCP server
        Socket socket = new Socket("localhost", 5000);

        RrnGenerator rrnGenerator = new RrnGenerator();

        ISOMsg request = new ISOMsg();
        request.setPackager(new ISO87APackager());
        request.setMTI("0200");
        request.set(2, "4111111111111111");
        request.set(3, "000000");
        request.set(4, "000000005000");
        request.set(7, "0707103030");
        request.set(11, "123456");
        request.set(41, "ATM12345");
        request.set(37, rrnGenerator.generate());
        request.set(42, "MERCHANT01");

        byte[] packed = request.pack();
        OutputStream out = socket.getOutputStream();

        out.write(packed);
        out.flush();
        System.out.println("ISO Request Sent");

        InputStream in = socket.getInputStream();
        byte[] responseBytes = in.readAllBytes();

        ISOMsg response = new ISOMsg();
        response.setPackager(new ISO87APackager());
        response.unpack(responseBytes);

        System.out.println("Response MTI: " + response.getMTI());
        System.out.println("Response Code: " + response.getString(39));

        System.out.println("Auth Code: " + response.getString(38));
        socket.close();
    }
}