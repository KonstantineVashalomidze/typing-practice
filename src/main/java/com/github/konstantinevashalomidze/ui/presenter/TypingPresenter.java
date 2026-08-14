package com.github.konstantinevashalomidze.ui.presenter;

import com.github.konstantinevashalomidze.config.Config;
import com.github.konstantinevashalomidze.db.MetricsRepository;
import com.github.konstantinevashalomidze.domain.TypingSession;
import com.github.konstantinevashalomidze.domain.exceptions.TypingSessionHasNotBeenStartedException;
import com.github.konstantinevashalomidze.domain.model.Metrics;
import com.github.konstantinevashalomidze.domain.service.TextProvider;
import com.github.konstantinevashalomidze.domain.service.fallback.FallbackTextProvider;
import com.github.konstantinevashalomidze.domain.view.TypingView;
import com.github.konstantinevashalomidze.domain.view.TypingViewHandler;
import com.github.konstantinevashalomidze.ui.Navigator;
import com.github.konstantinevashalomidze.ui.views.SettingsPanel;

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
    private final Navigator navigator;

    public TypingPresenter(Config config, Navigator navigator, MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
        this.navigator = navigator;
        textProvider = new FallbackTextProvider(config, metricsRepository);
    }

    public void createNewSession() {
        String targetText = textProvider.getText();
        typingSession = new TypingSession(targetText);
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
                typingView.displayErrorCount(typingSession.getErrorCount());
                if (typingSession.getState() == TypingSession.State.COMPLETED) {
                    metricsRepository.save(new Metrics(
                            typingSession.getWpm(),
                            typingSession.getAccuracy(),
                            typingSession.getErrorCount(),
                            typingSession.getKeyTimestamps(),
                            targetText
                    ));
                    metricsTimer.cancel();
                }
            }
        };
        metricsTimer.scheduleAtFixedRate(task, 0, 500);
    }

    @Override
    public void keyDown(char key) {
        typingSession.keyDown(key);
    }

    @Override
    public void keyUp(char key, String event) {
        if (event != null) {
            switch (event) {
                case "ctrls" -> {
                    navigator.navigateTo(SettingsPanel.class.getSimpleName());
                }
                case "ctrln" -> createNewSession();
            }
        }
    }

    @Override
    public void keyTyped(char key, boolean wordDelete) {
        if (typingSession == null) {
            throw new TypingSessionHasNotBeenStartedException("Start new session before typing");
        }

        if (typingSession.getState() != TypingSession.State.COMPLETED) {
            int prevCaretPosition = typingSession.getCaretPosition();
            if (wordDelete) {
                typingSession.deleteWord();
                int caretPosition = typingSession.getCaretPosition();
                for (int i = caretPosition; i < prevCaretPosition; i++) {
                    typingView.colorCharAt(i, correctnessToColorMapper(TypingSession.CharState.DEFAULT));
                }
                typingView.drawCaretAt(caretPosition);
            } else {
                var charState = typingSession.newChar(key);
                int caretPosition = typingSession.getCaretPosition();

                typingView.colorCharAt(charState == TypingSession.CharState.DEFAULT ? caretPosition
                        : prevCaretPosition, correctnessToColorMapper(charState));
                typingView.drawCaretAt(caretPosition);
            }

        }
    }

    private Color correctnessToColorMapper(TypingSession.CharState charState) {
        return switch (charState) {
            case CORRECT -> Color.BLACK;
            case INCORRECT -> Color.RED;
            default -> new Color(0, 0, 0, 128);
        };
    }

}
