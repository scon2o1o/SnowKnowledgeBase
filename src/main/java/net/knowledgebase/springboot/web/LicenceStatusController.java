package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Licence;
import net.knowledgebase.springboot.repository.LicenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/licence_status")
public class LicenceStatusController {

    @Autowired
    LicenceRepository licenceRepository;

    @GetMapping("/{id}")
    public boolean getLicenceStatus(@PathVariable("id") Long id)
    {
        Licence licence = licenceRepository.findById(id).orElseThrow();
        return licence.isActive();
    }
}
