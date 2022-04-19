package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.Document;
import net.knowledgebase.springboot.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

@Controller
public class DocumentController {

    private DocumentService documentService;
    private CategoryService categoryService;
    private SubcategoryService subcategoryService;
    private AuditService auditService;
    private SmtpService smtpService;
    private SettingsService settingsService;

    public DocumentController(DocumentService documentService, CategoryService categoryService,
                              SubcategoryService subcategoryService, AuditService auditService,
                              SmtpService smtpService, SettingsService settingsService) {
        super();
        this.documentService = documentService;
        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
        this.auditService = auditService;
        this.smtpService = smtpService;
        this.settingsService = settingsService;
    }

    @GetMapping("/documents")
    public String listDocuments(Model model, Model settingsModel) {
        model.addAttribute("documents", documentService.getAllDocuments());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "documents";
    }

    public String listCategories(Model categoryModel) {
        categoryModel.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    public String listSubcategories(Model subcategoryModel) {
        subcategoryModel.addAttribute("subcategories", subcategoryService.getAllSubcategories());
        return "subcategories";
    }

    @GetMapping("/documents/new")
    public String createDocumentForm(Model model, Model categoryModel, Model SubcategoryModel, Model settingsModel) {
        Document document = new Document();
        model.addAttribute("document", document);
        listCategories(categoryModel);
        listSubcategories(SubcategoryModel);
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "create_document";
    }

    @PostMapping("/documents")
    public String saveDocument(@ModelAttribute("document") Document document) {
        try {
            document.setDateAdded(new Date());
            document.setLastModified(new Date());
            documentService.saveDocument(document);
            Audit audit = new Audit("Document " + document.getId() + ", '" + document.getName() + "' added to the database");
            auditService.saveAudit(audit);
            return "redirect:/documents?success";
        } catch (Exception e) {
            return "redirect:/documents?fail";
        }
    }

    @GetMapping("/documents/edit/{id}")
    public String editDocumentForm(@PathVariable Long id, Model model, Model categoryModel, Model subcategoryModel, Model settingsModel) {
        model.addAttribute("document", documentService.getDocumentById(id));
        listCategories(categoryModel);
        listSubcategories(subcategoryModel);
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "edit_document";
    }

    @PostMapping("/documents/{id}")
    public String updateDocument(@PathVariable Long id, @ModelAttribute("document") Document document, Model model) {
        try {
            Document existingDocument = documentService.getDocumentById(id);
            if (!existingDocument.getName().equals(document.getName())) {
                Audit audit = new Audit("Document " + document.getId() + " updated. Name updated from '" + existingDocument.getName() + "' to '" + document.getName() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingDocument.getDetails().equals(document.getDetails())) {
                Audit audit = new Audit("Document " + document.getId() + " updated. Details updated from '" + existingDocument.getDetails() + "' to '" + document.getDetails() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingDocument.getCategory().equals(document.getCategory())) {
                Audit audit = new Audit("Document " + document.getId() + " updated. Category updated from '" + existingDocument.getCategory() + "' to '" + document.getCategory() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingDocument.getSubcategory().equals(document.getSubcategory())) {
                Audit audit = new Audit("Document " + document.getId() + " updated. Subcategory updated from '" + existingDocument.getSubcategory() + "' to '" + document.getSubcategory() + "'");
                auditService.saveAudit(audit);
            }
            if (!existingDocument.getContent().equals(document.getContent())) {
                Audit audit = new Audit("Document " + document.getId() + " updated. Content updated");
                auditService.saveAudit(audit);
            }
            existingDocument.setId(id);
            existingDocument.setName(document.getName());
            existingDocument.setDetails(document.getDetails());
            existingDocument.setCategory(document.getCategory());
            existingDocument.setLastModified(new Date());
            existingDocument.setSubcategory(document.getSubcategory());
            existingDocument.setContent(document.getContent());
            documentService.updateDocument(existingDocument);
            return "redirect:/documents?success";
        } catch (Exception e) {
            return "redirect:/documents?fail";
        }
    }

    @GetMapping("/documents/view/{id}")
    public String viewDocumentForm(@PathVariable Long id, Model model, Model settingsModel) {
        model.addAttribute("document", documentService.getDocumentById(id));
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "view_document";
    }

    @GetMapping("/documents/view_document/{id}")
    public String viewDocumentFormClient(@PathVariable Long id, Model model, Model settingsModel) {
        model.addAttribute("document", documentService.getDocumentById(id));
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "view_document_client";
    }

    @GetMapping("/documents/{id}")
    public String deleteDocument(@PathVariable Long id) {
        Document existingDocument = documentService.getDocumentById(id);
        documentService.deleteDocumentById(id);
        Audit audit = new Audit("Document " + existingDocument.getId() + ", '" + existingDocument.getName() + "' deleted from the database");
        auditService.saveAudit(audit);
        return "redirect:/documents";
    }
}
