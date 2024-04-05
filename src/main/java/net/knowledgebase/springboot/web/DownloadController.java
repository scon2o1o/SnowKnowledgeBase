package net.knowledgebase.springboot.web;

import com.google.gson.Gson;
import net.knowledgebase.springboot.model.*;
import net.knowledgebase.springboot.repository.ClientRepository;
import net.knowledgebase.springboot.repository.CompanyRepository;
import net.knowledgebase.springboot.repository.LicenceRepository;
import net.knowledgebase.springboot.repository.UserRepository;
import net.knowledgebase.springboot.service.DownloadStorageService;
import net.knowledgebase.springboot.service.DownloadTypeService;
import net.knowledgebase.springboot.service.SettingsService;
import net.knowledgebase.springboot.web.dto.DownloadDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Controller
public class DownloadController {

    private DownloadStorageService downloadStorageService;
    private SettingsService settingsService;
    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private CompanyRepository companyRepository;
    private DownloadTypeService downloadTypeService;
    private LicenceRepository licenceRepository;

    private static final String AES_KEY = "Qq9f/bNDn2l64m9ZrvM4Qw==";
    private static final String AES_IV = "U&v58(2!/ftO9.w!";

    public DownloadController(LicenceRepository licenceRepository, DownloadStorageService downloadStorageService, SettingsService settingsService, UserRepository userRepository, ClientRepository clientRepository, CompanyRepository companyRepository, DownloadTypeService downloadTypeService) {
        super();
        this.licenceRepository = licenceRepository;
        this.downloadStorageService = downloadStorageService;
        this.settingsService = settingsService;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
        this.downloadTypeService = downloadTypeService;
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
        if (user.getRole().equals("Client")) {
            Client client = clientRepository.findByEmail(user.getEmail());
            Company company = companyRepository.findByName(client.getCompany());
            if (company.getStatus().equals("On Hold")) {
                return "account_not_active";
            }
        }
        List<DownloadDto> downloads = downloadStorageService.getDownloadsWithoutContent();
        model.addAttribute("downloads", downloads);
        return "downloads";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/downloads/upload")
    public String uploadMultipleDownloads(@RequestParam("downloads") MultipartFile[] downloads) {
        try {
            for (MultipartFile download : downloads) {
                downloadStorageService.saveDownload(download);
            }
            return "redirect:/downloads?success";
        } catch (Exception e) {
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/licencing/generate/{id}")
    public ResponseEntity<byte[]> generateLicence(@PathVariable Long id) throws Exception {
        Licence licence = licenceRepository.findById(id).orElse(null);

        licence.setDateGenerated(LocalDateTime.now());
        licence.setActive(true);

        if (licence.getSeasonalExpiryDate() != null && licence.getSeasonalExpiryDate().isBefore(licence.getDateGenerated().plusMonths(12))) {
            licence.setExpiryDate(licence.getSeasonalExpiryDate());
        } else {
            licence.setExpiryDate(licence.getDateGenerated().plusMonths(12));
        }

        licenceRepository.save(licence);
        Gson gson = new Gson();
        String jsonData = gson.toJson(licence);

        String encryptedJsonData = encryptAES(jsonData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment",
                licence.getClient().replace(" ", "_")
                        + "_"
                        + licence.getDateGenerated().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + ".lic");

        return ResponseEntity.ok()
                .headers(headers)
                .body(encryptedJsonData.getBytes());
    }

    private String encryptAES(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(AES_IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/downloads/delete/{id}")
    public String deleteDownload(@PathVariable Long id) {
        downloadStorageService.deleteDownloadById(id);
        return "redirect:/downloads";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/downloads/edit/{id}")
    public String editDownloadForm(@PathVariable Long id, Model model, Model settingsModel, Model downloadTypeModel) {
        model.addAttribute("download", downloadStorageService.findDownloadsWithoutContentById(id));
        downloadTypeModel.addAttribute("downloadtypes", downloadTypeService.getAllDownloadTypes());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "edit_download";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/downloads/edit/{id}")
    public String updateDownloadWithoutContent(@PathVariable Long id, @ModelAttribute("downloadDto") DownloadDto downloadDto) {
        try {
            downloadStorageService.updateDownloadWithoutContent(id, downloadDto.getCategory());
            return "redirect:/downloads?success";
        } catch (Exception e) {
            return "redirect:/downloads?fail";
        }
    }
}
