package com.github.konstantinevashalomidze.domain;

import com.github.konstantinevashalomidze.domain.exceptions.IllegalMetricsArgumentException;

public record Metrics(
        double netWpm,
        double accuracy,
        int errorCount
) {
    public Metrics {
        if (netWpm < 0) {
            throw new IllegalMetricsArgumentException("WPM can't be less than 0");
        }

        if (accuracy < 0) {
            throw new IllegalMetricsArgumentException("Accuracy can't be less than 0");
        }

        if (errorCount < 0) {
            throw new IllegalMetricsArgumentException("Error count can't be less than 0");
        }
    }

}
