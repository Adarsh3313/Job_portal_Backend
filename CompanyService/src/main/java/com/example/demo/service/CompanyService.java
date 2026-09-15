package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.entity.Company;
import com.example.demo.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;

    public String createCompany(
            CompanyRequest request) {

        Company company = Company.builder()
                .companyName(request.getCompanyName())
                .description(request.getDescription())
                .website(request.getWebsite())
                .industry(request.getIndustry())
                .location(request.getLocation())
                .email(request.getEmail())
                .phone(request.getPhone())
                .logoUrl(request.getLogoUrl())
                .verified(false)
                .build();

        repository.save(company);

        return "Company Created Successfully";
    }

    public List<Company> getAllCompanies() {
        return repository.findAll();
    }

    public Company getCompanyById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company Not Found"));
    }

    public String deleteCompany(Long id) {

        repository.deleteById(id);

        return "Company Deleted Successfully";
    }
}