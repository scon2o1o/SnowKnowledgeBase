package net.knowledgebase.springboot.repository;

import net.knowledgebase.springboot.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Company findByName(String name);

    @Query("SELECT (name) FROM Company company WHERE company.id = ?1")
    String findCompanyNameByID(String id);

    List<Company> findByPortalIsNotNull();

    @Query("SELECT (portal) FROM Company company WHERE company.id = ?1")
    String findPortalByID(String id);

    @Query("SELECT (token) FROM Company company WHERE company.id = ?1")
    String findTokenByID(String id);
}
