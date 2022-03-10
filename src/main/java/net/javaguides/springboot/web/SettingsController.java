package net.javaguides.springboot.web;

import net.javaguides.springboot.model.Settings;
import net.javaguides.springboot.service.AuditService;
import net.javaguides.springboot.service.SettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SettingsController {

    private SettingsService settingsService;
    private AuditService auditService;

    public SettingsController(SettingsService settingsService, AuditService auditService) {
        super();
        this.settingsService = settingsService;
        this.auditService = auditService;
    }

    @GetMapping("/settings")
    public String listSettings(Model settingsModel, Model responseModel) {
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settingsList = settingsService.getAllSettings();
        if (settingsList.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "settings";
    }

    @GetMapping("/settings/new")
    public String createSettingsForm(Model model, Model settingsModel) {
        Settings settings = new Settings();
        model.addAttribute("settings", settings);
        settingsModel.addAttribute("settings1", settingsService.getAllSettings());
        List settingsList = settingsService.getAllSettings();
        if (settingsList.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "create_settings";
    }

    @PostMapping("/settings")
    public String saveSettings(@ModelAttribute("settings") Settings settings) {
        try {
            settingsService.saveSettings(settings);
            return "redirect:/settings?success";
        } catch (Exception e) {
            return "redirect:/settings?fail";
        }
    }

    @GetMapping("/settings/edit/{id}")
    public String editSettingsForm(@PathVariable int id, Model model, Model settingsModel) {
        model.addAttribute("settings", settingsService.getSettingsById(id));
        settingsModel.addAttribute("settings1", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "edit_settings";
    }

    @PostMapping("/settings/{id}")
    public String updateSettings(@PathVariable int id, @ModelAttribute("settings") Settings settings, Model model) {
        try {
            Settings existingSettings = settingsService.getSettingsById(id);
            existingSettings.setUrl(settings.getUrl());
            existingSettings.setEmail(settings.isEmail());
            existingSettings.setTitle(settings.getTitle());
            settingsService.updateSettings(existingSettings);
            return "redirect:/settings?success";
        } catch (Exception e) {
            return "redirect:/settings?fail";
        }
    }

    @GetMapping("/settings/{id}")
    public String deleteSettings(@PathVariable int id) {
        settingsService.deleteSettingsById(id);
        return "redirect:/settings";
    }
}
