package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Download;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadRepository extends JpaRepository<Download, Long> {
}
