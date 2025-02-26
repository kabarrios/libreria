package com.ceiba.biblioteca.infraestructure.service;

import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpService {

    public Mono<String> pruebaServicio(String valor){
        OutputStream os = null;
        BufferedReader in = null;

        try {
            String urlString = "http://localhost:8081/prestamo";
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);
            connection.setDoOutput(true);

            String cuerpo = query(valor);

            os = connection.getOutputStream(); // Open the OutputStream
            byte[] input = cuerpo.getBytes("utf-8");
            os.write(input, 0, input.length); // Write to the OutputStream

            int responseCode = connection.getResponseCode();
            System.out.println("Código de respuesta prueba: " + responseCode);

            // Leer la respuesta del servicio
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            System.out.println("Respuesta del servicio: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure OutputStream and BufferedReader are closed in the finally block
            try {
                if (os != null) {
                    os.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Mono.just("Response");
    }

    public String query(String valor) {
        StringBuilder builder = new StringBuilder();

        builder.append("{\n");
        builder.append("  \"header\": {},\n");
        builder.append("  \"properties\": {},\n");
        builder.append("  \"body\": {\n");
        builder.append("    \"xml\": \"").append(valor).append("\"\n");
        builder.append("  }\n");
        builder.append("}");

        return builder.toString();
    }
}
