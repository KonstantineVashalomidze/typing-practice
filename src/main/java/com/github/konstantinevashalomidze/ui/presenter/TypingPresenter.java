package com.github.konstantinevashalomidze.ui.presenter;

import com.github.konstantinevashalomidze.config.Config;
import com.github.konstantinevashalomidze.db.MetricsRepository;
import com.github.konstantinevashalomidze.domain.TypingSession;
import com.github.konstantinevashalomidze.domain.exceptions.TypingSessionHasNotBeenStartedException;
import com.github.konstantinevashalomidze.domain.service.ai.AiTextProvider;
import com.github.konstantinevashalomidze.domain.service.random.RandomTextProvider;
import com.github.konstantinevashalomidze.domain.service.TextProvider;
import com.github.konstantinevashalomidze.domain.view.TypingView;
import com.github.konstantinevashalomidze.domain.view.TypingViewHandler;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class TypingPresenter implements TypingViewHandler {
    private final TextProvider textProvider;
    private TypingView typingView;
    public void setTypingView(TypingView typingView) {
        this.typingView = typingView;
    }
    private TypingSession typingSession;
    private final MetricsRepository metricsRepository;
    private Timer metricsTimer;
    private final Config config;

    public TypingPresenter(Config config, MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
        this.config = config;
        textProvider = new AiTextProvider(config, 25);
    }

    public void createNewSession() {
        String targetText = textProvider.getText();
        typingSession = new TypingSession(metricsRepository, targetText);
        typingView.drawTargetText(targetText);
        typingView.drawCaretAt(typingSession.getCaretPosition());
        if (metricsTimer != null) {
            metricsTimer.cancel();
        }
        metricsTimer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                typingView.displayWpm(typingSession.getWpm());
                typingView.displayAccuracy(typingSession.getAccuracy());
            }
        };
        metricsTimer.scheduleAtFixedRate(task, 0, 500);
    }

    @Override
    public void keyDown(char key) {

    }

    @Override
    public void keyUp(char key) {

    }

    @Override
    public void keyTyped(char key) {
        if (typingSession == null) {
            throw new TypingSessionHasNotBeenStartedException("Start new session before typing");
        }

        if (typingSession.getState() != TypingSession.State.COMPLETED) {
            int prevCaretPosition = typingSession.getCaretPosition();
            var charState = typingSession.newChar(key, System.nanoTime());
            int caretPosition = typingSession.getCaretPosition();

            typingView.colorCharAt(charState == TypingSession.CharState.DEFAULT ? caretPosition
                    : prevCaretPosition, correctnessToColorMapper(charState));
            typingView.drawCaretAt(caretPosition);
        }
    }

    @Override
    public void newSession() {
        createNewSession();
    }

    private Color correctnessToColorMapper(TypingSession.CharState charState) {
        return switch (charState) {
            case CORRECT -> Color.BLACK;
            case INCORRECT -> Color.RED;
            default -> new Color(0, 0, 0, 128);
        };
    }

}
