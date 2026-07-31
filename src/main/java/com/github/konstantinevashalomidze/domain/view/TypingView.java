package com.github.konstantinevashalomidze.domain.view;

import com.github.konstantinevashalomidze.domain.Metrics;

import java.util.List;

public interface TypingView {
    void displayTargetText(String targetText);
    void updateLiveMetrics(double wpm, double accuracy);
    void showFinalResults(Metrics metrics);
    void addKeyEventHandler(KeyEventHandler keh);
    void updateTypedProgress(String typedSoFar, List<Boolean> correctness);
}
