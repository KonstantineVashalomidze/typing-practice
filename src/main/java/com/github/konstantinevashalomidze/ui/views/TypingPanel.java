package com.github.konstantinevashalomidze.ui.views;

import com.github.konstantinevashalomidze.domain.view.TypingView;
import com.github.konstantinevashalomidze.domain.view.TypingViewHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class TypingPanel extends JPanel implements KeyListener, TypingView {
    private TypingViewHandler typingViewHandler;
    public void setTypingViewHandler(TypingViewHandler typingViewHandler) {
        this.typingViewHandler = typingViewHandler;
    }

    private int caretPosition;
    private String targetText;
    private Color[] indexColors;
    private final int MARGIN_X = 40, MARGIN_Y = 30;

    public TypingPanel() {
        setFocusable(true);
        addKeyListener(this);
        setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 24));
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

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
                g2d.setColor(Color.BLUE);
                g2d.drawRect(drawTargetTextX, drawTargetTextY - fm.getAscent(),
                        charWidth, fm.getAscent() + fm.getDescent());
            }
            g2d.setColor(indexColors[i]);
            if (" ".equals(targetTextChar)) {
                Color originalColor = indexColors[i];
                if (originalColor != Color.RED) {
                    g2d.setColor(new Color(
                            originalColor.getRed(),
                            originalColor.getGreen(),
                            originalColor.getBlue(),
                            10
                    ));
                }
                g2d.drawString("_", drawTargetTextX, drawTargetTextY);
            } else {
                g2d.drawString(targetTextChar, drawTargetTextX, drawTargetTextY);
            }
            drawTargetTextX += charWidth;
        }

    }


    @Override
    public void drawTargetText(String targetText) {
        this.targetText = targetText;
        indexColors = new Color[targetText.length()];
        Arrays.fill(indexColors, Color.BLACK);
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
    public void keyTyped(KeyEvent e) {
        typingViewHandler.keyTyped(e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
