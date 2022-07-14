package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.service.DownloadStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadRestController {

    @Autowired
    DownloadStorageService downloadStorageService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/api/fileupload")
    public void uploadFile(@RequestParam("download") MultipartFile download) {
        try {
                downloadStorageService.saveDownload(download);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
