package com.github.konstantinevashalomidze.ui.presenter;

import com.github.konstantinevashalomidze.config.Config;
import com.github.konstantinevashalomidze.db.MetricsRepository;
import com.github.konstantinevashalomidze.domain.view.SettingsView;
import com.github.konstantinevashalomidze.domain.view.SettingsViewHandler;
import com.github.konstantinevashalomidze.ui.Navigator;
import com.github.konstantinevashalomidze.ui.views.SettingsPanel;

public class SettingsPresenter implements SettingsViewHandler {
    private final Config config;
    private final Navigator navigator;


    public SettingsPresenter(Config config, Navigator navigator) {
        this.config = config;
        this.navigator = navigator;
    }


    public void setSettingsView(SettingsView settingsView) {

    }
}
