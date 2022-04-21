package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.DownloadType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadTypeRepository extends JpaRepository<DownloadType, Long> {
}
