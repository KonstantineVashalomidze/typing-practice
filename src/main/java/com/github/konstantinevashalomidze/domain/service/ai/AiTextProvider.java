package com.github.konstantinevashalomidze.domain.service.ai;

import com.github.konstantinevashalomidze.config.Config;
import com.github.konstantinevashalomidze.db.MetricsRepository;
import com.github.konstantinevashalomidze.domain.model.Metrics;
import com.github.konstantinevashalomidze.domain.service.TextProvider;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class AiTextProvider implements TextProvider {
    private final Logger logger = LoggerFactory.getLogger(AiTextProvider.class);
    private final Client client;
    private final Config config;
    private final int numberOfWords;
    private final MetricsRepository metricsRepository;
    private final Deque<String> targetTexts;

    public AiTextProvider(Config config, MetricsRepository metricsRepository, int numberOfWords) {
        this.config = config;
        this.numberOfWords = numberOfWords;
        this.metricsRepository = metricsRepository;
        client = Client.builder()
                .apiKey(config.genaiApiKey())
                .build();
        var unshuffled = metricsRepository.findN(3).stream().map(Metrics::targetText).collect(Collectors.toList());
        Collections.shuffle(unshuffled);
        targetTexts = new LinkedList<>(unshuffled);
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

    private void fillTheQueue() {
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

        targetTexts.offer(response.text());
        logger.info("fetched text %s".formatted(response.text()));
    }

    @Override
    public String getText() {
        if (targetTexts.isEmpty()) {
            fillTheQueue();
        }

        String text = targetTexts.poll();
        Thread.ofVirtual().start(() -> {
            if (targetTexts.size() < 2) {
                fillTheQueue();
                fillTheQueue();
                fillTheQueue();
            }
        });
        return text;
    }
}
