package net.javaguides.springboot.web;

import net.javaguides.springboot.model.Client;
import net.javaguides.springboot.model.Company;
import net.javaguides.springboot.model.Download;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.ClientRepository;
import net.javaguides.springboot.repository.CompanyRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.CategoryService;
import net.javaguides.springboot.service.DocumentService;
import net.javaguides.springboot.service.DownloadStorageService;
import net.javaguides.springboot.service.SettingsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private DocumentService documentService;
    private CategoryService categoryService;
    private SettingsService settingsService;
    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private CompanyRepository companyRepository;
    private DownloadStorageService downloadStorageService;

    public MainController(DocumentService documentService, CategoryService categoryService, SettingsService settingsService, UserRepository userRepository, ClientRepository clientRepository, CompanyRepository companyRepository, DownloadStorageService downloadStorageService) {
        super();
        this.documentService = documentService;
        this.categoryService = categoryService;
        this.settingsService = settingsService;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
        this.downloadStorageService = downloadStorageService;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName);
        if (user.getRole().equals("Client")){
            Client client = clientRepository.findByEmail(user.getEmail());
            Company company = companyRepository.findByName(client.getCompany());
            if (!"Active".equals(company.getStatus())){
                return "account_not_active";
            }
        }
        return "index";
    }

    @GetMapping("/files")
    public String downloads(Model documentModel, Model categoryModel, Model settingsModel, Model model) {
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
        if (user.getRole().equals("Client")){
            Client client = clientRepository.findByEmail(user.getEmail());
            Company company = companyRepository.findByName(client.getCompany());
            if (!"Active".equals(company.getStatus())){
                return "account_not_active";
            }
        }
        List<Download> downloads = downloadStorageService.getDownloads();
        model.addAttribute("downloads", downloads);
        return "client_downloads";
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName);
        if (user.getRole().equals("Client")){
            Client client = clientRepository.findByEmail(user.getEmail());
            Company company = companyRepository.findByName(client.getCompany());
            if (!"Active".equals(company.getStatus())){
                return "account_not_active";
            }
            return "page_not_found";
        }
        return "admin";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
