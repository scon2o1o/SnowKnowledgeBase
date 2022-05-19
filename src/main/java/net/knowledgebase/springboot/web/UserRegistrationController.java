package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.Settings;
import net.knowledgebase.springboot.repository.SettingsRepository;
import net.knowledgebase.springboot.service.AuditService;
import net.knowledgebase.springboot.service.SettingsService;
import net.knowledgebase.springboot.service.SmtpService;
import net.knowledgebase.springboot.service.UserService;
import net.knowledgebase.springboot.web.dto.UserRegistrationDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserService userService;
    private AuditService auditService;
    private SmtpService smtpService;
    private SettingsRepository settingsRepository;
    private SettingsService settingsService;

    public UserRegistrationController(UserService userService, AuditService auditService, SmtpService smtpService, SettingsRepository settingsRepository, SettingsService settingsService) {
        super();
        this.userService = userService;
        this.auditService = auditService;
        this.smtpService = smtpService;
        this.settingsRepository = settingsRepository;
        this.settingsService = settingsService;
    }

    public Settings getSettings() {
        List<Settings> databaseSettings = settingsRepository.findAll();
        Settings settings = new Settings();
        settings.setId(databaseSettings.get(0).getId());
        settings.setUrl(databaseSettings.get(0).getUrl());
        settings.setEmail(databaseSettings.get(0).isEmail());
        return settings;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping
    public String showRegistrationForm(Model settingsModel) {
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "registration";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, HttpServletRequest request) {
        try {
            Settings settings = getSettings();
            Audit audit = new Audit("User: " + registrationDto.getEmail() + " added");
            auditService.saveAudit(audit);
            userService.save(registrationDto);
            return "redirect:/users?success";
        } catch (Exception e) {
            return "redirect:/registration?fail";
        }
    }
}
