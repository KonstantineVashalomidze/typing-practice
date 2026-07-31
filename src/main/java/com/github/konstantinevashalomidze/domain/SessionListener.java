package com.github.konstantinevashalomidze.domain;

public interface SessionListener {
    void onStarted(long timestamp);
    void onFinished(long timestamp);
    void onKeystrokeEvent(KeystrokeEvent event);
}
