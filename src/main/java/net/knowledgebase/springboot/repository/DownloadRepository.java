package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Download;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadRepository extends JpaRepository<Download, Long> {
}
