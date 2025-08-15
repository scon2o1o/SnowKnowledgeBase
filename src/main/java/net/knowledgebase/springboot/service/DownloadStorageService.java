package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Download;
import net.knowledgebase.springboot.repository.DownloadRepository;
import net.knowledgebase.springboot.web.dto.DownloadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class DownloadStorageService {

    private final DataSource dataSource;

    @Autowired
    private DownloadRepository downloadRepository;

    public DownloadStorageService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Transactional
    public void saveDownload(MultipartFile download) throws IOException, SQLException {
        String downloadName = StringUtils.cleanPath(download.getOriginalFilename());

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO download (name, type, date_added, content, size) VALUES (?, ?, ?, ?, ?)"
             )) {
            ps.setString(1, downloadName);
            ps.setString(2, download.getContentType());
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            ps.setBinaryStream(4, download.getInputStream(), download.getSize());
            ps.setLong(5, download.getSize());

            ps.executeUpdate();
        }
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

    public InputStream getDownloadStream(Long id) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT content FROM download WHERE id = ?");
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            // Don't close conn/ps/rs here — caller will do it when stream is closed
            return rs.getBinaryStream("content");
        } else {
            rs.close();
            ps.close();
            conn.close();
            throw new EntityNotFoundException("Download not found");
        }
    }

}
