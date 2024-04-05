package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Licence;
import net.knowledgebase.springboot.repository.LicenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicenceServiceImpl implements LicenceService{

    private LicenceRepository licenceRepository;

    public LicenceServiceImpl(LicenceRepository licenceRepository) {
        super();
        this.licenceRepository = licenceRepository;
    }

    @Override
    public List<Licence> getAllLicences(){
        return licenceRepository.findAll();
    }

    @Override
    public Licence saveLicence(Licence licence){
        return licenceRepository.save(licence);
    }

    @Override
    public Licence getLicenceById(Long id){
        return licenceRepository.findById(id).get();
    }

    @Override
    public Licence updateLicence(Licence licence){
        return licenceRepository.save(licence);
    }

    @Override
    public void deleteLicenceById(Long id){
        licenceRepository.deleteById(id);
    }
}
