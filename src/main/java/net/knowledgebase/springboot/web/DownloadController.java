package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Client;
import net.knowledgebase.springboot.model.Company;
import net.knowledgebase.springboot.model.Download;
import net.knowledgebase.springboot.model.User;
import net.knowledgebase.springboot.repository.ClientRepository;
import net.knowledgebase.springboot.repository.CompanyRepository;
import net.knowledgebase.springboot.repository.UserRepository;
import net.knowledgebase.springboot.service.DownloadStorageService;
import net.knowledgebase.springboot.service.SettingsService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class DownloadController {

    private DownloadStorageService downloadStorageService;
    private SettingsService settingsService;
    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private CompanyRepository companyRepository;

    public DownloadController(DownloadStorageService downloadStorageService, SettingsService settingsService, UserRepository userRepository, ClientRepository clientRepository, CompanyRepository companyRepository) {
        super();
        this.downloadStorageService = downloadStorageService;
        this.settingsService = settingsService;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
    }

    @GetMapping("/downloads")
    public String getAllDownloads(Model model, Model settingsModel) {
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
            if (company.getStatus().equals("On Hold")){
                return "account_not_active";
            }
        }
        List<Download> downloads = downloadStorageService.getDownloads();
        model.addAttribute("downloads", downloads);
        return "downloads";
    }

    @GetMapping("/downloads/new")
    public String createDownloadForm(Model settingsModel) {
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "create_download";
    }

    @PostMapping("/downloads/upload")
    public String uploadMultipleDownloads(@RequestParam("downloads") MultipartFile[] downloads) {
        try{
            for (MultipartFile download : downloads) {
                downloadStorageService.saveDownload(download);
            }
            return "redirect:/downloads?success";
        } catch(Exception e){
            return "redirect:/downloads?fail";
        }
    }

    @GetMapping("/downloads/{id}")
    public ResponseEntity<ByteArrayResource> getDownload(@PathVariable Long id) {
        Download download = downloadStorageService.getDownload(id).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(download.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + download.getName() + "\"")
                .body(new ByteArrayResource(download.getContent()));
    }

    @GetMapping("/downloads/delete//{id}")
    public String deleteDownload(@PathVariable Long id) {
        downloadStorageService.deleteDownloadById(id);
        return "redirect:/downloads";
    }
}
