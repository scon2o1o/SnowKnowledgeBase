package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Settings;
import net.javaguides.springboot.repository.SettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsServiceImpl implements SettingsService {

    private SettingsRepository settingsRepository;

    public SettingsServiceImpl(SettingsRepository settingsRepository) {
        super();
        this.settingsRepository = settingsRepository;
    }

    @Override
    public List<Settings> getAllSettings() {
        return settingsRepository.findAll();
    }

    @Override
    public Settings saveSettings(Settings settings) {
        return settingsRepository.save(settings);
    }

    @Override
    public Settings getSettingsById(Long id) {
        return settingsRepository.findById(id).get();
    }

    @Override
    public Settings updateSettings(Settings settings) {
        return settingsRepository.save(settings);
    }

    @Override
    public void deleteSettingsById(Long id) {
        settingsRepository.deleteById(id);
    }
}
