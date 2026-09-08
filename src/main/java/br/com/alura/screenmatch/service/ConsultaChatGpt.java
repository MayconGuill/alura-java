package br.com.alura.screenmatch.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Deprecated
public class ConsultaChatGpt {
    public static String obterTraducao(String texto) {
        var json = """
                {
                    "model": "gpt-4o-mini",
                    "input": "Traduza para português: %s"
                }
                """.formatted(texto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/responses"))
                .header("Authorization", "Bearer " + System.getenv("OPENAI_API_KEY"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
