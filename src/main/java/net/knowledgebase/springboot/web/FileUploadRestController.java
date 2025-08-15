package net.knowledgebase.springboot.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.knowledgebase.springboot.model.Licence;
import net.knowledgebase.springboot.service.DownloadStorageService;
import net.knowledgebase.springboot.service.LicenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class FileUploadRestController {

    @Autowired
    DownloadStorageService downloadStorageService;

    @Autowired
    LicenceService licenceService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/api/fileupload")
    public ResponseEntity<String> uploadFile(@RequestParam("download") MultipartFile download) {
        try {
            downloadStorageService.saveDownload(download);
            return ResponseEntity.ok("Upload successful");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/api/licenceupload")
    public void licenceFileUpload(@RequestParam("download") MultipartFile download, @RequestParam("client") String client) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(download.getInputStream());
            Licence licence = new Licence();

            licence.setClient(client);
            licence.setChecksum(rootNode.get("checksum").asText());
            String dateString = rootNode.get("requestDateTime").asText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            licence.setDateRequested(dateTime);
            licence.setExpiryDate(dateTime.plusMonths(12));
            licence.setReadOnly(rootNode.get("readonly").asBoolean());
            licence.setEmployees(rootNode.get("maximumNumberOfEmployees").asLong());

            JsonNode numberOfCompaniesNode = rootNode.get("numberOfCompanies");
            licence.setUnlimitedCompanies(numberOfCompaniesNode.get("unlimited").asBoolean());
            licence.setCompanies(numberOfCompaniesNode.get("maximumNumberOfCompanies").asLong());

            JsonNode modules = rootNode.get("modules");
            licence.setModule1(modules.get("module1").asBoolean());
            licence.setModule2(modules.get("module2").asBoolean());
            licence.setModule3(modules.get("module3").asBoolean());
            licence.setModule4(modules.get("module4").asBoolean());
            licence.setModule5(modules.get("module5").asBoolean());
            licence.setModule6(modules.get("module6").asBoolean());
            licence.setModule7(modules.get("module7").asBoolean());
            licence.setModule8(modules.get("module8").asBoolean());
            licence.setModule9(modules.get("module9").asBoolean());
            licence.setModule10(modules.get("module10").asBoolean());
            licence.setModule11(modules.get("module11").asBoolean());
            licence.setModule12(modules.get("module12").asBoolean());
            licence.setModule13(modules.get("module13").asBoolean());
            licence.setModule14(modules.get("module14").asBoolean());
            licence.setModule15(modules.get("module15").asBoolean());
            licence.setModule16(modules.get("module16").asBoolean());
            licence.setModule21(modules.get("module21").asBoolean());
            licence.setModule31(modules.get("module31").asBoolean());

            JsonNode seasonal = rootNode.get("seasonal");
            if (seasonal != null && !seasonal.isNull()) {
                dateString = seasonal.get("expiryDate").asText();
                dateTime = LocalDateTime.parse(dateString, formatter);
                licence.setSeasonalExpiryDate(dateTime);
                licence.setExpiryDate((dateTime));
            }
            licenceService.saveLicence(licence);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
