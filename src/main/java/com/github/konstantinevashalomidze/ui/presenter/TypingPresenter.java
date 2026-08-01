package com.github.konstantinevashalomidze.ui.presenter;

import com.github.konstantinevashalomidze.domain.TypingSession;
import com.github.konstantinevashalomidze.domain.exceptions.TypingSessionHasNotBeenStartedException;
import com.github.konstantinevashalomidze.domain.service.RandomTextProvider;
import com.github.konstantinevashalomidze.domain.service.TextProvider;
import com.github.konstantinevashalomidze.domain.view.TypingView;
import com.github.konstantinevashalomidze.domain.view.TypingViewHandler;

import java.awt.*;

public class TypingPresenter implements TypingViewHandler {
    private final TextProvider textProvider = new RandomTextProvider();
    private TypingView typingView;
    public void setTypingView(TypingView typingView) {
        this.typingView = typingView;
    }
    private TypingSession typingSession;

    public void createNewSession() {
        typingSession = new TypingSession(textProvider.getText());
        typingView.drawTargetText(textProvider.getText());
        typingView.drawCaretAt(typingSession.getCaretPosition());
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
            var charState = typingSession.newChar(key);
            int caretPosition = typingSession.getCaretPosition();

            typingView.colorCharAt(charState == TypingSession.CharState.DEFAULT ? caretPosition
                    : prevCaretPosition, correctnessToColorMapper(charState));
            typingView.drawCaretAt(caretPosition);
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
