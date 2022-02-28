package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {
}
