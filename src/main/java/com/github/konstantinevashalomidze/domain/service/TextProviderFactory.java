package com.github.konstantinevashalomidze.domain.service;

import com.github.konstantinevashalomidze.config.Config;
import com.github.konstantinevashalomidze.db.MetricsRepository;
import com.github.konstantinevashalomidze.domain.service.ai.AiTextProvider;
import com.github.konstantinevashalomidze.domain.service.random.RandomTextProvider;

public class TextProviderFactory {
    public static TextProvider createTextProvider(Config config, MetricsRepository metricsRepository) {
        try {
            return new AiTextProvider(config, metricsRepository, 25);
        } catch (Exception e) {
            return new RandomTextProvider(25, 1, 5);
        }
    }
}
