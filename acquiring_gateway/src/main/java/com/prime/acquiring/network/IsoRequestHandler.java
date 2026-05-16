package com.prime.acquiring.network;

import com.prime.acquiring.parser.IsoParser;
import com.prime.acquiring.engine.TransactionEngine;
import lombok.RequiredArgsConstructor;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@Component
@RequiredArgsConstructor
public class IsoRequestHandler {

    private final IsoParser isoParser;
    private final TransactionEngine transactionEngine;
    private final IsoResponseBuilder responseBuilder;

    public void handle(Socket socket) {

        try (socket;
             InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            System.out.println("Waiting for ISO message...");

            byte[] buffer = new byte[4096];
            int length = in.read(buffer);

            if (length == -1) {
                System.out.println("No data received");
                return;
            }

            System.out.println("ISO raw bytes received: " + length);

            byte[] requestBytes = new byte[length];
            System.arraycopy(buffer, 0, requestBytes, 0, length);

            // 2. Parse ISO message
            ISOMsg isoRequest = isoParser.unpack(requestBytes);

            System.out.println("ISO parsed successfully. MTI = " + isoRequest.getMTI());

            // 3. Process transaction
            ISOMsg isoResponse = transactionEngine.process(isoRequest);

            System.out.println("Transaction processed");

            // 4. Build response bytes
            byte[] responseBytes = responseBuilder.pack(isoResponse);

            // 5. Send response
            out.write(responseBytes);
            out.flush();
            System.out.println("Response sent to POS");
        } catch (Exception e) {
            System.out.println("Error in ISO handler");
            e.printStackTrace();
        }
    }
}
