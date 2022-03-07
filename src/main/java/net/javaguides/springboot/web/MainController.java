package net.javaguides.springboot.web;

import net.javaguides.springboot.service.CategoryService;
import net.javaguides.springboot.service.DocumentService;
import net.javaguides.springboot.service.SettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private DocumentService documentService;
    private CategoryService categoryService;
    private SettingsService settingsService;

    public MainController(DocumentService documentService, CategoryService categoryService, SettingsService settingsService) {
        super();
        this.documentService = documentService;
        this.categoryService = categoryService;
        this.settingsService = settingsService;
    }

    @GetMapping("/")
    public String home(Model documentModel, Model categoryModel, Model settingsModel) {
        documentModel.addAttribute("documents", documentService.getAllDocuments());
        categoryModel.addAttribute("categories", categoryService.getAllCategories());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model settingsModel) {
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "admin";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
