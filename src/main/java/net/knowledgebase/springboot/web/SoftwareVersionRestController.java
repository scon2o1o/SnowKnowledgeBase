package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.SoftwareVersion;
import net.knowledgebase.springboot.model.SoftwareVersionDto;
import net.knowledgebase.springboot.repository.SoftwareVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Version")
public class SoftwareVersionRestController {

    private final SoftwareVersionRepository softwareVersionRepository;

    @Autowired
    public SoftwareVersionRestController(SoftwareVersionRepository softwareVersionRepository) {
        this.softwareVersionRepository = softwareVersionRepository;
    }

    @GetMapping("/minVersion")
    public ResponseEntity<List<SoftwareVersionDto>> getSoftwareVersion() {
        List<SoftwareVersion> versions = softwareVersionRepository.findAll();
        List<SoftwareVersionDto> dtos = versions.stream()
                .map(v -> new SoftwareVersionDto(
                        v.getPlatform(),
                        v.getMinVersion(),
                        v.getLatestVersion(),
                        v.getUpdateUrl()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}