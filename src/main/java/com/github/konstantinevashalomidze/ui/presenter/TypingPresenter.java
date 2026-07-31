package com.github.konstantinevashalomidze.ui.presenter;

import com.github.konstantinevashalomidze.domain.KeystrokeEvent;
import com.github.konstantinevashalomidze.domain.TypingSession;
import com.github.konstantinevashalomidze.domain.service.TextProvider;
import com.github.konstantinevashalomidze.domain.view.KeyEventHandler;
import com.github.konstantinevashalomidze.domain.view.TypingView;

public class TypingPresenter implements KeyEventHandler {
    private final TypingSession session;
    private final TypingView typingView;
    private final TextProvider textProvider;

    public TypingPresenter(TypingView typingView) {
        session = new TypingSession();
        this.typingView = typingView;
        textProvider = () -> "Random text for visualization";
        typingView.displayTargetText(textProvider.getText());
    }


    @Override
    public void keyDown(char key) {
        if (!Character.isLetter(key) && key != ' ') return;
        if (!session.started()){
            session.start();
            typingView.displayTargetText(textProvider.getText());
        }
        session.addEvent(new KeystrokeEvent(
          key,
          ' ',
          System.nanoTime(),
          System.nanoTime(),
          false,
          0
        ));
        typingView.updateTypedProgress(session.typedSoFar(), session.correctness());
    }

    @Override
    public void keyUp(char key) {
    }


}
