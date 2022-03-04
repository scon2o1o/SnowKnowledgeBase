package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Settings;

import java.util.List;

public interface SettingsService {
    List<Settings> getAllSettings();

    Settings saveSettings(Settings settings);

    Settings getSettingsById(Long id);

    Settings updateSettings(Settings settings);

    void deleteSettingsById(Long id);
}
