package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.Smtp;
import net.knowledgebase.springboot.service.AuditService;
import net.knowledgebase.springboot.service.SettingsService;
import net.knowledgebase.springboot.service.SmtpService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SmtpController {

    private SmtpService smtpService;
    private AuditService auditService;
    private SettingsService settingsService;

    public SmtpController(SmtpService smtpService, AuditService auditService, SettingsService settingsService) {
        super();
        this.smtpService = smtpService;
        this.auditService = auditService;
        this.settingsService = settingsService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/smtp")
    public String listSmtp(Model smtpModel, Model responseModel, Model settingsModel) {
        smtpModel.addAttribute("smtp", smtpService.getAllSmtp());
        List ls = smtpService.getAllSmtp();
        if (ls.isEmpty()) {
            responseModel.addAttribute("response", "NoData");
        }
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "smtp";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/smtp/new")
    public String createSmtpForm(Model model, Model settingsModel) {
        Smtp smtp = new Smtp();
        model.addAttribute("smtp", smtp);
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "create_smtp";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/smtp")
    public String saveSmtp(@ModelAttribute("smtp") Smtp smtp) {
        try {
            Audit audit = new Audit("SMTP settings added");
            auditService.saveAudit(audit);
            smtpService.saveSmtp(smtp);
            return "redirect:/smtp?success";
        } catch (Exception e) {
            return "redirect:/smtp?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/smtp/edit/{id}")
    public String editSmtpForm(@PathVariable Long id, Model model, Model settingsModel) {
        model.addAttribute("smtp", smtpService.getSmtpById(id));
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "edit_smtp";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/smtp/{id}")
    public String updateSmtp(@PathVariable Long id,
                             @ModelAttribute("smtp") Smtp smtp,
                             Model model) {
        Smtp existingSmtp = smtpService.getSmtpById(id);
        if (!existingSmtp.getServer().equals(smtp.getServer())) {
            Audit audit = new Audit("SMTP settings updated. Server updated from '" + existingSmtp.getServer() + "' to '" + smtp.getServer() + "'");
            auditService.saveAudit(audit);
        }
        if (existingSmtp.getPort() != (smtp.getPort())) {
            Audit audit = new Audit("SMTP settings updated. Port updated from '" + existingSmtp.getPort() + "' to '" + smtp.getPort() + "'");
            auditService.saveAudit(audit);
        }
        if (!existingSmtp.getAuth().equals(smtp.getAuth())) {
            Audit audit = new Audit("SMTP settings updated. Port updated from '" + existingSmtp.getPort() + "' to '" + smtp.getPort() + "'");
            auditService.saveAudit(audit);
        }
        existingSmtp.setId(id);
        existingSmtp.setServer(smtp.getServer());
        existingSmtp.setPort(smtp.getPort());
        existingSmtp.setAuth(smtp.getAuth());
        existingSmtp.setStarttls(smtp.getStarttls());
        existingSmtp.setFromAddress(smtp.getFromAddress());
        existingSmtp.setUsername(smtp.getUsername());
        existingSmtp.setPassword(smtp.getPassword());
        smtpService.updateSmtp(existingSmtp);
        return "redirect:/smtp";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/smtp/{id}")
    public String deleteSmtp(@PathVariable Long id) {
        try {
            Audit audit = new Audit("SMTP settings deleted");
            smtpService.deleteSmtpById(id);
            return "redirect:/smtp?success";
        } catch (Exception e) {
            return "redirect:/smtp?fail";
        }
    }
}
