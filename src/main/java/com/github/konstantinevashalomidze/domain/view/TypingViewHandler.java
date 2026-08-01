package com.github.konstantinevashalomidze.domain.view;

public interface TypingViewHandler {
    void keyDown(char key);
    void keyUp(char key);
    void keyTyped(char key);
}
