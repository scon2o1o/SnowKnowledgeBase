package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Client;
import net.knowledgebase.springboot.model.Company;
import net.knowledgebase.springboot.model.User;
import net.knowledgebase.springboot.repository.ClientRepository;
import net.knowledgebase.springboot.repository.CompanyRepository;
import net.knowledgebase.springboot.repository.UserRepository;
import net.knowledgebase.springboot.service.*;
import net.knowledgebase.springboot.web.dto.DownloadDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class MainController {

    private DocumentService documentService;
    private CategoryService categoryService;
    private SettingsService settingsService;
    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private CompanyRepository companyRepository;
    private DownloadStorageService downloadStorageService;
    private DownloadTypeService downloadTypeService;

    public MainController(DocumentService documentService, CategoryService categoryService, SettingsService settingsService, UserRepository userRepository, ClientRepository clientRepository, CompanyRepository companyRepository, DownloadStorageService downloadStorageService, DownloadTypeService downloadTypeService) {
        super();
        this.documentService = documentService;
        this.categoryService = categoryService;
        this.settingsService = settingsService;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
        this.downloadStorageService = downloadStorageService;
        this.downloadTypeService = downloadTypeService;
    }

    @GetMapping("/")
    public String home(Model documentModel, Model categoryModel, Model settingsModel) {
        documentModel.addAttribute("documents", documentService.findByInternalFalse());
        categoryModel.addAttribute("categories", categoryService.getAllCategories());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName);
        if (user.getRole().equals("Client")) {
            Client client = clientRepository.findByEmail(user.getEmail());
            Company company = companyRepository.findByName(client.getCompany());
            if ((!"Active".equals(company.getStatus()) || client.isSuspended()) == true) {
                return "account_not_active";
            }
        }
        return "home";
    }

    @GetMapping("/docs")
    public String docs(Model documentModel, Model categoryModel, Model settingsModel) {
        documentModel.addAttribute("documents", documentService.getAllDocuments());
        categoryModel.addAttribute("categories", categoryService.getAllCategories());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName);
        if (user.getRole().equals("Client")) {
            Client client = clientRepository.findByEmail(user.getEmail());
            Company company = companyRepository.findByName(client.getCompany());
            if (!"Active".equals(company.getStatus())) {
                return "account_not_active";
            }
        }
        return "index";
    }

    @GetMapping("/docs/{category}")
    public String filteredDocs(@PathVariable String category, Model documentModel, Model categoryModel, Model settingsModel) {
        switch(category) {
            case "q_bi":
                category = "Q BI";
                break;
            case "q_portal":
                category = "Q Portal";
                break;
            case "q_script":
                category = "Q Script";
                break;
            case "quantum_cloud":
                category = "Quantum Cloud";
                break;
            case "quantum_payroll":
                category = "Quantum Payroll";
                break;
            case "quantum_pay":
                category = "Quantum Pay";
                break;
            default:
                category = "";
        }
        if(category.equals("") || category.isEmpty()){
            documentModel.addAttribute("documents", documentService.findByInternalFalse());
        } else{
            documentModel.addAttribute("documents", documentService.findDocumentsByCategory(category));
        }
        categoryModel.addAttribute("categories", categoryService.getAllCategories());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName);
        if (user.getRole().equals("Client")) {
            Client client = clientRepository.findByEmail(user.getEmail());
            Company company = companyRepository.findByName(client.getCompany());
            if (!"Active".equals(company.getStatus())) {
                return "account_not_active";
            }
        }
        return "client_documents";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/docs/internal")
    public String internalDocs(Model documentModel, Model settingsModel) {
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        documentModel.addAttribute("documents", documentService.findByInternalTrue());
        return "documents_internal";
    }

    @GetMapping("/files")
    public String downloads(Model documentModel, Model downloadTypeModel, Model settingsModel, Model model) {
        downloadTypeModel.addAttribute("downloadtypes", downloadTypeService.getAllDownloadTypes());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName);
        if (user.getRole().equals("Client")) {
            Client client = clientRepository.findByEmail(user.getEmail());
            Company company = companyRepository.findByName(client.getCompany());
            if (!"Active".equals(company.getStatus())) {
                return "account_not_active";
            }
        }
        List<DownloadDto> downloads = downloadStorageService.getDownloadsWithoutContent();
        model.addAttribute("downloads", downloads);
        return "client_downloads";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
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
