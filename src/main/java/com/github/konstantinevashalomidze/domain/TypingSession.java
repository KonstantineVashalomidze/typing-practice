package com.github.konstantinevashalomidze.domain;

import com.github.konstantinevashalomidze.domain.exceptions.SessionAlreadyStartedException;
import com.github.konstantinevashalomidze.domain.exceptions.SessionNotStartedException;
import com.github.konstantinevashalomidze.domain.service.MetricsCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypingSession {
    private final List<KeystrokeEvent> keystrokeEvents = new ArrayList<>();
    private final List<SessionListener> sessionListeners = new ArrayList<>();
    private long startTime;
    private long finishTime;

    
    public boolean started() {
        return startTime != 0 && finishTime == 0;
    }

    public boolean finished() {
        return startTime != 0 && finishTime != 0;
    }
    

    public void start() {
        if (started()) {
            throw new SessionAlreadyStartedException("Session already started");
        }
        keystrokeEvents.clear();
        startTime = System.nanoTime();
        sessionListeners.forEach(sessionListener -> 
                sessionListener.onStarted(startTime));
    }


    public void finish() {
        if (!started()) {
            throw new SessionNotStartedException("Session not started");
        }

        if (finished()) {
            throw new SessionNotStartedException("Session not started");
        }
        finishTime = System.nanoTime();
        keystrokeEvents.clear();
        MetricsCalculator.compute(keystrokeEvents, startTime, finishTime);
        sessionListeners.forEach(sessionListener -> sessionListener.onFinished(finishTime));
    }

    public void addEvent(KeystrokeEvent event) {
        if (!started()) {
            throw new SessionNotStartedException("Session has not been started");
        }
        keystrokeEvents.add(event);
        sessionListeners.forEach(sessionListener -> sessionListener.onKeystrokeEvent(event));
    }

    public String typedSoFar() {
        StringBuilder sb = new StringBuilder();
        for (var kse : keystrokeEvents) {
            sb.append(kse.typed());
        }
        return sb + "";
    }

    public List<Boolean> correctness() {
        List<Boolean> correctness = new ArrayList<>();
        for (var kse : keystrokeEvents) {
            correctness.add(kse.isCorrect());
        }
        return correctness;
    }

    public void addListener(SessionListener sessionListener) {
        sessionListeners.add(sessionListener);
    }


}
