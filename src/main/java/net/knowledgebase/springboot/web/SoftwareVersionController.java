package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.Company;
import net.knowledgebase.springboot.model.SoftwareVersion;
import net.knowledgebase.springboot.service.SettingsService;
import net.knowledgebase.springboot.service.SoftwareVersionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SoftwareVersionController {
    private SettingsService settingsService;
    private SoftwareVersionService softwareVersionService;

    public SoftwareVersionController(SettingsService settingsService, SoftwareVersionService softwareVersionService) {
        this.settingsService = settingsService;
        this.softwareVersionService = softwareVersionService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/versions")
    public String versions(Model settingsModel, Model softwareVersionModel){
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        softwareVersionModel.addAttribute("versions", softwareVersionService.getAllSoftwareVersions());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "versions_home";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/versions/new")
    public String createVersion (Model model, Model settingsModel){
        SoftwareVersion version = new SoftwareVersion();
        model.addAttribute("version", version);
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "versions_create";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/versions")
    public String saveVersion(@ModelAttribute("version") SoftwareVersion version){
        try{
            softwareVersionService.saveSoftwareVersion(version);
            return "redirect:/versions?success";
        } catch(Exception e){
            return "redirect:/versions?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/versions/delete/{id}")
    public String deleteVersion(@PathVariable Long id) {
        softwareVersionService.deleteSoftwareVersionById(id);
        return "redirect:/versions";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/versions/edit/{id}")
    public String editLicenceForm(@PathVariable Long id, Model model, Model settingsModel) {
        model.addAttribute("version", softwareVersionService.getSoftwareVersionById(id));
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "versions_edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/versions/{id}")
    public String updateVersion(@PathVariable Long id, @ModelAttribute("version") SoftwareVersion softwareVersion, Model model) {
        try {
            SoftwareVersion existingVersion = softwareVersionService.getSoftwareVersionById(id);
            existingVersion.setId(id);
            existingVersion.setLatestVersion(softwareVersion.getLatestVersion());
            existingVersion.setMinVersion(softwareVersion.getMinVersion());
            existingVersion.setPlatform(softwareVersion.getPlatform());
            existingVersion.setUpdateUrl(softwareVersion.getUpdateUrl());
            softwareVersionService.updateSoftwareVersion(existingVersion);

            return "redirect:/versions?success";
        } catch (Exception e) {
            return "redirect:/versions?fail";
        }
    }
}


