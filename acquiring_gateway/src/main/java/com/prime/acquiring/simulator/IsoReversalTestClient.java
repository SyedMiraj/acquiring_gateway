package com.prime.acquiring.simulator;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class IsoReversalTestClient {

    public static void main(String[] args)
            throws Exception {

        URL url = new URL("http://localhost:8080/reversal");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        String json = """
                {
                    "stan":"123456",
                    "rrn":"260516100001"
                }
                """;

        try (OutputStream os = connection.getOutputStream()) {
            os.write(json.getBytes());
        }

        int responseCode = connection.getResponseCode();
        System.out.println("HTTP Response Code : " + responseCode);
    }
}