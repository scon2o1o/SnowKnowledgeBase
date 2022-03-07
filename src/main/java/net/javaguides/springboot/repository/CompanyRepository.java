package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
