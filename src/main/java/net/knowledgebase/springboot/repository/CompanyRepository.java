package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Company;
import net.knowledgebase.springboot.web.dto.DownloadDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Company findByName(String name);

    @Query("SELECT (name) FROM Company company WHERE company.id = ?1")
    String findCompanyNameByID(String id);
}
