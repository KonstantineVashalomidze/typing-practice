package com.github.konstantinevashalomidze.domain.view;

public interface TypingViewHandler {
    void keyDown(char key);
    void keyUp(char key, String event);
    void keyTyped(char key, boolean wordDelete);


}
