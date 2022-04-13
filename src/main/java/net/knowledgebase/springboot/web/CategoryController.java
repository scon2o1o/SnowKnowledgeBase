package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.Category;
import net.knowledgebase.springboot.service.AuditService;
import net.knowledgebase.springboot.service.CategoryService;
import net.knowledgebase.springboot.service.SettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CategoryController {
    private CategoryService categoryService;
    private AuditService auditService;
    private SettingsService settingsService;

    public CategoryController(CategoryService categoryService, AuditService auditService, SettingsService settingsService) {
        super();
        this.categoryService = categoryService;
        this.auditService = auditService;
        this.settingsService = settingsService;
    }

    @GetMapping("/categories")
    public String listCategories(Model model, Model settingsModel) {
        model.addAttribute("categories", categoryService.getAllCategories());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "categories";
    }

    @GetMapping("/categories/new")
    public String createCategoryForm(Model model, Model settingsModel) {

        Category category = new Category();
        model.addAttribute("category", category);
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "create_category";
    }

    @PostMapping("/categories")
    public String saveCategory(@ModelAttribute("category") Category category) {
        try {
            Audit audit = new Audit("Category '" + category.getName() + "' added");
            auditService.saveAudit(audit);
            categoryService.saveCategory(category);
            return "redirect:/categories?success";
        } catch (Exception e) {
            return "redirect:/categories/new?fail";
        }
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model, Model settingsModel) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "edit_category";
    }

    @PostMapping("/categories/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute("category") Category category,
                                 Model model) {
        try {
            Category existingCategory = categoryService.getCategoryById(id);
            if (!existingCategory.getName().equals(category.getName())) {
                Audit audit = new Audit("Category " + category.getId() + " updated. Name updated from '" + existingCategory.getName() + "' to '" + category.getName() + "'");
                auditService.saveAudit(audit);
            }
            existingCategory.setId(id);
            existingCategory.setName(category.getName());
            categoryService.updateCategory(existingCategory);
            return "redirect:/categories?success";
        } catch (Exception e) {
            return "redirect:/categories?fail";
        }
    }

    @GetMapping("/categories/{id}")
    public String deleteCategory(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            Audit audit = new Audit("Category '" + category.getName() + "' deleted");
            auditService.saveAudit(audit);
            categoryService.deleteCategoryById(id);
            return "redirect:/categories?success";
        } catch (Exception e) {
            return "redirect:/categories?fail";
        }
    }
}
