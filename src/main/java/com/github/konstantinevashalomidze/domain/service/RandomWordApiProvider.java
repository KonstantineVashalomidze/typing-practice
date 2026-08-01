package com.github.konstantinevashalomidze.domain.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RandomWordApiProvider implements TextProvider {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final int wordCount;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RandomWordApiProvider(int wordCount) {
        this.wordCount = wordCount;
    }

    @Override
    public String getText() {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://random-word-api.herokuapp.com/word?number=%d".formatted(wordCount)))
                .build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            List<String> list = objectMapper.readValue(httpResponse.body(), new TypeReference<>() { });
            return String.join(" ", list);
        } catch (IOException | InterruptedException e) {
            return "";
        }
    }
}
