package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.Subcategory;
import net.knowledgebase.springboot.service.AuditService;
import net.knowledgebase.springboot.service.SettingsService;
import net.knowledgebase.springboot.service.SubcategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SubcategoryController {
    private SubcategoryService subcategoryService;
    private AuditService auditService;
    private SettingsService settingsService;

    public SubcategoryController(SubcategoryService subcategoryService, AuditService auditService, SettingsService settingsService) {
        super();
        this.subcategoryService = subcategoryService;
        this.auditService = auditService;
        this.settingsService = settingsService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/subcategories")
    public String listSubcategories(Model model, Model settingsModel) {
        model.addAttribute("subcategories", subcategoryService.getAllSubcategories());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "subcategories";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/subcategories/new")
    public String createSubcategoryForm(Model model, Model settingsModel) {
        Subcategory subcategory = new Subcategory();
        model.addAttribute("subcategory", subcategory);
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "create_subcategory";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/subcategories")
    public String saveSubcategory(@ModelAttribute("subcategory") Subcategory subcategory) {
        try {
            Audit audit = new Audit("Subcategory '" + subcategory.getName() + "' added");
            auditService.saveAudit(audit);
            subcategoryService.saveSubcategory(subcategory);
            return "redirect:/subcategories?success";
        } catch (Exception e) {
            return "redirect:/subcategories/new?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/subcategories/edit/{id}")
    public String editSubcategoryForm(@PathVariable Long id, Model model, Model settingsModel) {
        model.addAttribute("subcategory", subcategoryService.getSubcategoryById(id));
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "edit_subcategory";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/subcategories/{id}")
    public String updateSubcategory(@PathVariable Long id,
                                    @ModelAttribute("subcategory") Subcategory subcategory,
                                    Model model) {
        try {
            Subcategory existingSubcategory = subcategoryService.getSubcategoryById(id);
            if (!existingSubcategory.getName().equals(subcategory.getName())) {
                Audit audit = new Audit("Subcategory " + subcategory.getId() + " updated. Name updated from '" + existingSubcategory.getName() + "' to '" + subcategory.getName() + "'");
                auditService.saveAudit(audit);
            }
            existingSubcategory.setId(id);
            existingSubcategory.setName(subcategory.getName());
            subcategoryService.updateSubcategory(existingSubcategory);
            return "redirect:/subcategories?success";
        } catch (Exception e) {
            return "redirect:/subcategories?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/subcategories/{id}")
    public String deleteSubcategory(@PathVariable Long id) {
        try {
            Subcategory subcategory = subcategoryService.getSubcategoryById(id);
            Audit audit = new Audit("Subcategory '" + subcategory.getName() + "' deleted");
            auditService.saveAudit(audit);
            subcategoryService.deleteSubcategoryById(id);
            return "redirect:/subcategories?success";
        } catch (Exception e) {
            return "redirect:/subcategories?fail";
        }
    }
}
