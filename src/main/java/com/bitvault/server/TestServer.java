package com.bitvault.server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TestServer {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        HttpClient httpClient = HttpClient
                .newBuilder()
                .build();


        for (int i = 0; i < 100; i++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://127.0.0.1:8080/"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.printf("Status = %s, body= %s\n",response.statusCode(),response.body());
        }


    }
}
