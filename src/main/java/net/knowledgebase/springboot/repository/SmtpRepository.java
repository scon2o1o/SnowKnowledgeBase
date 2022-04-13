package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Smtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmtpRepository extends JpaRepository<Smtp, Long> {
}
