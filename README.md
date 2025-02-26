Las diferencias entre la clase HttpURLConnection de Java 6 y Java 8 son principalmente relacionadas con mejoras de rendimiento, nuevas características y la introducción de otras tecnologías de red. Aquí te detallo algunos de los puntos clave:

1. Compatibilidad con HTTP/2
Java 6: La clase HttpURLConnection solo soporta HTTP/1.1. No tiene soporte para HTTP/2.
Java 8: Aunque HttpURLConnection todavía sigue utilizando HTTP/1.1 por defecto, a partir de Java 8 Update 40, se introdujo soporte para HTTP/2 a través de la API de HttpClient. Sin embargo, el HttpURLConnection tradicional no es compatible directamente con HTTP/2, pero el nuevo HttpClient (introducido en Java 9) sí lo es.
2. API de HttpClient (Java 9)
Java 6: Solo puedes usar HttpURLConnection para realizar solicitudes HTTP. Es una API relativamente básica y no permite realizar algunas tareas avanzadas como manejar solicitudes asincrónicas, configurar proxies de forma más flexible, etc.
Java 8: Aunque no se introdujo oficialmente el HttpClient en Java 8, existen mejoras en la capacidad de red y se recomienda el uso de bibliotecas adicionales como Apache HttpClient o las nuevas clases de java.net.http que aparecerían en Java 9.
3. Manejo de Proxy
Java 6: El manejo de proxies con HttpURLConnection era limitado, y no era tan flexible para configuraciones avanzadas.
Java 8: Mejoras en el soporte de proxies. Aunque HttpURLConnection seguía siendo la principal clase para establecer conexiones HTTP, con Java 8 podías gestionar proxies de una forma más flexible utilizando configuraciones globales con la propiedad java.net.Proxy o mediante métodos de conexión que permitían especificar proxies manualmente.
4. Soporte para Cookies
Java 6: El manejo de cookies con HttpURLConnection era básico. Tenías que gestionar manualmente las cookies que se enviaban y recibían.
Java 8: Mejoras en el manejo de cookies. Podías usar nuevas clases de utilidad como CookieManager para manejar las cookies de manera más eficiente.
5. Mejoras en el rendimiento y optimización
Java 6: Si bien HttpURLConnection era funcional, no estaba tan optimizado en términos de rendimiento y características modernas.
Java 8: A medida que avanzaba la versión, hubo mejoras en el rendimiento de la red, y las conexiones HTTP se gestionaban de manera más eficiente.
6. Manejo de excepciones y errores
Java 6: El manejo de excepciones al trabajar con HttpURLConnection era más básico y no proporcionaba mucha información adicional sobre errores de red.
Java 8: Mejor manejo de excepciones. Aunque no hubo cambios drásticos en HttpURLConnection específicamente, la introducción de HttpClient en Java 9 mejoró el manejo de excepciones para las conexiones HTTP.
7. Soporte para HTTPS
Java 6: El soporte para conexiones HTTPS estaba disponible, pero requería configuraciones más específicas, como la configuración de los SSLContext.
Java 8: Mejor soporte para HTTPS, y en general, el proceso de configuración y manejo de conexiones seguras fue más sencillo.
8. Mayor compatibilidad con bibliotecas externas
Java 6: El uso de bibliotecas de terceros como Apache HttpClient era común para realizar solicitudes más complejas, ya que HttpURLConnection era limitado.
Java 8: Aunque HttpURLConnection mejoró, las bibliotecas como Apache HttpClient o OkHttp seguían siendo populares. La gran novedad en Java 8 fue la capacidad de trabajar más fácilmente con estas bibliotecas y su integración con nuevas funcionalidades de red.



-----------------------


package com.ceiba.biblioteca.infraestructure.service;

import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpService {

    public Mono<String> pruebaServicio(String valor){
        OutputStream os = null;  //se maneja la entrada y salida de datos en la solicitud HTTP.
        BufferedReader in = null;

        try {
            String urlString = "http://localhost:8081/prestamo";
            URL url = new URL(urlString);  //contiene la URL del servicio web al que se hará la solicitud

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  //Se establece una conexión HTTP a la URL indicada utilizando

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");  //especifica que se acepta una respuesta en formato JSON.
            connection.setRequestProperty("Content-Type", "application/json"); //especifica que el cuerpo de la solicitud será JSON
            connection.setConnectTimeout(50000);  //Se configuran los tiempos de espera para la conexión
            connection.setReadTimeout(50000);     //Se configuran los tiempos de espera para la conexión
            connection.setDoOutput(true);  //Se indica que la conexión permitirá la escritura de datos

            String cuerpo = query(valor);  //Se construye el Json del cuerpo

            os = connection.getOutputStream(); // Open the OutputStream
            byte[] input = cuerpo.getBytes("utf-8");
            os.write(input, 0, input.length); // Write to the OutputStream

            int responseCode = connection.getResponseCode();  // Se obtiene el codigo de response
            System.out.println("Código de respuesta prueba: " + responseCode);

            // Leer la respuesta del servicio
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            //Se declaran dos variables: inputLine para almacenar temporalmente cada línea de la respuesta, y response para construir la respuesta completa
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


