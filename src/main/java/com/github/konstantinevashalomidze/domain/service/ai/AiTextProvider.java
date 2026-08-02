package com.github.konstantinevashalomidze.domain.service.ai;

import com.github.konstantinevashalomidze.config.Config;
import com.github.konstantinevashalomidze.db.MetricsRepository;
import com.github.konstantinevashalomidze.domain.model.Metrics;
import com.github.konstantinevashalomidze.domain.service.TextProvider;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import java.util.List;

public class AiTextProvider implements TextProvider {
    private final Client client;
    private final Config config;
    private final int numberOfWords;
    private final MetricsRepository metricsRepository;
    private final List<Metrics> metrics;

    public AiTextProvider(Config config, MetricsRepository metricsRepository, int numberOfWords) {
        this.config = config;
        this.numberOfWords = numberOfWords;
        this.metricsRepository = metricsRepository;

        client = Client.builder()
                .apiKey(config.genaiApiKey())
                .build();
        metrics = metricsRepository.findAll();
    }

    @Override
    public String getText() {
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.1-flash-lite",
                        "Generate %d English lowercase words separated by space, nothing more nothing less."
                                .formatted(
                                        numberOfWords
                                        ),
                        null
                );
        return response.text();
    }
}
