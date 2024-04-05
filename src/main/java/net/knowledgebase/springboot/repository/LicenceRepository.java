package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Licence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenceRepository extends JpaRepository<Licence, Long> {
}
