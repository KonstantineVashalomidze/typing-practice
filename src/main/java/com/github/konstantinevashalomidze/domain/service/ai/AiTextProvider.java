package com.github.konstantinevashalomidze.domain.service.ai;

import com.github.konstantinevashalomidze.config.Config;
import com.github.konstantinevashalomidze.db.MetricsRepository;
import com.github.konstantinevashalomidze.domain.model.Metrics;
import com.github.konstantinevashalomidze.domain.service.TextProvider;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import java.util.ArrayList;
import java.util.List;

public class AiTextProvider implements TextProvider {
    private final Client client;
    private final Config config;
    private final int numberOfWords;
    private final MetricsRepository metricsRepository;

    public AiTextProvider(Config config, MetricsRepository metricsRepository, int numberOfWords) {
        this.config = config;
        this.numberOfWords = numberOfWords;
        this.metricsRepository = metricsRepository;

        client = Client.builder()
                .apiKey(config.genaiApiKey())
                .build();
    }


    private List<String> determineHesitatedCharPairs() {
        var strugglingPairs = new ArrayList<String>();
        var metrics = metricsRepository.findN(3);
        for (var metric : metrics) {
            var targetText = metric.targetText();
            var keyTimestamps = metric.keyTimestamps();
            for (int i = 0; i < keyTimestamps.size() - 1; i++) {
                var keyTimestamp1 = keyTimestamps.get(i);
                var key1 = keyTimestamp1.key();
                var timestamp1 = keyTimestamp1.value();

                var keyTimestamp2 = keyTimestamps.get(i + 1);
                var key2 = keyTimestamp2.key();
                var timestamp2 = keyTimestamp2.value();

                if (key1 == ' ' || key2 == ' ') {
                    continue;
                }


                // second delay is considered as hesitation
                if (timestamp2 - timestamp1 > 1 / 60.) {
                    strugglingPairs.add(key1 + "" + key2);
                }

                // Mistake is considered hesitation
                if (i < targetText.length() && targetText.charAt(i) != key1) {
                    strugglingPairs.add(key1 + "" + key2);
                }

            }
        }
        return strugglingPairs;
    }

    @Override
    public String getText() {
        List<String> hesitationPairs = determineHesitatedCharPairs();
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.1-flash-lite",
                        "Generate %d English lowercase words separated by space, nothing more nothing less. Make sure that words contain some of the pairs from these given set of pairs: %s"
                                .formatted(
                                        numberOfWords,
                                        hesitationPairs
                                        ),
                        null
                );
        return response.text();
    }
}
