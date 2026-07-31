package com.github.konstantinevashalomidze.ui.coordinator;

import com.github.konstantinevashalomidze.domain.KeystrokeEvent;
import com.github.konstantinevashalomidze.domain.SessionListener;
import com.github.konstantinevashalomidze.domain.view.TypingView;
import com.github.konstantinevashalomidze.ui.presenter.TypingPresenter;
import com.github.konstantinevashalomidze.ui.views.TypingPanel;

public class AppCoordinator implements SessionListener, NavigationListener {
    private final Navigator navigator;
    private final TypingView typingView;
    private final TypingPresenter typingPresenter;

    public AppCoordinator(Navigator navigator, TypingView typingView) {
        this.navigator = navigator;
        this.typingView = typingView;
        typingPresenter = new TypingPresenter(typingView);

        // Register KeyEventHandler to Typing Panel
        typingView.addKeyEventHandler(typingPresenter);
    }

    public void showTypingPanel() {
        navigator.showPanel(TypingPanel.class.getName());
    }


    @Override
    public void onStarted(long timestamp) {

    }

    @Override
    public void onFinished(long timestamp) {

    }

    @Override
    public void onKeystrokeEvent(KeystrokeEvent event) {

    }

    @Override
    public void onShowTypingScreen() {
        navigator.showPanel(TypingPanel.class.getName());
    }
}
