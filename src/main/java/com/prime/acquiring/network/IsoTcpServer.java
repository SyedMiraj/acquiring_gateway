package com.prime.acquiring.network;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@RequiredArgsConstructor
public class IsoTcpServer {

    private final IsoRequestHandler requestHandler;

    private static final int PORT = 5000;

    @PostConstruct
    public void startServer() {
        new Thread(this::runServer).start();
    }

    private void runServer() {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("ISO TCP Server started on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("📡 Connection received from: " + socket.getInetAddress());

                // Handle each request in separate thread
                new Thread(() -> requestHandler.handle(socket)).start();
            }

        } catch (IOException e) {
            throw new RuntimeException("TCP Server failed", e);
        }
    }
}
