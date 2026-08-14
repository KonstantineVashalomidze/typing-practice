package com.github.konstantinevashalomidze.ui.views;

import com.github.konstantinevashalomidze.MainFrame;
import com.github.konstantinevashalomidze.domain.view.TypingView;
import com.github.konstantinevashalomidze.domain.view.TypingViewHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;

public class TypingPanel extends JPanel implements KeyListener, TypingView {
    private TypingViewHandler typingViewHandler;
    private int errorCount;

    public void setTypingViewHandler(TypingViewHandler typingViewHandler) {
        this.typingViewHandler = typingViewHandler;
    }

    private final Font targetTextFont = new Font(Font.DIALOG_INPUT, Font.PLAIN, 24);
    private final Font shortcutHintFont = new  Font(Font.DIALOG_INPUT, Font.PLAIN, 12);
    private final Color targetTextColorDefault = new Color(0, 0, 0, 128);
    private final Color spaceCharColorDefault =
            new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 10);


    private int caretPosition;
    private String targetText;
    private Color[] indexColors;
    private final int MARGIN_X = 40, MARGIN_Y = 30;
    private double wpm;
    private double accuracy;

    public TypingPanel() {
        setFocusable(true);
        addKeyListener(this);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
        g2d.setFont(targetTextFont);

        FontMetrics fm = g2d.getFontMetrics();

        int drawTargetTextX = MARGIN_X, drawTargetTextY = MARGIN_Y;
        int targetTextLength = targetText.length();
        for (int i = 0; i < targetTextLength; i++) {
            String targetTextChar = String.valueOf(targetText.charAt(i));
            int charWidth = fm.stringWidth(targetTextChar);
            // right border is reached wrap continue drawing on next line
            if (drawTargetTextX + charWidth >= getWidth() - MARGIN_X) {
                // If caret were currently on the middle of the word type -
                drawTargetTextX = MARGIN_X;
                drawTargetTextY += fm.getAscent() + fm.getDescent();
            }
            // Draw caret at
            if (caretPosition < targetTextLength && caretPosition == i) {
                g2d.setColor(Color.BLACK);
                g2d.drawRect(drawTargetTextX, drawTargetTextY - fm.getAscent(),
                        charWidth, fm.getAscent() + fm.getDescent());
            }
            g2d.setColor(indexColors[i]);
            if (" ".equals(targetTextChar)) {
                Color originalColor = indexColors[i];
                if (originalColor != Color.RED) {
                    g2d.setColor(spaceCharColorDefault);
                }
                g2d.drawString("_", drawTargetTextX, drawTargetTextY);
            } else {
                g2d.drawString(targetTextChar, drawTargetTextX, drawTargetTextY);
            }
            drawTargetTextX += charWidth;
        }

        g2d.setFont(shortcutHintFont);
        g2d.setColor(Color.DARK_GRAY);
        fm = g2d.getFontMetrics();
        int bottomLineLabels = getHeight() - (MARGIN_Y / 2);
        String wpmLocal = String.valueOf((int) wpm);
        String accuracyLocal = String.valueOf((int) accuracy);
        List<String> bottomLabels = List.of(
                "ctrl + n - new game",
                " | ctrl + s - settings",
                " | WPM:", wpmLocal,
                " | ACCURACY:", accuracyLocal,
                " | ERRORS:", String.valueOf(errorCount));
        int drawingX = MARGIN_X;
        for (String bottomLabel : bottomLabels) {
            g2d.drawString(bottomLabel, drawingX,
                    bottomLineLabels);
            drawingX += fm.stringWidth(bottomLabel);
        }
    }


    @Override
    public void drawTargetText(String targetText) {
        this.targetText = targetText;
        indexColors = new Color[targetText.length()];
        Arrays.fill(indexColors, targetTextColorDefault);
        repaint();
    }

    @Override
    public void drawCaretAt(int i) {
        this.caretPosition = i;
        repaint();
    }

    @Override
    public void colorCharAt(int i, Color color) {
        indexColors[i] = color;
        repaint();
    }

    @Override
    public void displayWpm(double wpm) {
        this.wpm = wpm;
        repaint();
    }

    @Override
    public void displayAccuracy(double accuracy) {
        this.accuracy = accuracy;
        repaint();
    }

    @Override
    public void displayErrorCount(int errorCount) {
        this.errorCount = errorCount;
        repaint();
    }


    @Override
    public void keyTyped(KeyEvent e) {
        if (e.isControlDown() && e.getKeyChar() == '\b') {
            typingViewHandler.keyTyped(e.getKeyChar(), true);
        } else {
            typingViewHandler.keyTyped(e.getKeyChar(), false);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        typingViewHandler.keyDown(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_N) {
            typingViewHandler.keyUp(e.getKeyChar(), "ctrln");
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
            typingViewHandler.keyUp(e.getKeyChar(), "ctrls");
        }
    }
}
