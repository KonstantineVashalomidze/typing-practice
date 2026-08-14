package com.github.konstantinevashalomidze.domain.service.fallback;

import com.github.konstantinevashalomidze.config.Config;
import com.github.konstantinevashalomidze.db.MetricsRepository;
import com.github.konstantinevashalomidze.domain.service.TextProvider;
import com.github.konstantinevashalomidze.domain.service.ai.AiTextProvider;
import com.github.konstantinevashalomidze.domain.service.random.RandomTextProvider;

public class FallbackTextProvider implements TextProvider {
    private AiTextProvider aiTextProvider;
    private RandomTextProvider randomTextProvider;

    public FallbackTextProvider(Config config, MetricsRepository metricsRepository) {
        randomTextProvider = new RandomTextProvider(25, 1, 5);
        try {
            aiTextProvider = new AiTextProvider(config, metricsRepository, 25);
        } catch (Exception exception) {
            aiTextProvider = null;
        }
    }

    
    @Override
    public String getText() {
        try {
            return aiTextProvider.getText();
        } catch (Exception exception) {
            return randomTextProvider.getText();
        }
    }
}
