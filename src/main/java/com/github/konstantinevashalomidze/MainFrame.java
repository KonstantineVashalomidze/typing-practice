package com.github.konstantinevashalomidze;

import com.github.konstantinevashalomidze.config.Config;
import com.github.konstantinevashalomidze.db.MetricsRepository;
import com.github.konstantinevashalomidze.ui.Navigator;
import com.github.konstantinevashalomidze.ui.presenter.SettingsPresenter;
import com.github.konstantinevashalomidze.ui.presenter.TypingPresenter;
import com.github.konstantinevashalomidze.ui.views.SettingsPanel;
import com.github.konstantinevashalomidze.ui.views.TypingPanel;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame implements Navigator {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setSize(800, 600);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.getContentPane().setLayout(new CardLayout());

            Config config = new Config();

            // Typing
            MetricsRepository metricsRepository = new MetricsRepository();
            TypingPanel typingPanel = new TypingPanel();
            TypingPresenter typingPresenter = new TypingPresenter(config, mainFrame, metricsRepository);
            typingPanel.setTypingViewHandler(typingPresenter);
            typingPresenter.setTypingView(typingPanel);
            typingPresenter.createNewSession();
            mainFrame.getContentPane().add(typingPanel, TypingPanel.class.getSimpleName());

            // Settings
            SettingsPanel settingsPanel = new SettingsPanel();
            SettingsPresenter settingsPresenter = new SettingsPresenter(config, mainFrame);
            settingsPanel.setSettingsViewHandler(settingsPresenter);
            settingsPresenter.setSettingsView(settingsPanel);
            mainFrame.getContentPane().add(settingsPanel, SettingsPanel.class.getSimpleName());

            mainFrame.setVisible(true);
        });
    }

    @Override
    public void navigateTo(String className) {
        CardLayout cl = (CardLayout) (getContentPane().getLayout());
        cl.show(getContentPane(), className);
    }
}