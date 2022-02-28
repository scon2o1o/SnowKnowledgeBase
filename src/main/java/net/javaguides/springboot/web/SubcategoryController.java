package net.javaguides.springboot.web;

import net.javaguides.springboot.model.Audit;
import net.javaguides.springboot.model.Subcategory;
import net.javaguides.springboot.service.AuditService;
import net.javaguides.springboot.service.SubcategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SubcategoryController {
    private SubcategoryService subcategoryService;
    private AuditService auditService;

    public SubcategoryController(SubcategoryService subcategoryService, AuditService auditService) {
        super();
        this.subcategoryService = subcategoryService;
        this.auditService = auditService;
    }

    @GetMapping("/subcategories")
    public String listSubcategories(Model model) {
        model.addAttribute("subcategories", subcategoryService.getAllSubcategories());
        return "subcategories";
    }

    @GetMapping("/subcategories/new")
    public String createSubcategoryForm(Model model) {

        Subcategory subcategory = new Subcategory();
        model.addAttribute("subcategory", subcategory);
        return "create_subcategory";
    }

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

    @GetMapping("/subcategories/edit/{id}")
    public String editSubcategoryForm(@PathVariable Long id, Model model) {
        model.addAttribute("subcategory", subcategoryService.getSubcategoryById(id));
        return "edit_subcategory";
    }

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
