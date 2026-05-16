package com.prime.acquiring.engine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@Slf4j
@Component
public class SwitchClient {

    private static final String HOST = "localhost";
    private static final int PORT = 6000;

    public byte[] send(byte[] requestBytes) {
        try {
            System.out.println("Connecting to Switch...");

            try (Socket socket = new Socket(HOST, PORT);
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream()) {

                // Send ISO request
                out.write(requestBytes);
                out.flush();
                System.out.println("ISO request sent to Switch");

                // Read response
                byte[] buffer = new byte[4096];
                int length = in.read(buffer);

                if (length == -1) {
                    throw new RuntimeException("No response from Switch");
                }

                byte[] responseBytes = new byte[length];
                System.arraycopy(buffer, 0, responseBytes, 0, length);

                System.out.println("Response received from Switch");
                return responseBytes;
            }
        } catch (Exception e) {
            System.out.println("Failed to connect with Switch");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
