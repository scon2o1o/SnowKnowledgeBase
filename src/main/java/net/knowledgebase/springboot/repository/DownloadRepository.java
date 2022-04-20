package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Document;
import net.knowledgebase.springboot.model.Download;
import net.knowledgebase.springboot.web.dto.DownloadDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DownloadRepository extends JpaRepository<Download, Long> {

    @Query("SELECT new net.knowledgebase.springboot.web.dto.DownloadDto (id, name, type, dateAdded, size, category) FROM Download download")
    List<DownloadDto> findDownloadsWithoutContent();

    @Query("SELECT new net.knowledgebase.springboot.web.dto.DownloadDto (id, name, type, dateAdded, size, category) FROM Download download WHERE download.id = ?1")
    DownloadDto findDownloadsWithoutContentById(long id);

    @Modifying
    @Transactional
    @Query("UPDATE Download download set download.category = ?2 WHERE download.id = ?1")
    void updateDownloadWithoutContent(long id, String category);
}
