package com.github.konstantinevashalomidze.ui.views;

import com.github.konstantinevashalomidze.domain.Metrics;
import com.github.konstantinevashalomidze.domain.view.KeyEventHandler;
import com.github.konstantinevashalomidze.domain.view.TypingView;
import com.github.konstantinevashalomidze.ui.coordinator.Navigator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class TypingPanel extends JComponent implements KeyListener, TypingView, Navigator {
    private final List<KeyEventHandler> keyEventHandlers = new ArrayList<>();

    private int MARGIN_X = 40, MARGIN_Y = 50;

    private String targetText = "";
    private String typedSoFar = "";
    private List<Boolean> correctness = new  ArrayList<>();
    private boolean caretVisible = true;
    private Timer caretTogglerTimer;

    public TypingPanel() {
        setFocusable(true);
        addKeyListener(this);
        setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 24));
        caretTogglerTimer = new Timer(500, (e) -> {
            caretVisible = !caretVisible;
            repaint();
        });
        caretTogglerTimer.start();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        FontMetrics fm = g2d.getFontMetrics();
        if (!targetText.isEmpty()) {
            int drawX = MARGIN_X, drawY = MARGIN_Y + fm.getAscent();
            for (int i = 0; i < targetText.length(); i++) {
                char c = targetText.charAt(i);
                if (caretVisible && i == typedSoFar.length()) { // TODO: typedSoFar.length() - 1???
                    g2d.setColor(Color.RED);
                    g2d.drawLine(drawX, drawY + fm.getDescent(), drawX, drawY - fm.getAscent());
                    g2d.setColor(Color.BLACK);

                }
                if (drawX + fm.charWidth(c) >= getWidth() - MARGIN_X) {
                    drawX = MARGIN_X;
                    drawY += fm.getHeight();
                    if (c == ' ') {
                        continue;
                    }
                }
                if (i < typedSoFar.length()) {
                    g2d.setColor(correctness.get(i) ? Color.GREEN : Color.RED);
                } else {
                    g2d.setColor(Color.BLACK);
                }
                g2d.drawString(Character.toString(c), drawX, drawY);
                drawX += fm.charWidth(c);
            }
        }
    }


    @Override
    public void displayTargetText(String targetText) {
        this.targetText = targetText;
        repaint();
    }

    @Override
    public void updateLiveMetrics(double wpm, double accuracy) {

    }

    @Override
    public void showFinalResults(Metrics metrics) {

    }

    @Override
    public void addKeyEventHandler(KeyEventHandler keh) {
        if (!keyEventHandlers.contains(keh)) {
            keyEventHandlers.add(keh);
        }
    }

    @Override
    public void updateTypedProgress(String typedSoFar, List<Boolean> correctness) {
        this.typedSoFar = typedSoFar;
        this.correctness = correctness;
        repaint();
    }

    @Override
    public void showPanel(String panelName) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyEventHandlers.forEach(keh -> {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_BACK_SPACE -> keh.keyDown('\b');
                default -> keh.keyDown(e.getKeyChar());
            }
        });
        caretVisible = true;
        caretTogglerTimer.restart();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyEventHandlers.forEach(keh -> {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_BACK_SPACE -> keh.keyUp('\b');
                default -> keh.keyUp(e.getKeyChar());
            }
        });
        caretVisible = true;
        caretTogglerTimer.restart();
        repaint();
    }
}
