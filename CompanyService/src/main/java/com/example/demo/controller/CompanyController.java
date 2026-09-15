package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.entity.Company;
import com.example.demo.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService service;

    @PostMapping
    public String createCompany(
            @RequestBody CompanyRequest request) {

        return service.createCompany(request);
    }

    @GetMapping
    public List<Company> getAllCompanies() {

        return service.getAllCompanies();
    }

    @GetMapping("/{id}")
    public Company getCompanyById(
            @PathVariable Long id) {

        return service.getCompanyById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteCompany(
            @PathVariable Long id) {

        return service.deleteCompany(id);
    }
}