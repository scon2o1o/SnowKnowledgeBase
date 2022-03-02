package net.javaguides.springboot.web;

import net.javaguides.springboot.model.Audit;
import net.javaguides.springboot.model.Document;
import net.javaguides.springboot.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DocumentController {

    private DocumentService documentService;
    private CategoryService categoryService;
    private SubcategoryService subcategoryService;
    private AuditService auditService;
    private SmtpService smtpService;

    public DocumentController(DocumentService documentService, CategoryService categoryService, SubcategoryService subcategoryService, AuditService auditService, SmtpService smtpService) {
        super();
        this.documentService = documentService;
        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
        this.auditService = auditService;
        this.smtpService = smtpService;
    }

    @GetMapping("/documents")
    public String listDocuments(Model model) {
        model.addAttribute("documents", documentService.getAllDocuments());
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
    public String createDocumentForm(Model model, Model categoryModel, Model SubcategoryModel) {
        Document document = new Document();
        model.addAttribute("document", document);
        listCategories(categoryModel);
        listSubcategories(SubcategoryModel);
        return "create_document";
    }

    @PostMapping("/documents")
    public String saveDocument(@ModelAttribute("document") Document document) {
        try {
            documentService.saveDocument(document);
            Audit audit = new Audit("Document " + document.getId() + ", '" + document.getName() + "' added to the database");
            auditService.saveAudit(audit);
            return "redirect:/documents?success";
        } catch (Exception e) {
            return "redirect:/documents?fail";
        }
    }

    @GetMapping("/documents/edit/{id}")
    public String editDocumentForm(@PathVariable Long id, Model model, Model categoryModel, Model subcategoryModel) {
        model.addAttribute("document", documentService.getDocumentById(id));
        listCategories(categoryModel);
        listSubcategories(subcategoryModel);
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
            if (!existingDocument.getUrl().equals(document.getUrl())) {
                Audit audit = new Audit("Document " + document.getId() + " updated. Url updated from '" + existingDocument.getUrl() + "' to '" + document.getUrl() + "'");
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
            if (!existingDocument.getAuthor().equals(document.getAuthor())) {
                Audit audit = new Audit("Document " + document.getId() + " updated. Author updated from '" + existingDocument.getAuthor() + "' to '" + document.getAuthor() + "'");
                auditService.saveAudit(audit);
            }
            existingDocument.setId(id);
            existingDocument.setName(document.getName());
            existingDocument.setDetails(document.getDetails());
            existingDocument.setUrl(document.getUrl());
            existingDocument.setCategory(document.getCategory());
            existingDocument.setDateAdded(document.getDateAdded());
            existingDocument.setAuthor(document.getAuthor());
            existingDocument.setLikes(document.getLikes());
            existingDocument.setLastModified(document.getLastModified());
            existingDocument.setSubcategory(document.getSubcategory());
            documentService.updateDocument(existingDocument);
            return "redirect:/documents?success";
        } catch (Exception e) {
            return "redirect:/documents?fail";
        }
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
