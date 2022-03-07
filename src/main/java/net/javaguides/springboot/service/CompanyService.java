package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Company;

import java.util.List;

public interface CompanyService {
    List<Company> getAllCompanies();

    Company saveCompany(Company company);

    Company getCompanyById(Long id);

    Company updateCompany(Company company);

    void deleteCompanyById(Long id);
}
