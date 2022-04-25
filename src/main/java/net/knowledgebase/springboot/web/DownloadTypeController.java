package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.DownloadType;
import net.knowledgebase.springboot.service.AuditService;
import net.knowledgebase.springboot.service.DownloadTypeService;
import net.knowledgebase.springboot.service.SettingsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class DownloadTypeController {
    private AuditService auditService;
    private SettingsService settingsService;
    private DownloadTypeService downloadTypeService;

    public DownloadTypeController(AuditService auditService, SettingsService settingsService, DownloadTypeService downloadTypeService) {
        super();
        this.auditService = auditService;
        this.settingsService = settingsService;
        this.downloadTypeService = downloadTypeService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/downloadtypes")
    public String listDownloadTypes(Model model, Model settingsModel) {
        model.addAttribute("downloadtypes", downloadTypeService.getAllDownloadTypes());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "downloadtypes";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/downloadtypes/new")
    public String createDownloadTypeForm(Model model, Model settingsModel) {
        DownloadType downloadType = new DownloadType();
        model.addAttribute("downloadtype", downloadType);
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "create_downloadtype";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/downloadtypes")
    public String saveDownloadType(@ModelAttribute("downloadtype") DownloadType downloadType) {
        try {
            Audit audit = new Audit("Download type '" + downloadType.getName() + "' added");
            auditService.saveAudit(audit);
            downloadTypeService.saveDownloadType(downloadType);
            return "redirect:/downloadtypes?success";
        } catch (Exception e) {
            return "redirect:/downloadtypes/new?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/downloadtypes/edit/{id}")
    public String editDownloadTypeForm(@PathVariable Long id, Model model, Model settingsModel) {
        model.addAttribute("downloadtype", downloadTypeService.getDownloadTypeById(id));
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "edit_downloadtype";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/downloadtypes/{id}")
    public String updateDownloadType(@PathVariable Long id,
                                     @ModelAttribute("downloadtype") DownloadType downloadType,
                                     Model model) {
        try {
            DownloadType existingDownloadType = downloadTypeService.getDownloadTypeById(id);
            if (!existingDownloadType.getName().equals(downloadType.getName())) {
                Audit audit = new Audit("Download type " + downloadType.getId() + " updated. Name updated from '" + existingDownloadType.getName() + "' to '" + downloadType.getName() + "'");
                auditService.saveAudit(audit);
            }
            existingDownloadType.setId(id);
            existingDownloadType.setName(downloadType.getName());
            downloadTypeService.updateDownloadType(existingDownloadType);
            return "redirect:/downloadtypes?success";
        } catch (Exception e) {
            return "redirect:/downloadtypes?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/downloadtypes/{id}")
    public String deleteDownloadType(@PathVariable Long id) {
        try {
            DownloadType downloadType = downloadTypeService.getDownloadTypeById(id);
            Audit audit = new Audit("Download type '" + downloadType.getName() + "' deleted");
            auditService.saveAudit(audit);
            downloadTypeService.deleteDownloadTypeById(id);
            return "redirect:/downloadtypes?success";
        } catch (Exception e) {
            return "redirect:/downloadtypes?fail";
        }
    }
}
