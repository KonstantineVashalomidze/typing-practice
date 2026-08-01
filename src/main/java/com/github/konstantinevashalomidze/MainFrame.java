package com.github.konstantinevashalomidze;

import com.github.konstantinevashalomidze.ui.presenter.TypingPresenter;
import com.github.konstantinevashalomidze.ui.views.TypingPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setSize(800, 600);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.getContentPane().setLayout(new BorderLayout());

            TypingPanel typingPanel = new TypingPanel();
            TypingPresenter typingPresenter = new TypingPresenter();
            typingPanel.setTypingViewHandler(typingPresenter);
            typingPresenter.setTypingView(typingPanel);
            typingPresenter.createNewSession();

            mainFrame.getContentPane().add(typingPanel, BorderLayout.CENTER);

            mainFrame.setVisible(true);
        });

    }

}