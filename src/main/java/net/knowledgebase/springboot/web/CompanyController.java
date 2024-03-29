package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.Company;
import net.knowledgebase.springboot.service.AuditService;
import net.knowledgebase.springboot.service.CompanyService;
import net.knowledgebase.springboot.service.SettingsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CompanyController {

    private CompanyService companyService;
    private AuditService auditService;
    private SettingsService settingsService;

    public CompanyController(CompanyService companyService, AuditService auditService, SettingsService settingsService) {
        super();
        this.companyService = companyService;
        this.auditService = auditService;
        this.settingsService = settingsService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/companies")
    public String listCompanies(Model model, Model settingsModel){
        model.addAttribute("companies", companyService.getAllCompanies());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "companies";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/companies/new")
    public String createCompanyForm(Model model, Model settingsModel){
        Company company = new Company();
        model.addAttribute("company", company);
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "create_company";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/companies")
    public String saveCompany(@ModelAttribute("company") Company company){
        try{
            Audit audit = new Audit("Company '" + company.getName() + "' added");
            auditService.saveAudit(audit);
            companyService.saveCompany(company);
            return "redirect:/companies?success";
        } catch(Exception e){
            return "redirect:/companies?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/companies/edit/{id}")
    public String editCompanyForm(@PathVariable String id, Model model, Model settingsModel) {
        model.addAttribute("company", companyService.getCompanyById(id));
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "edit_company";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/companies/{id}")
    public String updateCompany(@PathVariable String id,
                                 @ModelAttribute("company") Company company,
                                 Model model) {
        try {
            Company existingCompany = companyService.getCompanyById(id);
            if (!existingCompany.getName().equals(company.getName())) {
                Audit audit = new Audit("Company " + company.getId() + " updated. Name updated from '" + existingCompany.getName() + "' to '" + company.getName() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingCompany.getType().equals(company.getType())) {
                Audit audit = new Audit("Company " + company.getId() + " updated. Type updated from '" + existingCompany.getType() + "' to '" + company.getType() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingCompany.getPhone().equals(company.getPhone())) {
                Audit audit = new Audit("Company " + company.getId() + " updated. Phone updated from '" + existingCompany.getPhone() + "' to '" + company.getPhone() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingCompany.getAddr1().equals(company.getAddr1())) {
                Audit audit = new Audit("Company " + company.getId() + " updated. Address Line 1 updated from '" + existingCompany.getAddr1() + "' to '" + company.getAddr1() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingCompany.getAddr2().equals(company.getAddr2())) {
                Audit audit = new Audit("Company " + company.getId() + " updated. Address Line 2 updated from '" + existingCompany.getAddr2() + "' to '" + company.getAddr2() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingCompany.getAddr3().equals(company.getAddr3())) {
                Audit audit = new Audit("Company " + company.getId() + " updated. Address Line 3 updated from '" + existingCompany.getAddr3() + "' to '" + company.getAddr3() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingCompany.getAddr4().equals(company.getAddr4())) {
                Audit audit = new Audit("Company " + company.getId() + " updated. Address Line 4 updated from '" + existingCompany.getAddr4() + "' to '" + company.getAddr4() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingCompany.getEircode().equals(company.getEircode())) {
                Audit audit = new Audit("Company " + company.getId() + " updated. Eircode updated from '" + existingCompany.getEircode() + "' to '" + company.getEircode() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingCompany.getWebsite().equals(company.getWebsite())) {
                Audit audit = new Audit("Company " + company.getId() + " updated. Website updated from '" + existingCompany.getWebsite() + "' to '" + company.getWebsite() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingCompany.getEmail().equals(company.getEmail())) {
                Audit audit = new Audit("Company " + company.getId() + " updated. Email updated from '" + existingCompany.getEmail() + "' to '" + company.getEmail() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingCompany.getStatus().equals(company.getStatus())) {
                Audit audit = new Audit("Company " + company.getId() + " updated. Status updated from '" + existingCompany.getStatus() + "' to '" + company.getStatus() + "'");
                auditService.saveAudit(audit);
            }
            existingCompany.setId(id);
            existingCompany.setName(company.getName());
            existingCompany.setType(company.getType());
            existingCompany.setPhone(company.getPhone());
            existingCompany.setAddr1(company.getAddr1());
            existingCompany.setAddr2(company.getAddr2());
            existingCompany.setAddr3(company.getAddr3());
            existingCompany.setAddr4(company.getAddr4());
            existingCompany.setEircode(company.getEircode());
            existingCompany.setWebsite(company.getWebsite());
            existingCompany.setEmail(company.getEmail());
            existingCompany.setStatus(company.getStatus());
            existingCompany.setPortal(company.getPortal());
            existingCompany.setToken(company.getToken());
            companyService.updateCompany(existingCompany);
            return "redirect:/companies?success";
        } catch (Exception e) {
            return "redirect:/companies?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/companies/{id}")
    public String deleteCompany(@PathVariable String id) {
        try {
            Company company = companyService.getCompanyById(id);
            Audit audit = new Audit("Company '" + company.getName() + "' deleted");
            auditService.saveAudit(audit);
            companyService.deleteCompanyById(id);
            return "redirect:/companies?success";
        } catch (Exception e) {
            return "redirect:/companies?fail";
        }
    }
}
