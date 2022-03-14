package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Download;
import net.javaguides.springboot.repository.DownloadRepository;
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

    public void deleteDownloadById(long id){
        downloadRepository.deleteById(id);
    }
}
