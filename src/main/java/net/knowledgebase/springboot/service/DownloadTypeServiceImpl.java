package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.DownloadType;
import net.knowledgebase.springboot.repository.DownloadTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DownloadTypeServiceImpl implements DownloadTypeService {

    private DownloadTypeRepository downloadTypeRepository;

    public DownloadTypeServiceImpl(DownloadTypeRepository downloadTypeRepository) {
        super();
        this.downloadTypeRepository = downloadTypeRepository;
    }

    @Override
    public List<DownloadType> getAllDownloadTypes() {
        return downloadTypeRepository.findAll();
    }

    @Override
    public DownloadType saveDownloadType(DownloadType downloadType) {
        return downloadTypeRepository.save(downloadType);
    }

    @Override
    public DownloadType getDownloadTypeById(Long id) {
        return downloadTypeRepository.findById(id).get();
    }

    @Override
    public DownloadType updateDownloadType(DownloadType downloadType) {
        return downloadTypeRepository.save(downloadType);
    }

    @Override
    public void deleteDownloadTypeById(Long id) {
        downloadTypeRepository.deleteById(id);
    }
}
