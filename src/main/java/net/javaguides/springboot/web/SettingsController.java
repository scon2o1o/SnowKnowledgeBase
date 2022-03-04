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
    public String listSettings(Model model) {
        model.addAttribute("settings", settingsService.getAllSettings());
        return "settings";
    }

    @GetMapping("/settings/new")
    public String createSettingsForm(Model model) {
        Settings settings = new Settings();
        model.addAttribute("settings", settings);
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
    public String editSettingsForm(@PathVariable Long id, Model model) {
        model.addAttribute("settings", settingsService.getSettingsById(id));
        return "edit_settings";
    }

    @PostMapping("/settings/{id}")
    public String updateSettings(@PathVariable Long id, @ModelAttribute("settings") Settings settings, Model model) {
        try {
            Settings existingSettings = settingsService.getSettingsById(id);
            existingSettings.setUrl(settings.getUrl());
            settingsService.updateSettings(existingSettings);
            return "redirect:/settings?success";
        } catch (Exception e) {
            return "redirect:/settings?fail";
        }
    }

    @GetMapping("/settings/{id}")
    public String deleteSettings(@PathVariable Long id) {
        settingsService.deleteSettingsById(id);
        return "redirect:/settings";
    }
}
