package com.github.konstantinevashalomidze.ui.views;

import com.github.konstantinevashalomidze.domain.view.SettingsView;
import com.github.konstantinevashalomidze.domain.view.SettingsViewHandler;
import com.github.konstantinevashalomidze.ui.presenter.SettingsPresenter;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel implements SettingsView {

    private SettingsViewHandler settingsViewHandler;

    public SettingsPanel() {
        setBackground(Color.PINK); // Just to distringuish from other panlels right now
    }

    public void setSettingsViewHandler(SettingsViewHandler settingsViewHandler) {
        this.settingsViewHandler = settingsViewHandler;
    }
}
