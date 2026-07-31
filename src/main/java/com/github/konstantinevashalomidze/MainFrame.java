package com.github.konstantinevashalomidze;

import com.github.konstantinevashalomidze.ui.coordinator.AppCoordinator;
import com.github.konstantinevashalomidze.ui.coordinator.Navigator;
import com.github.konstantinevashalomidze.ui.views.TypingPanel;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements Navigator {
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setSize(800, 600);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.getContentPane().setLayout(new CardLayout());

            TypingPanel typingPanel = new TypingPanel();
            mainFrame.getContentPane().add(typingPanel, TypingPanel.class.getSimpleName());
            AppCoordinator appCoordinator = new AppCoordinator(mainFrame, typingPanel);

            mainFrame.setVisible(true);
        });

    }

    @Override
    public void showPanel(String panelName) {
        ((CardLayout) (getContentPane().getLayout())).show(this, panelName);
    }
}