package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.SoftwareVersion;
import net.knowledgebase.springboot.repository.SoftwareVersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoftwareVersionImpl implements SoftwareVersionService{

    private SoftwareVersionRepository softwareVersionRepository;

    public SoftwareVersionImpl(SoftwareVersionRepository softwareVersionRepository) {
        super();
        this.softwareVersionRepository = softwareVersionRepository;
    }

    @Override
    public List<SoftwareVersion> getAllSoftwareVersions() {
        return softwareVersionRepository.findAll();
    }

    @Override
    public SoftwareVersion saveSoftwareVersion(SoftwareVersion version) {
        return softwareVersionRepository.save(version);
    }

    @Override
    public SoftwareVersion getSoftwareVersionById(Long id) {
        return softwareVersionRepository.findById(id).get();
    }

    @Override
    public SoftwareVersion updateSoftwareVersion(SoftwareVersion version) {
        return softwareVersionRepository.save(version);
    }

    @Override
    public void deleteSoftwareVersionById(Long id) {
        softwareVersionRepository.deleteById(id);
    }
}
