package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.SoftwareVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoftwareVersionRepository extends JpaRepository<SoftwareVersion, Long> {
}
