package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {
}
