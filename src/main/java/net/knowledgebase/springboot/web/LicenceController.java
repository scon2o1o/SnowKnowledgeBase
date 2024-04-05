package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Licence;
import net.knowledgebase.springboot.service.CompanyService;
import net.knowledgebase.springboot.service.LicenceService;
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
public class LicenceController {
    private LicenceService licenceService;
    private CompanyService companyService;
    private SettingsService settingsService;

    public LicenceController(LicenceService licenceService, CompanyService companyService, SettingsService settingsService) {
        this.licenceService = licenceService;
        this.companyService = companyService;
        this.settingsService = settingsService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/licencing")
    public String licence(Model settingsModel, Model companyModel, Model licenceModel) {
        companyModel.addAttribute("company", companyService.getAllCompanies());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        licenceModel.addAttribute("licence", licenceService.getAllLicences());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "licence_home";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/licencing/new")
    public String createLicence (Model model, Model settingsModel, Model companyModel){
        companyModel.addAttribute("company", companyService.getAllCompanies());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "licence_create";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/licencing/delete/{id}")
    public String deleteLicence(@PathVariable Long id) {
        licenceService.deleteLicenceById(id);
        return "redirect:/licencing";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/licencing/edit/{id}")
    public String editLicenceForm(@PathVariable Long id, Model model, Model settingsModel, Model companyModel) {
        model.addAttribute("licence", licenceService.getLicenceById(id));
        companyModel.addAttribute("company", companyService.getAllCompanies());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "licence_edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/licencing/{id}")
    public String updateLicence(@PathVariable Long id, @ModelAttribute("licence") Licence licence, Model model) {
        try {
            Licence existingLicence = licenceService.getLicenceById(id);
            existingLicence.setId(id);
            existingLicence.setActive(licence.isActive());
            existingLicence.setChecksum(licence.getChecksum());
            existingLicence.setClient(licence.getClient());
            existingLicence.setCompanies(licence.getCompanies());
            existingLicence.setDateGenerated(licence.getDateGenerated());
            existingLicence.setDateRequested(licence.getDateRequested());
            existingLicence.setEmployees(licence.getEmployees());
            existingLicence.setExpiryDate(licence.getExpiryDate());
            existingLicence.setModule1(licence.isModule1());
            existingLicence.setModule2(licence.isModule2());
            existingLicence.setModule3(licence.isModule3());
            existingLicence.setModule4(licence.isModule4());
            existingLicence.setModule5(licence.isModule5());
            existingLicence.setModule6(licence.isModule6());
            existingLicence.setModule7(licence.isModule7());
            existingLicence.setModule8(licence.isModule8());
            existingLicence.setModule9(licence.isModule9());
            existingLicence.setModule10(licence.isModule10());
            existingLicence.setModule11(licence.isModule11());
            existingLicence.setModule12(licence.isModule12());
            existingLicence.setModule13(licence.isModule13());
            existingLicence.setModule14(licence.isModule14());
            existingLicence.setModule15(licence.isModule15());
            existingLicence.setModule16(licence.isModule16());
            existingLicence.setModule21(licence.isModule21());
            existingLicence.setModule31(licence.isModule31());
            existingLicence.setSeasonalExpiryDate(licence.getSeasonalExpiryDate());
            existingLicence.setUnlimitedCompanies(licence.isUnlimitedCompanies());

            licenceService.updateLicence(existingLicence);

            return "redirect:/licencing?success";
        } catch (Exception e) {
            return "redirect:/licencing?fail";
        }
    }
}
