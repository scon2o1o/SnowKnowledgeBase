package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.SoftwareVersion;

import java.util.List;

public interface SoftwareVersionService {
    List<SoftwareVersion> getAllSoftwareVersions();

    SoftwareVersion saveSoftwareVersion(SoftwareVersion version);

    SoftwareVersion getSoftwareVersionById(Long id);

    SoftwareVersion updateSoftwareVersion(SoftwareVersion version);

    void deleteSoftwareVersionById(Long id);
}
