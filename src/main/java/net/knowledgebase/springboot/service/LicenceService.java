package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Licence;

import java.util.List;

public interface LicenceService {
    List<Licence> getAllLicences();

    Licence saveLicence(Licence licence);

    Licence getLicenceById(Long id);

    Licence updateLicence(Licence licence);

    void deleteLicenceById(Long id);
}
