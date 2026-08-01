package com.github.konstantinevashalomidze.domain.view;

import com.github.konstantinevashalomidze.domain.Metrics;

import java.awt.*;
import java.util.List;

public interface TypingView {



    void drawTargetText(String targetText);
    void drawCaretAt(int i);
    void colorCharAt(int i, Color color);

}
