package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Company;

import java.util.List;

public interface CompanyService {

    List<Company> getAllCompanies();

    Company saveCompany(Company company);

    Company getCompanyById(String id);

    Company updateCompany(Company company);

    void deleteCompanyById(String id);
}
