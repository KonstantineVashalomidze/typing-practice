package com.github.konstantinevashalomidze.domain.view;

import java.awt.*;

public interface TypingView {



    void drawTargetText(String targetText);
    void drawCaretAt(int i);
    void colorCharAt(int i, Color color);

    void displayWpm(double wpm);

    void displayAccuracy(double accuracy);
}
