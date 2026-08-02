package com.github.konstantinevashalomidze.domain.service.ai;

import com.github.konstantinevashalomidze.config.Config;
import com.github.konstantinevashalomidze.domain.service.TextProvider;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class AiTextProvider implements TextProvider {
    private final Client client;
    private final Config config;
    private final int numberOfWords;

    public AiTextProvider(Config config, int numberOfWords) {
        this.config = config;
        this.numberOfWords = numberOfWords;

        client = Client.builder()
                .apiKey(config.genaiApiKey())
                .build();

    }

    @Override
    public String getText() {
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.1-flash-lite",
                        "Generate %d English lowercase words separated by space, nothing more nothing less."
                                .formatted(numberOfWords),
                        null
                );
        return response.text();
    }
}
