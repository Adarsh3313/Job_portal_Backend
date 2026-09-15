package com.example.demo.dto;

import lombok.Data;

@Data
public class CompanyRequest {

    private String companyName;

    private String description;

    private String website;

    private String industry;

    private String location;

    private String email;

    private String phone;

    private String logoUrl;
}