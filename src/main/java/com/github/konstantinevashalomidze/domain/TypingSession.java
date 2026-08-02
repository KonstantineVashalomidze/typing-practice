package com.github.konstantinevashalomidze.domain;

import com.github.konstantinevashalomidze.db.MetricsRepository;
import com.github.konstantinevashalomidze.domain.exceptions.TypingSessionAlreadyCompletedException;
import com.github.konstantinevashalomidze.domain.model.Metrics;

import java.util.ArrayList;
import java.util.List;

import static com.github.konstantinevashalomidze.domain.TypingSession.State.*;

public class TypingSession {
    public enum CharState {
        CORRECT,
        INCORRECT,
        DEFAULT
    }

    private final MetricsRepository metricsRepository;
    private final List<Character> typedSoFar = new ArrayList<>();
    private State state = TO_DO;
    private final String targetText;
    private int caretPosition;
    private long startTimeNanos;
    private long endTimeNanos;
    private double wpm;
    private int errorCount;
    private double accuracy;

    public TypingSession(MetricsRepository metricsRepository, String targetText) {
        this.metricsRepository = metricsRepository;
        this.targetText = targetText;
    }

    public CharState newChar(char c, long timestamp) {
        if (state == COMPLETED) {
            throw new TypingSessionAlreadyCompletedException("Typing session already completed");
        }

        if (typedSoFar.isEmpty() && c == '\b') {
            return CharState.DEFAULT;
        } else if (c == '\b') {
            typedSoFar.removeLast();
            caretPosition--;
            return CharState.DEFAULT;
        } else {
            typedSoFar.add(c);
            if (c == targetText.charAt(caretPosition)) {
                commonPiece();
                return CharState.CORRECT;
            } else {
                commonPiece();
                errorCount++;
                 return CharState.INCORRECT;
            }
        }

    }

    private void commonPiece() {
        caretPosition++;
        if (state == TO_DO) {
            state = IN_PROGRESS;
            startTimeNanos = System.nanoTime();
        } else if (state == IN_PROGRESS && caretPosition >= targetText.length()) {
            state = COMPLETED;
            endTimeNanos = System.nanoTime();
            calculateAccuracy();
            metricsRepository.save(new Metrics(
                   wpm,
                   accuracy,
                   errorCount,
                    targetText
            ));
        }
    }

    private void calculateAccuracy() {
        accuracy = ((typedSoFar.size() - errorCount) / (double) typedSoFar.size()) * 100.;
    }

    private void calculateWpm() {
        if (state != COMPLETED) {
            endTimeNanos = System.nanoTime();
        }
        double elapsedMinutes = (endTimeNanos - startTimeNanos) / 60_000_000_000.;
        int numberOfChars = typedSoFar.size();
        wpm = (Math.max(0, (numberOfChars - errorCount)/ 5.)) / elapsedMinutes;
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    public List<Character> getTypedSoFar() {
        return typedSoFar;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public double getWpm() {
        calculateWpm();
        return wpm;
    }

    public double getAccuracy() {
        calculateAccuracy();
        return accuracy;
    }

    public String getTargetText() {
        return targetText;
    }

    public enum State {
        TO_DO, IN_PROGRESS, COMPLETED
    }
}
