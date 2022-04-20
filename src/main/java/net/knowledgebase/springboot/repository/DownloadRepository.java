package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Document;
import net.knowledgebase.springboot.model.Download;
import net.knowledgebase.springboot.web.dto.DownloadDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DownloadRepository extends JpaRepository<Download, Long> {

    @Query("SELECT new net.knowledgebase.springboot.web.dto.DownloadDto (id, name, type, dateAdded, size) FROM  Download download")
    List<DownloadDto> findDownloadsWithoutContent();
}
