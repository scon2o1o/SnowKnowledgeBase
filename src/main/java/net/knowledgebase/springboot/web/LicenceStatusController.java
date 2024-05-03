package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.exception.ResourceNotFoundException;
import net.knowledgebase.springboot.model.Licence;
import net.knowledgebase.springboot.model.LicenceStatusResponse;
import net.knowledgebase.springboot.repository.LicenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/licence_status")
public class LicenceStatusController {

    private final LicenceRepository licenceRepository;

    @Autowired
    public LicenceStatusController(LicenceRepository licenceRepository) {
        this.licenceRepository = licenceRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LicenceStatusResponse> getLicenceStatus(@PathVariable Long id) {
        try {
            Licence licence = licenceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Licence not found with id " + id));

            LicenceStatusResponse response = new LicenceStatusResponse(true, licence.isActive(), "");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            LicenceStatusResponse response = new LicenceStatusResponse(false, false, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            LicenceStatusResponse response = new LicenceStatusResponse(false, false, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
