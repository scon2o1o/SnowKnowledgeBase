package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Download;
import net.knowledgebase.springboot.repository.DownloadRepository;
import net.knowledgebase.springboot.web.dto.DownloadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class DownloadStorageService {

    @Autowired
    private DownloadRepository downloadRepository;

    public Download saveDownload(MultipartFile download) {
        String downloadName = StringUtils.cleanPath(download.getOriginalFilename());
        try {
            Download download1 = new Download(downloadName, download.getContentType(), download.getBytes(), download.getSize());
            return downloadRepository.save(download1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<Download> getDownload(Long id){
        return downloadRepository.findById(id);
    }

    public List<Download> getDownloads(){
        return downloadRepository.findAll();
    }

    public List<DownloadDto> getDownloadsWithoutContent(){
        return downloadRepository.findDownloadsWithoutContent();
    }

    public DownloadDto findDownloadsWithoutContentById(Long id){
        return downloadRepository.findDownloadsWithoutContentById(id);
    }

    public void updateDownloadWithoutContent(Long id, String category){
        downloadRepository.updateDownloadWithoutContent(id, category);
    }

    public void deleteDownloadById(long id){
        downloadRepository.deleteById(id);
    }
}
