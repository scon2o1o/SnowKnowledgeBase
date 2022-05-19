package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.exception.ResourceNotFoundException;
import net.knowledgebase.springboot.model.Company;
import net.knowledgebase.springboot.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyRestController {

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping
    public Company createCompany(@RequestBody Company company){
        return companyRepository.save(company);
    }

    @PostMapping("/multiple")
    public String createMultipleCompanies(@RequestBody List<Company> companies){
        for (Company company : companies) {
            companyRepository.save(company);
        }
        return "Success";
    }

    @PutMapping("{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable String id, @RequestBody Company companyDetails){
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No company found with id: " + id));
        company.setAddr1(companyDetails.getAddr1());
        company.setAddr2(companyDetails.getAddr2());
        company.setAddr3(companyDetails.getAddr3());
        company.setAddr4(companyDetails.getAddr4());
        company.setEircode(companyDetails.getEircode());
        company.setEmail(companyDetails.getEmail());
        company.setName(companyDetails.getName());
        company.setPhone(companyDetails.getPhone());
        company.setStatus(companyDetails.getStatus());
        company.setType(companyDetails.getType());
        company.setWebsite(companyDetails.getWebsite());
        companyRepository.save(company);
        return ResponseEntity.ok(company);
    }
}
