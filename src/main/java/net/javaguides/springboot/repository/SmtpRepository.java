package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Smtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmtpRepository extends JpaRepository<Smtp, Long> {
}
