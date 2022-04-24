package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.service.AuditService;
import net.knowledgebase.springboot.service.SettingsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuditController {
    private AuditService auditService;
    private SettingsService settingsService;

    public AuditController(AuditService auditService, SettingsService settingsService) {
        super();
        this.auditService = auditService;
        this.settingsService = settingsService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/audit")
    public String listAudit(Model model, Model settingsModel){
        model.addAttribute("audit", auditService.getAllAudit());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "audit";
    }
    @PostMapping("/audit")
    public String saveAudit(@ModelAttribute("audit") Audit audit) {
        auditService.saveAudit(audit);
        return "Saved";
    }
}
